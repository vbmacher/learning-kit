%{
/*
 * kcalc.l
 *
 * KEEP IT SIMPLE, STUPID
 * some things just: YOU AREN'T GONNA NEED IT
 *
 * Copyright (C) 2010 Peter Jakubco <pjakubco at gmail.com>
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

#include <string.h>

int yywrap(void);
extern YYSTYPE yylval;

%}


ID     [a-zA-Z]
NUM    [0-9]+
HEXNUM "x"[0-9a-fA-F]+
OCTNUM "o"[0-7]+
BINNUM "b"[0-1]+

%%


\n  { return 0; }
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

"q"    { yywrap(); return QUIT; }
"quit" { yywrap(); return QUIT; }
"exit" { yywrap(); return QUIT; }

"h"    { return HELP; }
"help" { return HELP; }

{ID}+    { yylval.name = strdup(yytext);
           return VARIABLE; }
{NUM}    { sscanf(yytext,"%d",&yylval.attr.ival);
           yylval.attr.sig = 1;
           return INT_VAL;
         }
{HEXNUM} { sscanf(yytext+1,"%x",&yylval.attr.ival);
           yylval.attr.sig = 1;
           return INT_VAL;
         }
{OCTNUM} { int i;
           for (i = yylval.attr.ival = 0; yytext[i+1] != 0; i++)
             yylval.attr.ival = yylval.attr.ival * 8 + yytext[i+1] - '0';
           yylval.attr.sig = 1;
           return INT_VAL;
         }
{BINNUM} { int i;
           for (i = yylval.attr.ival = 0; yytext[i+1] != 0; i++)
             yylval.attr.ival = yylval.attr.ival * 2 + yytext[i+1] - '0';
           yylval.attr.sig = 1;
           return INT_VAL;
         }

{NUM}"."{NUM} { sscanf(yytext,"%lf", &yylval.attr.dval);
                yylval.attr.sig = 2;
                return DBL_VAL;
              }

{HEXNUM}"."[0-9a-fA-F]+ {
           yylval.attr.dval = 0;
           yylval.attr.sig = 2;
           
           int i;
           char xx = '0';
           int num = 0;
           for (i = 1; yytext[i] != '.'; i++) {
             if (yytext[i] >= 'a' && yytext[i] <= 'f') {
               xx = 'a';
               num = 10;
             }
             else if (yytext[i] >= 'A' && yytext[i] <= 'F') {
               xx = 'A';
               num = 10;
             }
             yylval.attr.dval = yylval.attr.dval * 16 + yytext[i] - xx + num;
           }
           int j = i;
           num = 0;
           xx = '0';
           for (i=j+1; yytext[i] != 0; i++) {
             if (yytext[i] >= 'a' && yytext[i] <= 'f') {
               xx = 'a';
               num = 10;
             } else if (yytext[i] >= 'A' && yytext[i] <= 'F') {
               xx = 'A';
               num = 10;
             }
             yylval.attr.dval += (yytext[i] - xx + num)/pow(16,i-j);
           }
           return DBL_VAL;
         }

{BINNUM}"."[0-1]+ {
           yylval.attr.dval = 0;
           yylval.attr.sig = 2;
           
           int i;
           for (i = 1; yytext[i] != '.'; i++)
             yylval.attr.dval = yylval.attr.dval * 2 + yytext[i] - '0';

           int j = i;
           for (i=j+1; yytext[i] != 0; i++)
             yylval.attr.dval += (yytext[i] - '0')/pow(2,i-j);
           return DBL_VAL;
         }

{OCTNUM}"."[0-7]+ {
           yylval.attr.dval = 0;
           yylval.attr.sig = 2;
           
           int i;
           for (i = 1; yytext[i] != '.'; i++)
             yylval.attr.dval = yylval.attr.dval * 8 + yytext[i] - '0';

           int j = i;
           for (i=j+1; yytext[i] != 0; i++)
             yylval.attr.dval += (yytext[i] - '0')/pow(8,i-j);
           return DBL_VAL;
         }

%%

/**
 * End of input.
 */
int yywrap(void){
  return(1);
}

