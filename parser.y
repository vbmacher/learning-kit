%{
/*
 * parser.y
 *
 * KEEP IT SIMPLE, STUPID
 * some things just: YOU AREN'T GONNA NEED IT
 *
 * Copyright (C) 2010-2011 Peter Jakubco <pjakubco at gmail.com>
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

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <math.h>

int yyparse(void), yylex(void);

typedef struct {
  unsigned char sig;
  int ival;
  double dval;
} ATRV;
  
struct { char *name; ATRV attr;} VARIABLES[30];
int var_ix = 0;  // index to new variable

int tmp, ttmp;
double tmp2,ttmp2;
static int quit = 0;
  
%}

%union {
  char* name;
  ATRV attr;
}

/* terminal symbols */
%token <name> VARIABLE
%token <attr> INT_VAL
%token <attr> DBL_VAL

%token LPAR RPAR OP_EQU HELP
%token QUIT

%token M_SIN M_COS M_TAN M_COTAN M_LOG M_LOG2 M_LOGE M_SQRT M_CEIL M_FLOOR
%token CONST_PI CONST_E

/* priorities and associativity of operators */
%right OP_EQU
%left OP_ADD OP_SUB
%left	OP_MUL OP_DIV
%left OP_MOD
%right UADD USUB
%left OP_POW
%left OP_FACT

/* non-terminal symbols values */
%type <attr> Exp Primary
%type <attr> Constant

%%
/* Rules */

Subor  : 
       | Exp 
         { 
            tmp = findVAR("R",0);
            if (tmp == -1)
              tmp = saveVAR("R",&$1);
            if (tmp != -1) {
              VARIABLES[tmp].attr.sig = $1.sig;
              VARIABLES[tmp].attr.ival = $1.ival;
              VARIABLES[tmp].attr.dval = $1.dval;
            }
            printResult(&$1); 
         }
       | HELP
         { printHelp(); }
       | QUIT
         { quit = 1; }
       ;

Exp   : Primary
        { $$ = $1; }
      | Exp OP_ADD Exp
        { if ($1.sig == 1)
            tmp2 = $1.ival;
          else if ($1.sig == 2)
            tmp2 = $1.dval;
          if ($3.sig == 1)
            ttmp2 = $3.ival;
          else if ($3.sig == 2)
            ttmp2 = $3.dval;
          $$.sig = 2;
          $$.dval = tmp2 + ttmp2;
          if ($$.dval == (int)$$.dval) {
            $$.sig = 1;
            $$.ival = (int)$$.dval;
          }
        }
      | Exp OP_SUB Exp
        { if ($1.sig == 1)
            tmp2 = $1.ival;
          else if ($1.sig == 2)
            tmp2 = $1.dval;
          if ($3.sig == 1)
            ttmp2 = $3.ival;
          else if ($3.sig == 2)
            ttmp2 = $3.dval;
          $$.sig = 2;
          $$.dval = tmp2 - ttmp2;
          if ($$.dval == (int)$$.dval) {
            $$.sig = 1;
            $$.ival = (int)$$.dval;
          }
        }
      | Exp OP_MUL Exp
        { if ($1.sig == 1)
            tmp2 = $1.ival;
          else if ($1.sig == 2)
            tmp2 = $1.dval;
          if ($3.sig == 1)
            ttmp2 = $3.ival;
          else if ($3.sig == 2)
            ttmp2 = $3.dval;
          $$.sig = 2;
          $$.dval = tmp2 * ttmp2;
          if ($$.dval == (int)$$.dval) {
            $$.sig = 1;
            $$.ival = (int)$$.dval;
          }
        }
      | Exp OP_DIV Exp
        { if ($1.sig == 1)
            tmp2 = $1.ival;
          else if ($1.sig == 2)
            tmp2 = $1.dval;
          if ($3.sig == 1)
            ttmp2 = $3.ival;
          else if ($3.sig == 2)
            ttmp2 = $3.dval;
          $$.sig = 2;
          if (ttmp2 == 0) {
            xxerror("Divide by"," 0");
            break;
          }
          $$.dval = tmp2 / ttmp2;
          if ($$.dval == (int)$$.dval) {
            $$.sig = 1;
            $$.ival = (int)$$.dval;
          }
        }
      | Exp OP_POW Exp
        { if ($1.sig == 1)
            tmp2 = $1.ival;
          else if ($1.sig == 2)
            tmp2 = $1.dval;
          if ($3.sig == 1)
            ttmp2 = $3.ival;
          else if ($3.sig == 2)
            ttmp2 = $3.dval;
          $$.sig = 2;
          $$.dval = pow(tmp2,ttmp2);
        }
      | Exp OP_MOD Exp
        { if ($1.sig == 1)
            tmp = $1.ival;
          else if ($1.sig == 2)
            tmp = (int)$1.dval;
          if ($3.sig == 1)
            ttmp = $3.ival;
          else if ($3.sig == 2)
            ttmp = (int)$3.dval;
          $$.sig = 1;
          $$.ival = tmp % ttmp;
        }
      | Exp OP_FACT
        { if ($1.sig == 1)
            tmp = $1.ival;
          else if ($1.sig == 2) {
            tmp = (int)$1.dval;
            fprintf(stdout, "Warning: Using decimal value as integer\n");
          }
          $$.sig = 1;
          $$.ival = (int)fact(tmp);
        }
      | M_SIN Exp
        { if ($2.sig == 1)
            tmp2 = $2.ival;
          else if ($2.sig == 2)
            tmp2 = $2.dval;
          $$.sig = 2;
          $$.dval = sin(tmp2);
        }
      | M_COS Exp
        { if ($2.sig == 1)
            tmp2 = $2.ival;
          else if ($2.sig == 2)
            tmp2 = $2.dval;
          $$.sig = 2;
          $$.dval = cos(tmp2);
        }
      | M_TAN Exp
        { if ($2.sig == 1)
            tmp2 = $2.ival;
          else if ($2.sig == 2)
            tmp2 = $2.dval;
          $$.sig = 2;
          $$.dval = tan(tmp2);
        }
      | M_COTAN Exp
        { if ($2.sig == 1)
            tmp2 = $2.ival;
          else if ($2.sig == 2)
            tmp2 = $2.dval;
          $$.sig = 2;
          ttmp2 = tan(tmp2);
          if (ttmp2 == 0)
            $$.dval = 0;
          else
            $$.dval = 1/ttmp2;
        }
      | M_LOG Exp
        { if ($2.sig == 1)
            tmp2 = $2.ival;
          else if ($2.sig == 2)
            tmp2 = $2.dval;
          $$.sig = 2;
          $$.dval = log10(tmp2);
        }
      | M_LOG2 Exp
        { if ($2.sig == 1)
            tmp2 = $2.ival;
          else if ($2.sig == 2)
            tmp2 = $2.dval;
          $$.sig = 2;
          $$.dval = log(tmp2)/log(2);
        }
      | M_LOGE Exp
        { if ($2.sig == 1)
            tmp2 = $2.ival;
          else if ($2.sig == 2)
            tmp2 = $2.dval;
          $$.sig = 2;
          $$.dval = log(tmp2);
        }
      | M_SQRT Exp
        { if ($2.sig == 1)
            tmp2 = $2.ival;
          else if ($2.sig == 2)
            tmp2 = $2.dval;
          $$.sig = 2;
          $$.dval = sqrt(tmp2);
        }
      | M_CEIL Exp
        { if ($2.sig == 1)
            tmp2 = $2.ival;
          else if ($2.sig == 2)
            tmp2 = $2.dval;
          $$.sig = 1;
          $$.ival = (int)ceil(tmp2);
        }
      | M_FLOOR Exp
        { if ($2.sig == 1)
            tmp2 = $2.ival;
          else if ($2.sig == 2)
            tmp2 = $2.dval;
          $$.sig = 1;
          $$.ival = (int)floor(tmp2);
        }
      | OP_SUB Exp %prec USUB
        { if ($2.sig == 1)
            $2.ival = -$2.ival;
          else if ($2.sig == 2)
            $2.dval = -$2.dval;
          $$ = $2;
        }
      | OP_ADD Exp %prec UADD
        { $$ = $2; }
      ;

Primary : VARIABLE
          { tmp = findVAR($1,1);
            if (tmp != -1) {
              $$.sig = VARIABLES[tmp].attr.sig;
              $$.ival = VARIABLES[tmp].attr.ival;
              $$.dval = VARIABLES[tmp].attr.dval;

              if ($$.sig == 1)
                printf("  [%s] = %d\n", $1, $$.ival);
              else if ($$.sig == 2)
                printf("  [%s] = %lf\n", $1, $$.dval);
            } else {
              $$.sig = 1;
              $$.ival = 0;
            }
          }
        | VARIABLE OP_EQU Exp
          { tmp = findVAR($1,0);
            if (tmp == -1)
              tmp = saveVAR($1,&$3);
            if (tmp != -1) {
              VARIABLES[tmp].attr.sig = $3.sig;
              VARIABLES[tmp].attr.ival = $3.ival;
              VARIABLES[tmp].attr.dval = $3.dval;

              $$.sig = $3.sig;
              $$.ival = $3.ival;
              $$.dval = $3.dval;

              if ($$.sig == 1)
                printf("  let [%s] = %d\n", $1, $$.ival);
              else if ($$.sig == 2)
                printf("  let [%s] = %lf\n", $1, $$.dval);
            }
          }
        | Constant
          { $$ = $1; }
        | LPAR Exp RPAR
          { $$ = $2; }
        ;

Constant: INT_VAL
          { $$.sig=1;
            $$.ival = $1.ival;
          }
        | DBL_VAL
          { $$.sig=2;
            $$.dval = $1.dval;
          }
        | CONST_PI
          { $$.sig=2;
            $$.dval = 3.14159265358979323846264338327950288419716939937510;
          }
        | CONST_E
          { $$.sig=2;
            $$.dval = 2.71828182845904523536028747135266249775724709369995;
          }

%%
/* Functions */

#include "lexer.c"

void yyerror(char *s){
  fprintf(stdout,"ERROR: %s [%s]\n",s,yytext);
}

void xxerror(char *s1,char *s2){
  fprintf(stdout,"ERROR: %s %s\n",s1,s2);
}


int findVAR(char *name,int errorMissing) {
  int i;
  for(i=0; i < var_ix; i++) {
    if (strcmp(name,VARIABLES[i].name) == 0)
      return i;
  }
  if (errorMissing)
    xxerror("Variable was not defined: ",name);
  return -1;
}

int saveVAR(char *name, ATRV *attr) {
  if (var_ix == 30) {
    xxerror("Variable can not be defined (out of space): ", name);
    return -1;
  }
  VARIABLES[var_ix].name = name;
  memcpy(&VARIABLES[var_ix].attr, attr, sizeof(ATRV));
  var_ix++;
  return var_ix -1;
}

/**
 * Recursively compute a factorial.
 */
int fact(int x) {
  if (x == 0)
    return 1;
  else if (x > 0)
    return x * fact(x-1);
  else
    return -1;
}

/* radix should be either 2 or 8. */
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

  if (sign)
    result[--j] = '-';
  return (char*)(result + j);
}

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

