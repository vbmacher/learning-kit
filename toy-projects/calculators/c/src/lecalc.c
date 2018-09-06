/**
 * lecalc.c
 *
 * Copyright 2011 Peter Jakubco
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <config.h>

#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <lecalc.h>
#include "parser.h"
#include "lexer.h"

extern int quit;
extern int use;
extern char *filename;
extern int help;
extern double result;

struct { char *name; Tree *tree;} VARIABLES[MAX_VARIABLES]; // variable can be a function
int var_ix = 0;  // index to new variable
static int eval_error = 0; // evaluation error?

static int mode_of_operation;
static yyscan_t yyscanner; //Lexical analyzer

void xxerror(char *s1,char *s2){
  fprintf(stderr,"ERROR: %s %s\n",s1,s2);
}

/**
 * Finds a variable.
 *
 * @param name Name of the variable
 * @errorMissing if it is non-zero, then in a case that variable is not found,
 *  error message is printed.
 * @return index of the variable in VARIABLES array.
 */
int findVAR(char *name, int errorMissing) {
  int i;
  for(i=0; i < var_ix; i++) {
    if (strcmp(name,VARIABLES[i].name) == 0)
      return i;
  }
  if (errorMissing)
    xxerror("Variable was not defined: ",name);
  return -1;
}

/**
 * Create new variable or store a value into existing variable.
 *
 * @param name Name of the variable
 * @param tree Value of the variable - tree that must be evaluated.
 * @return index of the variable in VARIABLES array, or -1 if no variable was created.
 */
int saveVAR(char *name, Tree *tree) {
  int ix = findVAR(name, 0);
  if (ix == -1) {
    if (var_ix == MAX_VARIABLES) {
      xxerror("Variable can not be defined (out of space): ", name);
      return -1;
    }
    ix = var_ix++;
  } else {
    free(VARIABLES[ix].tree);
  }
  VARIABLES[ix].name = name;
  VARIABLES[ix].tree = tree;
  return ix;
}

/**
 * Compute a factorial (non-recursive version).
 *
 * @param x the number
 * @return factorial of x, or -1 if (x<0)
 */
int fact(int x) {
  if (x < 0) {
    return -1;
  }

  int result;
  for (result = 1; x > 1; x--) {
    result *= x;
  }
  return result;
}

/**
 * Convert integer number to string representation with specified radix.
 * 
 * @param num the number to be converted
 * @radix Radix of the string representation. It should be either 2 or 8.
 * @return string representation of num
 */
char *intToRadix(int num, int radix) {
  static char result[30];
  int i = 0, j = 29;
  unsigned char sign;

  if (num < 0) {
    sign = 1;
    num = 0 - num;
  } else
    sign = 0;

  result[29] = 0;
  for (i = num; i > 0; i = i/radix)
    result[--j] = "01234567"[i % radix];

  if (radix == 8)
    result[--j] = 'o';
  else if (radix == 2)
    result[--j] = 'b';
  else
    result[--j] = '0' + radix;

  if (sign)
    result[--j] = '-';
  return (char*)(result + j);
}

/**
 * Convert a double number into string representation with specified radix.
 * 
 * @param num the number to be converted
 * @radix Radix of the string representation. It should be from 2 to 16.
 * @return string representation of num
 */
char *doubleToRadix(double num, int radix) {
  static char result[40];
  int i = 0, j = 19;
  double x;

  int integer = (int)num;

  i = 0;
  result[j] = '.';
  for (x = (num-(int)num)*radix; i < 20; x *= radix, i++) {
    result[++j] = "0123456789ABCDEF"[(int)x];
    x = x - (int)x;
  }
  result[39] = 0;
  j = 19;
  result[18] = '0';
  for (i = (int)num; i > 0; i = i/radix)
    result[--j] = "0123456789ABCDEF"[i % radix];
  if (j == 19)
    j--;

  return (char*)(result + j);
}

int isEvalError() {
  return eval_error;
}


/**
 * Evaluate a tree.
 *
 * @param tree The tree to evaluate
 */
double evalTree(Tree *tree) {
  double left = 0.0;
  double right = 0.0;
  eval_error = 0;

  switch(tree->nodetype) {
    case operator_node:
      if (tree->body.an_operator.left != NULL)
        left = evalTree(tree->body.an_operator.left);
      if (eval_error)
        return;

      if (tree->body.an_operator.right != NULL)
        right = evalTree(tree->body.an_operator.right);
      if (eval_error)
        return;

      switch (tree->body.an_operator.oper) {
        case OP_ADD:
          if (tree->body.an_operator.left != NULL)
            return left + right;
          else
            return right;
        case OP_SUB:
          if (tree->body.an_operator.left != NULL)
            return left - right;
          else
            return -right;
        case OP_MUL:
          return left * right;
        case OP_DIV:
          if (right == 0) {
            xxerror("Divide by"," 0");
            break;
          }
          return left / right;
        case M_SIN:
          return sin(right);
        case M_COS:
          return cos(right);
        case M_TAN:
          return tan(right);
        case M_COTAN:
          return 1/tan(right);
        case M_LOG:
          return log10(right);
        case M_LOG2:
          return log(right)/log(2);
        case M_LOGE:
          return log(right);
        case M_SQRT:
          if (right < 0) {
            xxerror("Square root of a number must not be negative","!");
            break;
          }
          return sqrt(right);
        case M_CEIL:
          return ceil(right);
        case M_FLOOR:
          return floor(right);
        case OP_POW:
          return pow(left,right);
        case OP_MOD:
          return fmod(left,right);
        case OP_FACT:
          return fact(left);
        default:
          xxerror("Unknown ", "operator/function");
      }
      break;
    case value_node:
      return tree->body.value;
    case variable_node: {
      int idx = findVAR(tree->body.variable, 1);
      if (idx == -1) {
        break;
      }
      Tree *tr = VARIABLES[idx].tree;
      if (tr == NULL) {
        xxerror("Bad variable:", tree->body.variable);
        break;
      }
      return evalTree(tr);
    }
  }
  eval_error = 1;
  return 0;
}

