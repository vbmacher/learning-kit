#ifndef lint
static char yysccsid[] = "@(#)yaccpar	1.9 (Berkeley) 02/21/93";
#endif
#define YYBYACC 1
#define YYMAJOR 1
#define YYMINOR 9
#define yyclearin (yychar=(-1))
#define yyerrok (yyerrflag=0)
#define YYRECOVERING (yyerrflag!=0)
#define YYPREFIX "yy"
#line 2 "kcalc.y"
/*
 * kcalc.y
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
int var_ix = 0;  /* index to new variable*/

int tmp, ttmp;
double tmp2,ttmp2;
static int quit = 0;
  
#line 47 "kcalc.y"
typedef union {
  char* name;
  ATRV attr;
} YYSTYPE;
#line 61 "kcalc.tab.c"
#define VARIABLE 257
#define INT_VAL 258
#define DBL_VAL 259
#define LPAR 260
#define RPAR 261
#define OP_EQU 262
#define HELP 263
#define QUIT 264
#define M_SIN 265
#define M_COS 266
#define M_TAN 267
#define M_COTAN 268
#define M_LOG 269
#define M_LOG2 270
#define M_LOGE 271
#define M_SQRT 272
#define M_CEIL 273
#define M_FLOOR 274
#define CONST_PI 275
#define CONST_E 276
#define OP_ADD 277
#define OP_SUB 278
#define OP_MUL 279
#define OP_DIV 280
#define OP_MOD 281
#define UADD 282
#define USUB 283
#define OP_POW 284
#define OP_FACT 285
#define YYERRCODE 256
short yylhs[] = {                                        -1,
    0,    0,    0,    0,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    2,    2,    2,    2,    3,    3,
    3,    3,
};
short yylen[] = {                                         2,
    0,    1,    1,    1,    1,    3,    3,    3,    3,    3,
    3,    2,    2,    2,    2,    2,    2,    2,    2,    2,
    2,    2,    2,    2,    1,    3,    1,    3,    1,    1,
    1,    1,
};
short yydefred[] = {                                      0,
    0,   29,   30,    0,    3,    4,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   31,   32,    0,    0,
    0,    0,    5,   27,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   12,    0,   28,    0,    0,    0,
    0,    0,    0,
};
short yydgoto[] = {                                      21,
   22,   23,   24,
};
short yysindex[] = {                                   -152,
 -241,    0,    0, -130,    0,    0, -130, -130, -130, -130,
 -130, -130, -130, -130, -130, -130,    0,    0, -130, -130,
    0, -252,    0,    0, -130, -261, -252, -252, -252, -252,
 -252, -252, -252, -252, -252, -252, -270, -270, -130, -130,
 -130, -130, -130, -130,    0, -252,    0, -240, -240, -247,
 -247, -270, -255,
};
short yyrindex[] = {                                     35,
    1,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   42,    0,    0,    0,    0,    2,    3,    4,    5,
    6,    7,    8,    9,   11,   12,   22,   31,    0,    0,
    0,    0,    0,    0,    0,   13,    0,   63,   65,   43,
   57,   36,   10,
};
short yygindex[] = {                                      0,
   60,    0,    0,
};
#define YYTABLESIZE 343
short yytable[] = {                                      47,
   25,   13,   14,   15,   16,   17,   18,   19,   20,   10,
   21,   22,   26,   44,   45,   39,   40,   41,   42,   43,
   25,   24,   44,   45,   39,   40,   41,   42,   43,   45,
   23,   44,   45,   43,    1,   11,   44,   45,   41,   42,
   43,    2,    8,   44,   45,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    9,    0,    0,    0,
    0,    0,    6,   26,    7,    0,   27,   28,   29,   30,
   31,   32,   33,   34,   35,   36,    0,    0,   37,   38,
    0,    0,    0,    0,   46,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   48,   49,
   50,   51,   52,   53,    1,    2,    3,    4,    0,    0,
    5,    6,    7,    8,    9,   10,   11,   12,   13,   14,
   15,   16,   17,   18,   19,   20,    1,    2,    3,    4,
    0,    0,    0,    0,    7,    8,    9,   10,   11,   12,
   13,   14,   15,   16,   17,   18,   19,   20,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   25,   13,   14,   15,   16,   17,   18,   19,   20,
   10,   21,   22,   26,    0,    0,    0,   25,   25,   25,
   25,   25,   24,    0,   25,   25,   10,   10,   10,   10,
   10,   23,    0,   10,    0,    0,   11,    0,   24,   24,
   24,   24,   24,    8,    0,    0,    0,   23,   23,   23,
   23,   23,   11,   11,   11,   11,   11,    9,    0,    8,
    8,    8,    8,    6,    0,    7,    0,    0,    0,    0,
    0,    0,    0,    9,    9,    9,    9,    0,    0,    6,
    6,    7,    7,
};
short yycheck[] = {                                     261,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  284,  285,  277,  278,  279,  280,  281,
  262,    0,  284,  285,  277,  278,  279,  280,  281,  285,
    0,  284,  285,  281,    0,    0,  284,  285,  279,  280,
  281,    0,    0,  284,  285,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,    0,   -1,   -1,   -1,
   -1,   -1,    0,    4,    0,   -1,    7,    8,    9,   10,
   11,   12,   13,   14,   15,   16,   -1,   -1,   19,   20,
   -1,   -1,   -1,   -1,   25,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   39,   40,
   41,   42,   43,   44,  257,  258,  259,  260,   -1,   -1,
  263,  264,  265,  266,  267,  268,  269,  270,  271,  272,
  273,  274,  275,  276,  277,  278,  257,  258,  259,  260,
   -1,   -1,   -1,   -1,  265,  266,  267,  268,  269,  270,
  271,  272,  273,  274,  275,  276,  277,  278,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  261,  261,  261,  261,  261,  261,  261,  261,  261,
  261,  261,  261,  261,   -1,   -1,   -1,  277,  278,  279,
  280,  281,  261,   -1,  284,  285,  277,  278,  279,  280,
  281,  261,   -1,  284,   -1,   -1,  261,   -1,  277,  278,
  279,  280,  281,  261,   -1,   -1,   -1,  277,  278,  279,
  280,  281,  277,  278,  279,  280,  281,  261,   -1,  277,
  278,  279,  280,  261,   -1,  261,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  277,  278,  279,  280,   -1,   -1,  277,
  278,  277,  278,
};
#define YYFINAL 21
#ifndef YYDEBUG
#define YYDEBUG 0
#endif
#define YYMAXTOKEN 285
#if YYDEBUG
char *yyname[] = {
"end-of-file",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,"VARIABLE","INT_VAL","DBL_VAL",
"LPAR","RPAR","OP_EQU","HELP","QUIT","M_SIN","M_COS","M_TAN","M_COTAN","M_LOG",
"M_LOG2","M_LOGE","M_SQRT","M_CEIL","M_FLOOR","CONST_PI","CONST_E","OP_ADD",
"OP_SUB","OP_MUL","OP_DIV","OP_MOD","UADD","USUB","OP_POW","OP_FACT",
};
char *yyrule[] = {
"$accept : Subor",
"Subor :",
"Subor : Exp",
"Subor : HELP",
"Subor : QUIT",
"Exp : Primary",
"Exp : Exp OP_ADD Exp",
"Exp : Exp OP_SUB Exp",
"Exp : Exp OP_MUL Exp",
"Exp : Exp OP_DIV Exp",
"Exp : Exp OP_POW Exp",
"Exp : Exp OP_MOD Exp",
"Exp : Exp OP_FACT",
"Exp : M_SIN Exp",
"Exp : M_COS Exp",
"Exp : M_TAN Exp",
"Exp : M_COTAN Exp",
"Exp : M_LOG Exp",
"Exp : M_LOG2 Exp",
"Exp : M_LOGE Exp",
"Exp : M_SQRT Exp",
"Exp : M_CEIL Exp",
"Exp : M_FLOOR Exp",
"Exp : OP_SUB Exp",
"Exp : OP_ADD Exp",
"Primary : VARIABLE",
"Primary : VARIABLE OP_EQU Exp",
"Primary : Constant",
"Primary : LPAR Exp RPAR",
"Constant : INT_VAL",
"Constant : DBL_VAL",
"Constant : CONST_PI",
"Constant : CONST_E",
};
#endif
#ifdef YYSTACKSIZE
#undef YYMAXDEPTH
#define YYMAXDEPTH YYSTACKSIZE
#else
#ifdef YYMAXDEPTH
#define YYSTACKSIZE YYMAXDEPTH
#else
#define YYSTACKSIZE 500
#define YYMAXDEPTH 500
#endif
#endif
int yydebug;
int yynerrs;
int yyerrflag;
int yychar;
short *yyssp;
YYSTYPE *yyvsp;
YYSTYPE yyval;
YYSTYPE yylval;
short yyss[YYSTACKSIZE];
YYSTYPE yyvs[YYSTACKSIZE];
#define yystacksize YYSTACKSIZE
#line 342 "kcalc.y"
/* Functions */

