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
     FILENAME = 259,
     INT_VAL = 260,
     DBL_VAL = 261,
     LPAR = 262,
     RPAR = 263,
     OP_EQU = 264,
     HELP = 265,
     USE = 266,
     QUIT = 267,
     EOL = 268,
     M_SIN = 269,
     M_COS = 270,
     M_TAN = 271,
     M_COTAN = 272,
     M_LOG = 273,
     M_LOG2 = 274,
     M_LOGE = 275,
     M_SQRT = 276,
     M_CEIL = 277,
     M_FLOOR = 278,
     CONST_PI = 279,
     CONST_E = 280,
     OP_SUB = 281,
     OP_ADD = 282,
     OP_DIV = 283,
     OP_MUL = 284,
     OP_MOD = 285,
     USUB = 286,
     UADD = 287,
     OP_POW = 288,
     OP_FACT = 289
   };
#endif
/* Tokens.  */
#define VARIABLE 258
#define FILENAME 259
#define INT_VAL 260
#define DBL_VAL 261
#define LPAR 262
#define RPAR 263
#define OP_EQU 264
#define HELP 265
#define USE 266
#define QUIT 267
#define EOL 268
#define M_SIN 269
#define M_COS 270
#define M_TAN 271
#define M_COTAN 272
#define M_LOG 273
#define M_LOG2 274
#define M_LOGE 275
#define M_SQRT 276
#define M_CEIL 277
#define M_FLOOR 278
#define CONST_PI 279
#define CONST_E 280
#define OP_SUB 281
#define OP_ADD 282
#define OP_DIV 283
#define OP_MUL 284
#define OP_MOD 285
#define USUB 286
#define UADD 287
#define OP_POW 288
#define OP_FACT 289




#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
typedef union YYSTYPE
{

/* Line 2068 of yacc.c  */
#line 51 "parser.y"

  char*  name;
  double value;
  Tree*  tree;



/* Line 2068 of yacc.c  */
#line 126 "parser.h"
} YYSTYPE;
# define YYSTYPE_IS_TRIVIAL 1
# define yystype YYSTYPE /* obsolescent; will be withdrawn */
# define YYSTYPE_IS_DECLARED 1
#endif




