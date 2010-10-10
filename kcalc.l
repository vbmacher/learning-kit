%{

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


%%

/**
 * End of input.
 */
int yywrap(void){
  return(1);
}

