/* A Bison parser, made by GNU Bison 2.5.  */

/* Bison interface for Yacc-like parsers in C
   
      Copyright (C) 1984, 1989-1990, 2000-2011 Free Software Foundation, Inc.
   
   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.
   
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   
   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.
   
   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */


/* Tokens.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
   /* Put the tokens into the symbol table, so that GDB and other debuggers
      know about them.  */
   enum yytokentype {
     VARIABLE = 258,
     INT_VAL = 259,
     DBL_VAL = 260,
     LPAR = 261,
     RPAR = 262,
     OP_EQU = 263,
     HELP = 264,
     QUIT = 265,
     M_SIN = 266,
     M_COS = 267,
     M_TAN = 268,
     M_COTAN = 269,
     M_LOG = 270,
     M_LOG2 = 271,
     M_LOGE = 272,
     M_SQRT = 273,
     M_CEIL = 274,
     M_FLOOR = 275,
     CONST_PI = 276,
     CONST_E = 277,
     OP_SUB = 278,
     OP_ADD = 279,
     OP_DIV = 280,
     OP_MUL = 281,
     OP_MOD = 282,
     USUB = 283,
     UADD = 284,
     OP_POW = 285,
     OP_FACT = 286
   };
#endif
/* Tokens.  */
#define VARIABLE 258
#define INT_VAL 259
#define DBL_VAL 260
#define LPAR 261
#define RPAR 262
#define OP_EQU 263
#define HELP 264
#define QUIT 265
#define M_SIN 266
#define M_COS 267
#define M_TAN 268
#define M_COTAN 269
#define M_LOG 270
#define M_LOG2 271
#define M_LOGE 272
#define M_SQRT 273
#define M_CEIL 274
#define M_FLOOR 275
#define CONST_PI 276
#define CONST_E 277
#define OP_SUB 278
#define OP_ADD 279
#define OP_DIV 280
#define OP_MUL 281
#define OP_MOD 282
#define USUB 283
#define UADD 284
#define OP_POW 285
#define OP_FACT 286




#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
typedef union YYSTYPE
{

/* Line 2068 of yacc.c  */
#line 42 "parser.y"

  char*  name;
  double value;
  Tree*  tree;



/* Line 2068 of yacc.c  */
#line 120 "parser.h"
} YYSTYPE;
# define YYSTYPE_IS_TRIVIAL 1
# define yystype YYSTYPE /* obsolescent; will be withdrawn */
# define YYSTYPE_IS_DECLARED 1
#endif

extern YYSTYPE yylval;


