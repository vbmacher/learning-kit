%{
/*
 * parser.y
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

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <math.h>

#include <lecalc.h>
#include "parser.h"
#include "lexer.h"

static Tree *make_operator (Tree *left, int oper, Tree *right);
static Tree *make_value (double n);
static Tree *make_variable(char* var);
int yyerror(yyscan_t *scanner, char *s);

int quit = 0;
int use = 0;
int help = 0;
double result = 0.0;
char *filename=  NULL;

%}

%pure-parser
%defines
%lex-param {yyscan_t *scanner}
%parse-param {void *scanner}

%union {
  char*  name;
  double value;
  Tree*  tree;
}

/* terminal symbols */
%token <name> VARIABLE FILENAME
%token <value> INT_VAL DBL_VAL

%token LPAR RPAR OP_EQU HELP USE VARS
%token QUIT EOL

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
       | Exp { 
            if ($1 != NULL) {
              result = evalTree($1);
              if (!isEvalError()) {
                Tree *tree = make_value(result); 
                saveVAR("R", tree);
              } else 
                xxerror("Evaluation"," error");
            }
         }
       | HELP         { help = 1; }
       | USE FILENAME { use = 1; filename = $2; }
       | VARS {}
       | VARS VARIABLE {}
       | QUIT         { quit = 1;    }
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

Primary : VARIABLE { $$ = make_variable($1); }
        | VARIABLE OP_EQU Exp {
            saveVAR($1,$3);
            $$ = NULL; // what impact has this?
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


