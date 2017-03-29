%{
/*
 * lexer.l
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

#include <math.h>
#include <string.h>
#include <lecalc.h>
#include "parser.h"

//option reentrant noyywrap never-interactive nounistd yylineno
// %option header-file="lexer.h"

%}

%option reentrant noyywrap yylineno
%option bison-bridge

ID     [a-zA-Z\_\@][a-zA-Z0-9\-\_\@]*
NUM    [0-9]+
HEXNUM [0-9a-fA-F]+
OCTNUM [0-7]+
BINNUM [0-1]+
FILENAME "'"[^'\n\r]+"'"
SPACE [\ \r\t]*
%%


\n  { return EOF;} 
SPACE { }
" " { }
"+" { return OP_ADD; }
"-" { return OP_SUB; }
"*" { return OP_MUL; }
"/" { return OP_DIV; }
"!" { return OP_FACT; }
"^" { return OP_POW; }
"%" { return OP_MOD; }
"mod" { return OP_MOD; }
"=" { return OP_EQU; }

"(" { return LPAR; }
")" { return RPAR; }

"sin" { return M_SIN; }
"cos" { return M_COS; }
"tan" { return M_TAN; }
"cotan" { return M_COTAN; }
"log" { return M_LOG; }
"log2" { return M_LOG2; }
"ln"  { return M_LOGE; }
"sqrt" { return M_SQRT; }
"ceil" { return M_CEIL; }
"floor" { return M_FLOOR; }

"PI" { return CONST_PI; }
"E"  { return CONST_E; }

"use" { return USE; }

"q"    { return QUIT; }
"quit" { return QUIT; }
"exit" { return QUIT; }

"h"    { return HELP; }
"help" { return HELP; }

"vars" { return VARS; }

{FILENAME} { int len = strlen(yytext)-2;
             yylval->name = (char *)malloc(len+1);
             memcpy(yylval->name, yytext+1,len);
             yylval->name[len] = 0;
             return FILENAME;
           }
{ID}     { yylval->name = strdup(yytext);
           return VARIABLE; }
{NUM}    { int i;
           sscanf(yytext,"%d",&i);
           yylval->value = (double)i;
           return INT_VAL;
         }
{HEXNUM}"x" {
  int i;
  sscanf(yytext,"%x",&i);
  yylval->value = (double)i;
  return INT_VAL;
}

{OCTNUM}"o" {
  int i;
  yylval->value = 0;
  for (i = 0; yytext[i] != 'o'; i++)
    yylval->value = yylval->value * 8 + yytext[i] - '0';
  return INT_VAL;
}

{BINNUM}"b" {
  int i;
  yylval->value = 0;
  for (i = 0; yytext[i] != 'b'; i++)
    yylval->value = yylval->value * 2 + yytext[i] - '0';
  return INT_VAL;
}

{NUM}"."{NUM} {
  sscanf(yytext,"%lf", &yylval->value);
  return DBL_VAL;
}

{HEXNUM}"."{HEXNUM}"x" {
  yylval->value = 0;
        
  int i;
  char xx = '0';
  int num = 0;
  for (i = 0; yytext[i] != '.'; i++) {
    if (yytext[i] >= 'a' && yytext[i] <= 'f') {
      xx = 'a';
      num = 10;
    } else if (yytext[i] >= 'A' && yytext[i] <= 'F') {
      xx = 'A';
      num = 10;
    }
    yylval->value = yylval->value * 16 + yytext[i] - xx + num;
  }
  int j = i;
  num = 0;
  xx = '0';
  for (i=j+1; yytext[i] != 'x'; i++) {
    if (yytext[i] >= 'a' && yytext[i] <= 'f') {
      xx = 'a';
      num = 10;
    } else if (yytext[i] >= 'A' && yytext[i] <= 'F') {
      xx = 'A';
      num = 10;
    }
    yylval->value += (yytext[i] - xx + num)/pow(16,i-j);
  }
  return DBL_VAL;
}

{BINNUM}"."{BINNUM}"b" {
  yylval->value = 0;

  int i;
  for (i = 0; yytext[i] != '.'; i++)
    yylval->value = yylval->value * 2 + yytext[i] - '0';

  int j = i;
  for (i=j+1; yytext[i] != 'b'; i++)
    yylval->value += (yytext[i] - '0')/pow(2,i-j);
  return DBL_VAL;
}

{OCTNUM}"."{OCTNUM}"o" {
  yylval->value = 0;
           
  int i;
  for (i = 0; yytext[i] != '.'; i++)
    yylval->value = yylval->value * 8 + yytext[i] - '0';

  int j = i;
  for (i=j+1; yytext[i] != 'o'; i++)
    yylval->value += (yytext[i] - '0')/pow(8,i-j);
  return DBL_VAL;
}

. { fprintf(stderr,"ERROR: Unknown character '%s'\n", yytext); }
%%


int yyerror(yyscan_t *scanner, char *s){
   // This weird variable is used in a macro by flex
   struct yyguts_t * yyg = (struct yyguts_t*)scanner;
   fprintf(stderr,"ERROR: %s [%s]\n",s,yytext);
}