void printResult(FILE *output_file, double value) {
  if (mode_of_operation == MODE_INTERACTIVE)
    fprintf(output_file, "Result was saved to variable R.\n");

  if ((int)value == value) {
    int a = (int)value;
    fprintf(output_file, "   %d\n  x%X\n",a,a);
    fprintf(output_file, "   %s\n", intToRadix(a,8));
    fprintf(output_file, "   %s\n", intToRadix(a,2));
  }
  else {
    fprintf(output_file, "  %lf\n",value);
    fprintf(output_file, " x%s\n",doubleToRadix(value,16));
    fprintf(output_file, " o%s\n",doubleToRadix(value,8));
    fprintf(output_file, " b%s\n",doubleToRadix(value,2));
  }
}

/**
 * Print usage and help using kCalc.
 */
void printHelp(FILE *output_file) {
  fprintf(output_file,"This is a command-line calculator. It supports the following commands:\n" \
         "  h, help    - this command.\n" \
         "  use 'file' - process external file.\n" \
         "  q, quit    - quit the calculator.\n\n" \
         "There are supported both integral and decimal numbers, in various radixes:\n" \
         "  decadic (`1.3`), hexadecimal (`4x`), octal (`-23.05o`), binary (`101b`)\n\n" \
         "Results are printed in all radixes. The following operators are supported:\n" \
         "  + (addition, unary plus), - (substraction, unary minus), * (multiplication),\n" \
         "  / (divide), %, mod (divide remainder), ^ (power), ! (factorial)\n\n" \
         "The following math functions are supported:\n" \
         "  sin   - sinus, input in radians (sin PI=0)\n" \
         "  cos   - cosinus, input in radians (cos PI=-1)\n" \
         "  tan   - tangens, input in radians (tan PI=0)\n" \
         "  cotan - cotangens, input in radians (cotan (PI/2)=0)\n" \
         "  log   - logarithm with base 10 (log 10=1)\n" \
         "  log2  - logarithm with base 2 (log2 2=1)\n" \
         "  ln    - logarithm with base E (ln E =1)\n" \
         "  sqrt  - square root (sqrt 16=4)\n" \
         "  ceil  - smallest integral value that is not less than input (ceil 4.3=5)\n" \
         "  floor - greatest integral value that is not greater than input (floor 4.6=4)\n\n" \
         "The following constants are supported:\n" \
         "  PI - The Ludolf PI number (3.141592...)\n" \
         "  E  - The Euler number (2.71828182...)\n\n" \
         "This calculator also supports variables that store values and expressions:\n" \
         "  var = 4.23 * x\n" \
         "  y=(-2 * var + x)/ (4*var^2)\nEvery result is stored to the R variable.\n");
}

/**
 * Initialize parser.
 */
void initialize(FILE *input_file, FILE *output_file) {
  if (mode_of_operation == MODE_INTERACTIVE) {
    fprintf(output_file, PACKAGE_STRING "\n(c) Copyright 2010-2011, P.Jakubco <" 
           PACKAGE_BUGREPORT ">\n\nStarting interactive mode.\n(Type 'help' for help.)\n");
  }

  yylex_init(&yyscanner);
  yyset_in(input_file, yyscanner);
  yyset_out(output_file, yyscanner);
}

/**
 * Process external file.
 *
 * @param input_file
 *   File stream of main input file
 * @param file_name
 *   Processed file name
 */
void process_file(FILE *input_file, char *file_name) {
  FILE *use_file = fopen(file_name, "r");
  if (use_file == NULL) {
    xxerror("File name cannot be processed:", file_name);
    return;
  }
  yyset_in(use_file, yyscanner);
  while (!quit && !feof(use_file)) {
    yyparse(yyscanner);
    if (use == 1) {
      use = 0;
      xxerror("USE command cannot be used now", "");
    }
    if (help == 1) {
      // ignore help requests in processed file
      help = 0;
    }
  }
  yyset_in(input_file, yyscanner);
  fclose(use_file);
  yyrestart(input_file,yyscanner);
}

int main(int ac,char *av[]){
  FILE *input_file = stdin;
  FILE *output_file = stdout;
  mode_of_operation = MODE_INTERACTIVE;
  if (ac > 1) {
    input_file = fopen(av[1], "r");
    if (input_file == NULL)
      input_file = stdin;
    else
      mode_of_operation = MODE_FILE;
  }
  initialize(input_file, output_file);

  while(!quit && !feof(input_file)) {
    if (mode_of_operation == MODE_INTERACTIVE) {
      fprintf(output_file, "\n> ");
      fflush(output_file);
    }

    // Parse expression
    yyparse(yyscanner);
    if (use == 1) {
      use = 0;
      process_file(input_file, filename);
    }
    if (help == 1) {
      help = 0;
      if (mode_of_operation == MODE_INTERACTIVE)
        printHelp(output_file);
    } else {
      printResult(output_file, result);
    }
  }
  yylex_destroy(yyscanner);

  return(0);
}

