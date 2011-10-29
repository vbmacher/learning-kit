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

#include "kcalc.h"

int yyparse(void), yylex(void);
static Tree *make_operator (Tree *left, int oper, Tree *right);
static Tree *make_value (double n);
static Tree *make_variable(char* var);
void yyerror(char *s);

static int quit = 0;
extern char*yytext;  
%}

%union {
  char*  name;
  double value;
  Tree*  tree;
}

/* terminal symbols */
%token <name> VARIABLE
%token <value> INT_VAL DBL_VAL

%token LPAR RPAR OP_EQU HELP
%token QUIT

%token M_SIN M_COS M_TAN M_COTAN M_LOG M_LOG2 M_LOGE M_SQRT M_CEIL M_FLOOR
%token CONST_PI CONST_E

/* priorities and associativity of operators */
%right OP_EQU
%left  OP_ADD OP_SUB
%left  OP_MUL OP_DIV
%left  OP_MOD
%right UADD USUB
%left  OP_POW
%left  OP_FACT

/* non-terminal symbols values */
%type <tree> Exp Primary
%type <tree> Constant

%%
/* Rules */

Start  : 
       | Exp 
         { 
            double dbl = evalTree($1); // TODO: check for evaluation error
            Tree *tree = make_value(dbl); 
            saveVAR("R", tree);
            printResult(dbl); 
         }
       | HELP
         { printHelp(); }
       | QUIT
         { quit = 1; }
       ;

Exp   : Primary        { $$ = $1;                               }
      | Exp OP_ADD Exp { $$ = make_operator($1, OP_ADD, $3);    }
      | Exp OP_SUB Exp { $$ = make_operator($1, OP_SUB, $3);    }
      | Exp OP_MUL Exp { $$ = make_operator($1, OP_MUL, $3);    }
      | Exp OP_DIV Exp { $$ = make_operator($1, OP_DIV, $3);    }
      | Exp OP_POW Exp { $$ = make_operator($1, OP_POW, $3);    }
      | Exp OP_MOD Exp { $$ = make_operator($1, OP_MOD, $3);    }
      | Exp OP_FACT    { $$ = make_operator($1, OP_FACT, NULL); }
      | M_SIN Exp      { $$ = make_operator(NULL, M_SIN, $2);   }
      | M_COS Exp      { $$ = make_operator(NULL, M_COS, $2);   }
      | M_TAN Exp      { $$ = make_operator(NULL, M_TAN, $2);   }
      | M_COTAN Exp    { $$ = make_operator(NULL, M_COTAN, $2); }
      | M_LOG Exp      { $$ = make_operator(NULL, M_LOG, $2);   }
      | M_LOG2 Exp     { $$ = make_operator(NULL, M_LOG2, $2);  }
      | M_LOGE Exp     { $$ = make_operator(NULL, M_LOGE, $2);  }
      | M_SQRT Exp     { $$ = make_operator(NULL, M_SQRT, $2);  }
      | M_CEIL Exp     { $$ = make_operator(NULL, M_CEIL, $2);  }
      | M_FLOOR Exp    { $$ = make_operator(NULL, M_FLOOR, $2); }
      | OP_SUB Exp %prec USUB
                       { $$ = make_operator(NULL, OP_SUB, $2);  }
      | OP_ADD Exp %prec UADD
                       { $$ = make_operator(NULL, OP_ADD, $2);  }
      ;

Primary : VARIABLE { $$ = make_variable(strdup($1)); }
        | VARIABLE OP_EQU Exp {
            saveVAR($1,$3);
            $$ = $3;
          }
        | Constant      { $$ = $1; }
        | LPAR Exp RPAR { $$ = $2; }
        ;

Constant: INT_VAL  { $$ = make_value($1); }
        | DBL_VAL  { $$ = make_value($1); }
        | CONST_PI { $$ = make_value(3.14159265358979323846264338327950288419716939937510); }
        | CONST_E  { $$ = make_value(2.71828182845904523536028747135266249775724709369995); }
        ;

%%

void yyerror(char *s){
  fprintf(stdout,"ERROR: %s [%s]\n",s,yytext);
}

/* Borrowed from: http://www.cs.man.ac.uk/~pjj/cs212/ho/node8.html */

static Tree *make_operator (Tree *left, int oper, Tree *right) {
  Tree *result= (Tree*) malloc (sizeof(Tree));
  result->nodetype= operator_node;
  result->body.an_operator.left= left;
  result->body.an_operator.oper= oper;
  result->body.an_operator.right= right;
  return result;
}

static Tree *make_value (double n) {
  Tree *result = (Tree*) malloc (sizeof(Tree));
  result->nodetype = value_node;
  result->body.value = n;
  return result;
}

static Tree *make_variable (char* var) {
  Tree *result= (Tree*) malloc (sizeof(Tree));
  result->nodetype= variable_node;
  result->body.variable= var;
  return result;
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
  
  printf("kCalculator 0.11b\n(c) Copyright 2010,P.Jakubco\n\n(Type 'help' for help.)\n");

  while(!quit) {
    printf(">");
    (void)yyparse();
  }
  return(0);
}

