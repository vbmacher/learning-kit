/**
 * kcalc.c
 *
 * (c) Copyright 2011, P. Jakubčo <pjakubco@gmail.com>
 *
 * KISS, YAGNI
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

#include <config.h>

#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <kcalc.h>
#include "parser.h"
#include "lexer.h"

extern int quit;
extern int use;

struct { char *name; Tree *tree;} VARIABLES[MAX_VARIABLES]; // variable can be a function
int var_ix = 0;  // index to new variable
static int eval_error = 0; // evaluation error?

void xxerror(char *s1,char *s2){
  fprintf(stdout,"ERROR: %s %s\n",s1,s2);
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
 * Recursively compute a factorial.
 *
 * @param x the number
 * @return factorial of x
 */
int fact(int x) {
  if (x == 0)
    return 1;
  else if (x > 0)
    return x * fact(x-1);
  else
    return -1;
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

void printResult(double value) {
  printf("Result was saved to variable R.\n");
  if ((int)value == value) {
    int a = (int)value;
    printf("   %d\n  x%X\n",a,a);
    printf("   %s\n", intToRadix(a,8));
    printf("   %s\n", intToRadix(a,2));
  }
  else {
    printf("  %lf\n",value);
    printf(" x%s\n",doubleToRadix(value,16));
    printf(" o%s\n",doubleToRadix(value,8));
    printf(" b%s\n",doubleToRadix(value,2));
  }
}

void printHelp() {
  printf("This is a command-line calculator. It supports the following commands:\n" \
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

int main(int ac,char *av[]){
  FILE *ff = stdin;

  if (ac > 1) {
    ff = fopen(av[1], "r");
    if (ff == NULL)
      ff = stdin;
  }
 
  printf(PACKAGE_STRING "\n(c) Copyright 2010-2011, P.Jakubco <" PACKAGE_BUGREPORT ">\n\nStarting interactive mode.\n(Type 'help' for help.)\n");
  
  yyscan_t yyscanner;
  yylex_init(&yyscanner);
  yyset_in(ff, yyscanner);
  yyset_out(stdout, yyscanner);

  while(!quit && !feof(ff)) {
    printf(">");
    yyparse(yyscanner);
    if (use == 1) {
      use = 0;
      FILE *fin = fopen(filename, "r");
      if (fin == NULL) {
        xxerror("File name cannot be opened:", filename);
        continue;
      }
      yyset_in(fin, yyscanner);
      while (!quit && !feof(fin)) {
        yyparse(yyscanner);
        if (use == 1) {
          use = 0;
          xxerror("USE command cannot be used now", "");
        }
      }
      yyset_in(ff, yyscanner);
      fclose(fin);
      yyrestart(ff,yyscanner);
    }
  }
  yylex_destroy(yyscanner);

  return(0);
}

