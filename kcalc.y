%{
  int yyparse(void), yylex(void);

  typedef struct {
    unsigned char sig;
    int ival;
    double dval;
  } ATRV;
%}

%union {
  char* name;
  ATRV attr;
}

/* terminal symbols */
%token <name> VARIABLE
%token <attr> INT_VAL
%token <attr> DBL_VAL

%token LPAR RPAR OP_EQU
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

%{

struct { char *name; ATRV attr;} VARIABLES[30];
int var_ix = 0;  // index to new variable

int tmp, ttmp;
double tmp2,ttmp2;

#include <alloc.h>
#include <stdio.h>
#include <string.h>
#include <math.h>


#include "lexyy.cpp"

static int quit = 0;
%}

%%
/* Rules */

Subor  : 
       | Exp 
         { printResult(&$1); }
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
          if ($$.dval == (double)((int)$$.dval)) {
            $$.sig = 1;
            $$.ival = (int)(tmp2+ttmp2);
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
          if ($$.dval == (double)((int)$$.dval)) {
            $$.sig = 1;
            $$.ival = (int)(tmp2-ttmp2);
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
          if ($$.dval == (double)((int)$$.dval)) {
            $$.sig = 1;
            $$.ival = (int)tmp2*ttmp2;
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
          $$.dval = tmp2 / ttmp2;
          if ($$.dval == (double)((int)$$.dval)) {
            $$.sig = 1;
            $$.ival = (int)tmp2/ttmp2;
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
                printf("[%s] = %d\n", $1, $$.ival);
              else if ($$.sig == 2)
                printf("[%s] = %lf\n", $1, $$.dval);
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
                printf("let [%s] = %d\n", $1, $$.ival);
              else if ($$.sig == 2)
                printf("let [%s] = %lf\n", $1, $$.dval);
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

void yyerror(char *s){
  fprintf(stdout,"ERROR: %s [%s]\n",s,yytext);
}

void xxerror(char *s1,char *s2){
  fprintf(stdout,"ERROR: %s %s\n",s1,s2);
}


void initVAR(){
  var_ix = 0;
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

void printResult(ATRV *attr) {
  int a = attr->ival;
  double b = attr->dval;
  if (attr->sig == 1)
    printf("\n\t %d\n\tx%X\n",a,a);
  else if (attr->sig == 2)
    printf("\n\t%lf\n",b);
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
  initVAR();
  
  while(!quit) {
    printf("\n>");
    (void)yyparse();
  }
  return(0);
}