void printResult(ATRV *attr) {
  int a = attr->ival;
  double b = attr->dval;
  printf("Result was saved to variable R.\n");
  if (attr->sig == 1) {
    printf("   %d\n  x%X\n",a,a);
    printf("   %s\n", intToRadix(a,8));
    printf("   %s\n", intToRadix(a,2));
  }
  else if (attr->sig == 2) {
    printf("  %lf\n",b);
    printf(" x%s\n",doubleToRadix(b,16));
    printf(" o%s\n",doubleToRadix(b,8));
    printf(" b%s\n",doubleToRadix(b,2));
  }
}

void printHelp() {
  printf("This is a command-line calculator. It supports the following commands:\n" \
         "  h, help - this command.\n" \
         "  q, quit - quit the calculator.\n\n" \
         "There are supported numbers of various format:\n" \
         "  - integral\n" \
         "  - decimal\n\n" \
         "Both number formats can be expressed in various radixes, such as:\n" \
         "  - in decadic radix (`1`, `2.3`, `3`, `-10.554`, ...)\n" \
         "  - in hexadecimal radix (`x4`, `xA.f3`, `x5FC`, `-x5B.12`, ...)\n" \
         "  - in octal radix (`o3`, `o7`, `o43.243`, `-o23.05`, ...)\n" \
         "  - in binary radix (`b1`, `b10101.10101`, `-b01101.11`, ...)\n\n" \
         "Results are printed in:\n" \
         "  - decadic, octal, hexadecimal and binary radix\n\n" \
         "The following operators are supported:\n" \
         "  + - addition, or unary plus (5+5=6)\n" \
         "  - - substraction, or unary minus (3-2=1)\n" \
         "  * - multiplication (5*6=30)\n" \
         "  / - divide (4/2=2)\n" \
         "  %, mod - divide remainder (6%3=0)\n" \
         "  ^ - power (2^3=8)\n" \
         "  ! - factorial (3!=6)\n\n" \
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
         "This calculator also supports variables that store values, e.g.:\n" \
         "  x = 5\n" \
         "  var = 4.23 * x\n" \
         "  (-2 * var + x)/ (4*var^2)\nEvery result is stored to the R variable.\n");
}

int main(int ac,char *av[]){
  extern FILE *yyin;
  int i;
  if(ac>1){
    yyin=fopen(av[1],"r");
    if(yyin==NULL){
      yyin=stdin;
    }
  }
  var_ix = 0;
  
  printf("kCalculator 0.11b\n(c) Copyright 2010,P.Jakubco\n\n(Type 'help' for help.)\n");

  while(!quit) {
    printf(">");
    (void)yyparse();
  }
  return(0);
}