#include "lexyy.c"

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

void printResult(ATRV *attr) {
  int a = attr->ival;
  double b = attr->dval;
  if (attr->sig == 1)
    printf("   %d\n  x%X\n",a,a);
  else if (attr->sig == 2)
    printf("  %lf\n",b);
}

void printHelp() {
  printf("This is a command-line calculator. It supports the following commands:\n" \
         "  h, help - this command.\n" \
         "  q, quit - quit the calculator.\n\n" \
         "There are supported numbers of various format:\n" \
         "  - integral\n" \
         "      - in classical decadic radix (1, 2, 3, -10, ...)\n" \
         "      - in hexadecimal radix (x4, xA, x5FC, -x5B, ...)\n" \
         "      - in octal radix (o3, o7, o43, -o23, ...)\n" \
         "      - in binary radix (b1, b10101, -b01101, ...)\n" \
         "  - decimal (only in decadic radix, like 1.33, 4.53434, -12.40644, ...)\n\n" \
         "Results are printed in:\n" \
         "  - decadic and hexadecimal radix (for integral numbers)\n" \
         "  - only in decadic radix (for decimal numbers)\n\n" \
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
         "  (-2 * var + x)/ (4*var^2)\n");
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

#line 404 "kcalc.tab.c"
#define YYABORT goto yyabort
#define YYREJECT goto yyabort
#define YYACCEPT goto yyaccept
#define YYERROR goto yyerrlab
int
yyparse()
{
    register int yym, yyn, yystate;
#if YYDEBUG
    register char *yys;
    extern char *getenv();

    if (yys = getenv("YYDEBUG"))
    {
        yyn = *yys;
        if (yyn >= '0' && yyn <= '9')
            yydebug = yyn - '0';
    }
#endif

    yynerrs = 0;
    yyerrflag = 0;
    yychar = (-1);

    yyssp = yyss;
    yyvsp = yyvs;
    *yyssp = yystate = 0;

yyloop:
    if (yyn = yydefred[yystate]) goto yyreduce;
    if (yychar < 0)
    {
        if ((yychar = yylex()) < 0) yychar = 0;
#if YYDEBUG
        if (yydebug)
        {
            yys = 0;
            if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
            if (!yys) yys = "illegal-symbol";
            printf("%sdebug: state %d, reading %d (%s)\n",
                    YYPREFIX, yystate, yychar, yys);
        }
#endif
    }
    if ((yyn = yysindex[yystate]) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
    {
#if YYDEBUG
        if (yydebug)
            printf("%sdebug: state %d, shifting to state %d\n",
                    YYPREFIX, yystate, yytable[yyn]);
#endif
        if (yyssp >= yyss + yystacksize - 1)
        {
            goto yyoverflow;
        }
        *++yyssp = yystate = yytable[yyn];
        *++yyvsp = yylval;
        yychar = (-1);
        if (yyerrflag > 0)  --yyerrflag;
        goto yyloop;
    }
    if ((yyn = yyrindex[yystate]) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
    {
        yyn = yytable[yyn];
        goto yyreduce;
    }
    if (yyerrflag) goto yyinrecovery;
#ifdef lint
    goto yynewerror;
#endif
yynewerror:
    yyerror("syntax error");
#ifdef lint
    goto yyerrlab;
#endif
yyerrlab:
    ++yynerrs;
yyinrecovery:
    if (yyerrflag < 3)
    {
        yyerrflag = 3;
        for (;;)
        {
            if ((yyn = yysindex[*yyssp]) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
#if YYDEBUG
                if (yydebug)
                    printf("%sdebug: state %d, error recovery shifting\
 to state %d\n", YYPREFIX, *yyssp, yytable[yyn]);
#endif
                if (yyssp >= yyss + yystacksize - 1)
                {
                    goto yyoverflow;
                }
                *++yyssp = yystate = yytable[yyn];
                *++yyvsp = yylval;
                goto yyloop;
            }
            else
            {
#if YYDEBUG
                if (yydebug)
                    printf("%sdebug: error recovery discarding state %d\n",
                            YYPREFIX, *yyssp);
#endif
                if (yyssp <= yyss) goto yyabort;
                --yyssp;
                --yyvsp;
            }
        }
    }
    else
    {
        if (yychar == 0) goto yyabort;
#if YYDEBUG
        if (yydebug)
        {
            yys = 0;
            if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
            if (!yys) yys = "illegal-symbol";
            printf("%sdebug: state %d, error recovery discards token %d (%s)\n",
                    YYPREFIX, yystate, yychar, yys);
        }
#endif
        yychar = (-1);
        goto yyloop;
    }
yyreduce:
#if YYDEBUG
    if (yydebug)
        printf("%sdebug: state %d, reducing by rule %d (%s)\n",
                YYPREFIX, yystate, yyn, yyrule[yyn]);
#endif
    yym = yylen[yyn];
    yyval = yyvsp[1-yym];
    switch (yyn)
    {
case 2:
#line 81 "kcalc.y"
{ printResult(&yyvsp[0].attr); }
break;
case 3:
#line 83 "kcalc.y"
{ printHelp(); }
break;
case 4:
#line 85 "kcalc.y"
{ quit = 1; }
break;
case 5:
#line 89 "kcalc.y"
{ yyval.attr = yyvsp[0].attr; }
break;
case 6:
#line 91 "kcalc.y"
{ if (yyvsp[-2].attr.sig == 1)
            tmp2 = yyvsp[-2].attr.ival;
          else if (yyvsp[-2].attr.sig == 2)
            tmp2 = yyvsp[-2].attr.dval;
          if (yyvsp[0].attr.sig == 1)
            ttmp2 = yyvsp[0].attr.ival;
          else if (yyvsp[0].attr.sig == 2)
            ttmp2 = yyvsp[0].attr.dval;
          yyval.attr.sig = 2;
          yyval.attr.dval = tmp2 + ttmp2;
          if (yyval.attr.dval == (double)((int)yyval.attr.dval)) {
            yyval.attr.sig = 1;
            yyval.attr.ival = (int)(tmp2+ttmp2);
          }
        }
break;
case 7:
#line 107 "kcalc.y"
{ if (yyvsp[-2].attr.sig == 1)
            tmp2 = yyvsp[-2].attr.ival;
          else if (yyvsp[-2].attr.sig == 2)
            tmp2 = yyvsp[-2].attr.dval;
          if (yyvsp[0].attr.sig == 1)
            ttmp2 = yyvsp[0].attr.ival;
          else if (yyvsp[0].attr.sig == 2)
            ttmp2 = yyvsp[0].attr.dval;
          yyval.attr.sig = 2;
          yyval.attr.dval = tmp2 - ttmp2;
          if (yyval.attr.dval == (double)((int)yyval.attr.dval)) {
            yyval.attr.sig = 1;
            yyval.attr.ival = (int)(tmp2-ttmp2);
          }
        }
break;
case 8:
#line 123 "kcalc.y"
{ if (yyvsp[-2].attr.sig == 1)
            tmp2 = yyvsp[-2].attr.ival;
          else if (yyvsp[-2].attr.sig == 2)
            tmp2 = yyvsp[-2].attr.dval;
          if (yyvsp[0].attr.sig == 1)
            ttmp2 = yyvsp[0].attr.ival;
          else if (yyvsp[0].attr.sig == 2)
            ttmp2 = yyvsp[0].attr.dval;
          yyval.attr.sig = 2;
          yyval.attr.dval = tmp2 * ttmp2;
          if (yyval.attr.dval == (double)((int)yyval.attr.dval)) {
            yyval.attr.sig = 1;
            yyval.attr.ival = (int)tmp2*ttmp2;
          }
        }
break;
case 9:
#line 139 "kcalc.y"
{ if (yyvsp[-2].attr.sig == 1)
            tmp2 = yyvsp[-2].attr.ival;
          else if (yyvsp[-2].attr.sig == 2)
            tmp2 = yyvsp[-2].attr.dval;
          if (yyvsp[0].attr.sig == 1)
            ttmp2 = yyvsp[0].attr.ival;
          else if (yyvsp[0].attr.sig == 2)
            ttmp2 = yyvsp[0].attr.dval;
          yyval.attr.sig = 2;
          yyval.attr.dval = tmp2 / ttmp2;
          if (yyval.attr.dval == (double)((int)yyval.attr.dval)) {
            yyval.attr.sig = 1;
            yyval.attr.ival = (int)tmp2/ttmp2;
          }
        }
break;
case 10:
#line 155 "kcalc.y"
{ if (yyvsp[-2].attr.sig == 1)
            tmp2 = yyvsp[-2].attr.ival;
          else if (yyvsp[-2].attr.sig == 2)
            tmp2 = yyvsp[-2].attr.dval;
          if (yyvsp[0].attr.sig == 1)
            ttmp2 = yyvsp[0].attr.ival;
          else if (yyvsp[0].attr.sig == 2)
            ttmp2 = yyvsp[0].attr.dval;
          yyval.attr.sig = 2;
          yyval.attr.dval = pow(tmp2,ttmp2);
        }
break;
case 11:
#line 167 "kcalc.y"
{ if (yyvsp[-2].attr.sig == 1)
            tmp = yyvsp[-2].attr.ival;
          else if (yyvsp[-2].attr.sig == 2)
            tmp = (int)yyvsp[-2].attr.dval;
          if (yyvsp[0].attr.sig == 1)
            ttmp = yyvsp[0].attr.ival;
          else if (yyvsp[0].attr.sig == 2)
            ttmp = (int)yyvsp[0].attr.dval;
          yyval.attr.sig = 1;
          yyval.attr.ival = tmp % ttmp;
        }
break;
case 12:
#line 179 "kcalc.y"
{ if (yyvsp[-1].attr.sig == 1)
            tmp = yyvsp[-1].attr.ival;
          else if (yyvsp[-1].attr.sig == 2) {
            tmp = (int)yyvsp[-1].attr.dval;
            fprintf(stdout, "Warning: Using decimal value as integer\n");
          }
          yyval.attr.sig = 1;
          yyval.attr.ival = (int)fact(tmp);
        }
break;
case 13:
#line 189 "kcalc.y"
{ if (yyvsp[0].attr.sig == 1)
            tmp2 = yyvsp[0].attr.ival;
          else if (yyvsp[0].attr.sig == 2)
            tmp2 = yyvsp[0].attr.dval;
          yyval.attr.sig = 2;
          yyval.attr.dval = sin(tmp2);
        }
break;
case 14:
#line 197 "kcalc.y"
{ if (yyvsp[0].attr.sig == 1)
            tmp2 = yyvsp[0].attr.ival;
          else if (yyvsp[0].attr.sig == 2)
            tmp2 = yyvsp[0].attr.dval;
          yyval.attr.sig = 2;
          yyval.attr.dval = cos(tmp2);
        }
break;
case 15:
#line 205 "kcalc.y"
{ if (yyvsp[0].attr.sig == 1)
            tmp2 = yyvsp[0].attr.ival;
          else if (yyvsp[0].attr.sig == 2)
            tmp2 = yyvsp[0].attr.dval;
          yyval.attr.sig = 2;
          yyval.attr.dval = tan(tmp2);
        }
break;
case 16:
#line 213 "kcalc.y"
{ if (yyvsp[0].attr.sig == 1)
            tmp2 = yyvsp[0].attr.ival;
          else if (yyvsp[0].attr.sig == 2)
            tmp2 = yyvsp[0].attr.dval;
          yyval.attr.sig = 2;
          ttmp2 = tan(tmp2);
          if (ttmp2 == 0)
            yyval.attr.dval = 0;
          else
            yyval.attr.dval = 1/ttmp2;
        }
break;
case 17:
#line 225 "kcalc.y"
{ if (yyvsp[0].attr.sig == 1)
            tmp2 = yyvsp[0].attr.ival;
          else if (yyvsp[0].attr.sig == 2)
            tmp2 = yyvsp[0].attr.dval;
          yyval.attr.sig = 2;
          yyval.attr.dval = log10(tmp2);
        }
break;
case 18:
#line 233 "kcalc.y"
{ if (yyvsp[0].attr.sig == 1)
            tmp2 = yyvsp[0].attr.ival;
          else if (yyvsp[0].attr.sig == 2)
            tmp2 = yyvsp[0].attr.dval;
          yyval.attr.sig = 2;
          yyval.attr.dval = log(tmp2)/log(2);
        }
break;
case 19:
#line 241 "kcalc.y"
{ if (yyvsp[0].attr.sig == 1)
            tmp2 = yyvsp[0].attr.ival;
          else if (yyvsp[0].attr.sig == 2)
            tmp2 = yyvsp[0].attr.dval;
          yyval.attr.sig = 2;
          yyval.attr.dval = log(tmp2);
        }
break;
case 20:
#line 249 "kcalc.y"
{ if (yyvsp[0].attr.sig == 1)
            tmp2 = yyvsp[0].attr.ival;
          else if (yyvsp[0].attr.sig == 2)
            tmp2 = yyvsp[0].attr.dval;
          yyval.attr.sig = 2;
          yyval.attr.dval = sqrt(tmp2);
        }
break;
case 21:
#line 257 "kcalc.y"
{ if (yyvsp[0].attr.sig == 1)
            tmp2 = yyvsp[0].attr.ival;
          else if (yyvsp[0].attr.sig == 2)
            tmp2 = yyvsp[0].attr.dval;
          yyval.attr.sig = 1;
          yyval.attr.ival = (int)ceil(tmp2);
        }
break;
case 22:
#line 265 "kcalc.y"
{ if (yyvsp[0].attr.sig == 1)
            tmp2 = yyvsp[0].attr.ival;
          else if (yyvsp[0].attr.sig == 2)
            tmp2 = yyvsp[0].attr.dval;
          yyval.attr.sig = 1;
          yyval.attr.ival = (int)floor(tmp2);
        }
break;
case 23:
#line 273 "kcalc.y"
{ if (yyvsp[0].attr.sig == 1)
            yyvsp[0].attr.ival = -yyvsp[0].attr.ival;
          else if (yyvsp[0].attr.sig == 2)
            yyvsp[0].attr.dval = -yyvsp[0].attr.dval;
          yyval.attr = yyvsp[0].attr;
        }
break;
case 24:
#line 280 "kcalc.y"
{ yyval.attr = yyvsp[0].attr; }
break;
case 25:
#line 284 "kcalc.y"
{ tmp = findVAR(yyvsp[0].name,1);
            if (tmp != -1) {
              yyval.attr.sig = VARIABLES[tmp].attr.sig;
              yyval.attr.ival = VARIABLES[tmp].attr.ival;
              yyval.attr.dval = VARIABLES[tmp].attr.dval;

              if (yyval.attr.sig == 1)
                printf("  [%s] = %d\n", yyvsp[0].name, yyval.attr.ival);
              else if (yyval.attr.sig == 2)
                printf("  [%s] = %lf\n", yyvsp[0].name, yyval.attr.dval);
            } else {
              yyval.attr.sig = 1;
              yyval.attr.ival = 0;
            }
          }
break;
case 26:
#line 300 "kcalc.y"
{ tmp = findVAR(yyvsp[-2].name,0);
            if (tmp == -1)
              tmp = saveVAR(yyvsp[-2].name,&yyvsp[0].attr);
            if (tmp != -1) {
              VARIABLES[tmp].attr.sig = yyvsp[0].attr.sig;
              VARIABLES[tmp].attr.ival = yyvsp[0].attr.ival;
              VARIABLES[tmp].attr.dval = yyvsp[0].attr.dval;

              yyval.attr.sig = yyvsp[0].attr.sig;
              yyval.attr.ival = yyvsp[0].attr.ival;
              yyval.attr.dval = yyvsp[0].attr.dval;

              if (yyval.attr.sig == 1)
                printf("  let [%s] = %d\n", yyvsp[-2].name, yyval.attr.ival);
              else if (yyval.attr.sig == 2)
                printf("  let [%s] = %lf\n", yyvsp[-2].name, yyval.attr.dval);
            }
          }
break;
case 27:
#line 319 "kcalc.y"
{ yyval.attr = yyvsp[0].attr; }
break;
case 28:
#line 321 "kcalc.y"
{ yyval.attr = yyvsp[-1].attr; }
break;
case 29:
#line 325 "kcalc.y"
{ yyval.attr.sig=1;
            yyval.attr.ival = yyvsp[0].attr.ival;
          }
break;
case 30:
#line 329 "kcalc.y"
{ yyval.attr.sig=2;
            yyval.attr.dval = yyvsp[0].attr.dval;
          }
break;
case 31:
#line 333 "kcalc.y"
{ yyval.attr.sig=2;
            yyval.attr.dval = 3.14159265358979323846264338327950288419716939937510;
          }
break;
case 32:
#line 337 "kcalc.y"
{ yyval.attr.sig=2;
            yyval.attr.dval = 2.71828182845904523536028747135266249775724709369995;
          }
break;
#line 861 "kcalc.tab.c"
    }
    yyssp -= yym;
    yystate = *yyssp;
    yyvsp -= yym;
    yym = yylhs[yyn];
    if (yystate == 0 && yym == 0)
    {
#if YYDEBUG
        if (yydebug)
            printf("%sdebug: after reduction, shifting from state 0 to\
 state %d\n", YYPREFIX, YYFINAL);
#endif
        yystate = YYFINAL;
        *++yyssp = YYFINAL;
        *++yyvsp = yyval;
        if (yychar < 0)
        {
            if ((yychar = yylex()) < 0) yychar = 0;
#if YYDEBUG
            if (yydebug)
            {
                yys = 0;
                if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
                if (!yys) yys = "illegal-symbol";
                printf("%sdebug: state %d, reading %d (%s)\n",
                        YYPREFIX, YYFINAL, yychar, yys);
            }
#endif
        }
        if (yychar == 0) goto yyaccept;
        goto yyloop;
    }
    if ((yyn = yygindex[yym]) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn];
    else
        yystate = yydgoto[yym];
#if YYDEBUG
    if (yydebug)
        printf("%sdebug: after reduction, shifting from state %d \
to state %d\n", YYPREFIX, *yyssp, yystate);
#endif
    if (yyssp >= yyss + yystacksize - 1)
    {
        goto yyoverflow;
    }
    *++yyssp = yystate;
    *++yyvsp = yyval;
    goto yyloop;
yyoverflow:
    yyerror("yacc stack overflow");
yyabort:
    return (1);
yyaccept:
    return (0);
}
