/* --------------------------------------------------------------------- 
//    Subor:  "lexyy.cpp" 
// 
//    Tento subor bol generovany programom KPI-Lex 1.00.
// 
//    Pouzity specifikacny subor: "kcalc.l".
//    Pouzity skeleton subor:      "C:\Program Files\PPVP\ncform".
// 
//    Vytvorene:  Sun Oct 10 06:49:45 2010
// 
// ------------------------------------------------------------------- */ 

/* -------------------------------------------------------------------------- 
// Nasledujuca cast tohoto suboru je generovana priamo programom KPI-Lex.
-------------------------------------------------------------------------- */

/* --- Standardne hlavickove subory ----------- */
#include <stdio.h> 
#include <alloc.h> 

/* --- Makro definicie ----------------------------------- */
#define   U(x)       x
#define   NLSTATE    yyprevious = YYNEWLINE
#define   BEGIN      yybgin = yysvec + 1 +
#define   INITIAL    0
#define   YYLERR     yysvec
#define   YYSTATE    ( yyestate - yysvec - 1 )
#define   YYOPTIM    1
#define   YYLMAX     200

#define   output( c )   putc( c, yyout )
#define   input()       ( ( ( yytchar = yysptr > yysbuf ? U( *--yysptr ) : getc( yyin ) ) == 10 ? ( yylineno++, yytchar ) : yytchar ) == EOF ? 0 : yytchar ) 
#define   unput( c )    {  yytchar = ( c ); if ( yytchar == '\n') yylineno--; *yysptr++ = yytchar; }
#define   yymore()      ( yymorfg = 1 )
#define   ECHO          fprintf( yyout,"%s", yytext )
#define   REJECT        { nstr = yyreject(); goto yyfussy; }

/* -------- Prototypy lokalnych funkcii --------------- */
/* --- ( generovane automaticky programom KPI-Lex------ */

int   yylook( void ); 
int   yyback( int  *p, int  m );
int  yyinput( void );
void  yyoutput( int  c );
void  yyunput( int c );

/* ---  Definicie premennych a ine definicie ------------- */

FILE    *yyin   =   {stdin},  
        *yyout  =   {stdout} ;

struct  yysvf  { 
    struct  yywork   *yystoff;
    struct  yysvf    *yyother;
    int              *yystops;
};

struct           yysvf   *yyestate;

extern  struct   yysvf    yysvec[],
                         *yybgin;

int   yyleng;  
int   yymorfg; 
int   yytchar; 

extern  char    yytext[];
extern  char    yysbuf[];
extern  char   *yysptr;
extern  int     yylineno;



#include <string.h>

int yywrap(void);
extern YYSTYPE yylval;

# define YYNEWLINE 10
yylex(){
int nstr; extern int yyprevious;

#pragma warn -rch

   while ( ( nstr = yylook() ) >= 0 )  { 
yyfussy:
        switch( nstr )  {
            case 0:
                     if ( yywrap() ) 
                          return( 0 );
                           break;
case 1:
 { return 0; }
break;
case 2:
{ }
break;
case 3:
{ return OP_ADD; }
break;
case 4:
{ return OP_SUB; }
break;
case 5:
{ return OP_MUL; }
break;
case 6:
{ return OP_DIV; }
break;
case 7:
{ return OP_FACT; }
break;
case 8:
{ return OP_POW; }
break;
case 9:
{ return OP_MOD; }
break;
case 10:
{ return OP_MOD; }
break;
case 11:
{ return OP_EQU; }
break;
case 12:
{ return LPAR; }
break;
case 13:
{ return RPAR; }
break;
case 14:
{ return M_SIN; }
break;
case 15:
{ return M_COS; }
break;
case 16:
{ return M_TAN; }
break;
case 17:
{ return M_COTAN; }
break;
case 18:
{ return M_LOG; }
break;
case 19:
{ return M_LOG2; }
break;
case 20:
 { return M_LOGE; }
break;
case 21:
{ return M_SQRT; }
break;
case 22:
{ return M_CEIL; }
break;
case 23:
{ return M_FLOOR; }
break;
case 24:
{ return CONST_PI; }
break;
case 25:
 { return CONST_E; }
break;
case 26:
   { yywrap(); return QUIT; }
break;
case 27:
{ yywrap(); return QUIT; }
break;
case 28:
{ yywrap(); return QUIT; }
break;
case 29:
   { yylval.name = strdup(yytext);
           return VARIABLE; }
break;
case 30:
   { sscanf(yytext,"%d",&yylval.attr.ival);
           yylval.attr.sig = 1;
           return INT_VAL;
         }
break;
case 31:
{ sscanf(yytext+1,"%x",&yylval.attr.ival);
           yylval.attr.sig = 1;
           return INT_VAL;
         }
break;
case 32:
{ int i;
           for (i = yylval.attr.ival = 0; yytext[i+1] != 0; i++)
             yylval.attr.ival = yylval.attr.ival * 8 + yytext[i+1] - '0';
           yylval.attr.sig = 1;
           return INT_VAL;
         }
break;
case 33:
{ int i;
           for (i = yylval.attr.ival = 0; yytext[i+1] != 0; i++)
             yylval.attr.ival = yylval.attr.ival * 2 + yytext[i+1] - '0';
           yylval.attr.sig = 1;
           return INT_VAL;
         }
break;
case 34:
{ sscanf(yytext,"%lf", &yylval.attr.dval);
                yylval.attr.sig = 2;
                return DBL_VAL;
              }
break;

            case -1: 
                     break; 

            default: 
                     fprintf( yyout, "bad switch yylook  %d", nstr ); 
                     break;
        } /* end of switch */
   } /* end of while */

   return ( 0 ); 
#pragma warn +rch

}/* end of yylex */


/**
 * End of input.
 */
int yywrap(void){
  return(1);
}

/* --- Prekladove tabulky -- Cast generovana programom KPI-Lex --- */

int   yyvstop[]  =  {
                         0,
                         /* Akcia pre stav 2 */
                         1,
                         0,
                         /* Akcia pre stav 3 */
                         2,
                         0,
                         /* Akcia pre stav 4 */
                         7,
                         0,
                         /* Akcia pre stav 5 */
                         9,
                         0,
                         /* Akcia pre stav 6 */
                         12,
                         0,
                         /* Akcia pre stav 7 */
                         13,
                         0,
                         /* Akcia pre stav 8 */
                         5,
                         0,
                         /* Akcia pre stav 9 */
                         3,
                         0,
                         /* Akcia pre stav 10 */
                         4,
                         0,
                         /* Akcia pre stav 11 */
                         6,
                         0,
                         /* Akcia pre stav 12 */
                         30,
                         0,
                         /* Akcia pre stav 13 */
                         11,
                         0,
                         /* Akcia pre stav 14 */
                         29,
                         0,
                         /* Akcia pre stav 15 */
                         25,
                         29,
                         0,
                         /* Akcia pre stav 16 */
                         29,
                         0,
                         /* Akcia pre stav 17 */
                         8,
                         0,
                         /* Akcia pre stav 18 */
                         29,
                         0,
                         /* Akcia pre stav 19 */
                         29,
                         0,
                         /* Akcia pre stav 20 */
                         29,
                         0,
                         /* Akcia pre stav 21 */
                         29,
                         0,
                         /* Akcia pre stav 22 */
                         29,
                         0,
                         /* Akcia pre stav 23 */
                         29,
                         0,
                         /* Akcia pre stav 24 */
                         29,
                         0,
                         /* Akcia pre stav 25 */
                         26,
                         29,
                         0,
                         /* Akcia pre stav 26 */
                         29,
                         0,
                         /* Akcia pre stav 27 */
                         29,
                         0,
                         /* Akcia pre stav 28 */
                         29,
                         0,
                         /* Akcia pre stav 30 */
                         24,
                         29,
                         0,
                         /* Akcia pre stav 31 */
                         33,
                         0,
                         /* Akcia pre stav 32 */
                         29,
                         0,
                         /* Akcia pre stav 33 */
                         29,
                         0,
                         /* Akcia pre stav 34 */
                         29,
                         0,
                         /* Akcia pre stav 35 */
                         29,
                         0,
                         /* Akcia pre stav 36 */
                         20,
                         29,
                         0,
                         /* Akcia pre stav 37 */
                         29,
                         0,
                         /* Akcia pre stav 38 */
                         29,
                         0,
                         /* Akcia pre stav 39 */
                         32,
                         0,
                         /* Akcia pre stav 40 */
                         29,
                         0,
                         /* Akcia pre stav 41 */
                         29,
                         0,
                         /* Akcia pre stav 42 */
                         29,
                         0,
                         /* Akcia pre stav 43 */
                         29,
                         0,
                         /* Akcia pre stav 44 */
                         31,
                         0,
                         /* Akcia pre stav 45 */
                         29,
                         31,
                         0,
                         /* Akcia pre stav 46 */
                         34,
                         0,
                         /* Akcia pre stav 47 */
                         29,
                         0,
                         /* Akcia pre stav 48 */
                         15,
                         29,
                         0,
                         /* Akcia pre stav 49 */
                         29,
                         0,
                         /* Akcia pre stav 50 */
                         29,
                         0,
                         /* Akcia pre stav 51 */
                         29,
                         0,
                         /* Akcia pre stav 52 */
                         18,
                         29,
                         0,
                         /* Akcia pre stav 53 */
                         10,
                         29,
                         0,
                         /* Akcia pre stav 54 */
                         29,
                         0,
                         /* Akcia pre stav 55 */
                         14,
                         29,
                         0,
                         /* Akcia pre stav 56 */
                         29,
                         0,
                         /* Akcia pre stav 57 */
                         16,
                         29,
                         0,
                         /* Akcia pre stav 58 */
                         22,
                         29,
                         0,
                         /* Akcia pre stav 59 */
                         29,
                         0,
                         /* Akcia pre stav 60 */
                         28,
                         29,
                         0,
                         /* Akcia pre stav 61 */
                         29,
                         0,
                         /* Akcia pre stav 62 */
                         19,
                         0,
                         /* Akcia pre stav 63 */
                         27,
                         29,
                         0,
                         /* Akcia pre stav 64 */
                         21,
                         29,
                         0,
                         /* Akcia pre stav 65 */
                         17,
                         29,
                         0,
                         /* Akcia pre stav 66 */
                         23,
                         29,
                         0,
                         0
};  /* koniec tabulky "yyvstop" */ 

#define   YYTYPE    char
struct  yywork   { 
                   YYTYPE verify,  
                          advance; 
}  yycrank[]  =  { 
                      0,0,	0,0,	0,0,	0,0,	
                      0,0,	0,0,	0,0,	0,0,	
                      0,0,	0,0,	0,0,	1,3,	
                      0,0,	0,0,	0,0,	0,0,	
                      0,0,	0,0,	0,0,	0,0,	
                      0,0,	0,0,	0,0,	0,0,	
                      0,0,	0,0,	0,0,	0,0,	
                      0,0,	0,0,	0,0,	0,0,	
                      0,0,	1,4,	1,5,	0,0,	
                      0,0,	0,0,	1,6,	0,0,	
                      0,0,	1,7,	1,8,	1,9,	
                      1,10,	0,0,	1,11,	0,0,	
                      1,12,	1,13,	1,13,	1,13,	
                      1,13,	1,13,	1,13,	1,13,	
                      1,13,	1,13,	1,13,	0,0,	
                      0,0,	0,0,	1,14,	0,0,	
                      0,0,	0,0,	1,15,	1,15,	
                      1,15,	1,15,	1,16,	1,15,	
                      1,15,	1,15,	1,15,	1,15,	
                      1,15,	1,15,	1,15,	1,15,	
                      1,15,	1,17,	1,15,	1,15,	
                      1,15,	1,15,	1,15,	1,15,	
                      1,15,	1,15,	1,15,	1,15,	
                      19,32,	19,32,	0,0,	1,18,	
                      0,0,	0,0,	1,15,	1,19,	
                      1,20,	1,15,	1,21,	1,22,	
                      1,15,	1,15,	1,15,	1,15,	
                      1,15,	1,23,	1,24,	1,15,	
                      1,25,	1,15,	1,26,	1,15,	
                      1,27,	1,28,	1,15,	1,15,	
                      1,15,	1,29,	1,15,	1,15,	
                      13,30,	17,31,	13,13,	13,13,	
                      13,13,	13,13,	13,13,	13,13,	
                      13,13,	13,13,	13,13,	13,13,	
                      15,15,	15,15,	15,15,	15,15,	
                      15,15,	15,15,	15,15,	15,15,	
                      15,15,	15,15,	15,15,	15,15,	
                      15,15,	15,15,	15,15,	15,15,	
                      15,15,	15,15,	15,15,	15,15,	
                      15,15,	15,15,	15,15,	15,15,	
                      15,15,	15,15,	21,35,	22,36,	
                      23,37,	23,38,	24,39,	26,41,	
                      15,15,	15,15,	15,15,	15,15,	
                      15,15,	15,15,	15,15,	15,15,	
                      15,15,	15,15,	15,15,	15,15,	
                      15,15,	15,15,	15,15,	15,15,	
                      15,15,	15,15,	15,15,	15,15,	
                      15,15,	15,15,	15,15,	15,15,	
                      15,15,	15,15,	20,33,	25,40,	
                      25,40,	25,40,	25,40,	25,40,	
                      25,40,	25,40,	25,40,	27,42,	
                      20,34,	28,44,	32,32,	32,32,	
                      33,48,	34,49,	34,50,	27,43,	
                      29,45,	29,45,	29,45,	29,45,	
                      29,45,	29,45,	29,45,	29,45,	
                      29,45,	29,45,	35,51,	36,52,	
                      38,53,	39,54,	41,55,	42,56,	
                      43,57,	29,46,	29,46,	29,46,	
                      29,46,	29,46,	29,46,	30,47,	
                      30,47,	30,47,	30,47,	30,47,	
                      30,47,	30,47,	30,47,	30,47,	
                      30,47,	40,40,	40,40,	40,40,	
                      40,40,	40,40,	40,40,	40,40,	
                      40,40,	44,58,	48,59,	50,60,	
                      51,61,	52,62,	53,63,	55,64,	
                      57,65,	29,46,	29,46,	29,46,	
                      29,46,	29,46,	29,46,	45,45,	
                      45,45,	45,45,	45,45,	45,45,	
                      45,45,	45,45,	45,45,	45,45,	
                      45,45,	60,66,	62,67,	0,0,	
                      0,0,	0,0,	0,0,	0,0,	
                      45,45,	45,45,	45,45,	45,45,	
                      45,45,	45,45,	46,45,	46,45,	
                      46,45,	46,45,	46,45,	46,45,	
                      46,45,	46,45,	46,45,	46,45,	
                      0,0,	0,0,	0,0,	0,0,	
                      0,0,	0,0,	0,0,	46,46,	
                      46,46,	46,46,	46,46,	46,46,	
                      46,46,	0,0,	0,0,	0,0,	
                      45,45,	45,45,	45,45,	45,45,	
                      45,45,	45,45,	0,0,	0,0,	
                      0,0,	0,0,	0,0,	0,0,	
                      0,0,	0,0,	0,0,	0,0,	
                      0,0,	0,0,	0,0,	0,0,	
                      0,0,	0,0,	0,0,	46,46,	
                      46,46,	46,46,	46,46,	46,46,	
                      46,46,	0,0,	0,0,	0,0,	
                      0,0
};  /* koniec tabulky  "yycrank" */ 

struct  yysvf   yysvec[]  =  {
0,	0,	0,
yycrank+1,	0,		0,			/* stav 0 */
yycrank+0,	yysvec+1,	0,			/* stav 1 */
yycrank+0,	0,		yyvstop+1,		/* stav 2 */
yycrank+0,	0,		yyvstop+3,		/* stav 3 */
yycrank+0,	0,		yyvstop+5,		/* stav 4 */
yycrank+0,	0,		yyvstop+7,		/* stav 5 */
yycrank+0,	0,		yyvstop+9,		/* stav 6 */
yycrank+0,	0,		yyvstop+11,		/* stav 7 */
yycrank+0,	0,		yyvstop+13,		/* stav 8 */
yycrank+0,	0,		yyvstop+15,		/* stav 9 */
yycrank+0,	0,		yyvstop+17,		/* stav 10 */
yycrank+0,	0,		yyvstop+19,		/* stav 11 */
yycrank+78,	0,		yyvstop+21,		/* stav 12 */
yycrank+0,	0,		yyvstop+23,		/* stav 13 */
yycrank+71,	0,		yyvstop+25,		/* stav 14 */
yycrank+0,	yysvec+15,	yyvstop+27,		/* stav 15 */
yycrank+52,	yysvec+15,	yyvstop+30,		/* stav 16 */
yycrank+0,	0,		yyvstop+32,		/* stav 17 */
yycrank+44,	yysvec+15,	yyvstop+34,		/* stav 18 */
yycrank+93,	yysvec+15,	yyvstop+36,		/* stav 19 */
yycrank+42,	yysvec+15,	yyvstop+38,		/* stav 20 */
yycrank+55,	yysvec+15,	yyvstop+40,		/* stav 21 */
yycrank+54,	yysvec+15,	yyvstop+42,		/* stav 22 */
yycrank+55,	yysvec+15,	yyvstop+44,		/* stav 23 */
yycrank+147,	yysvec+15,	yyvstop+46,		/* stav 24 */
yycrank+50,	yysvec+15,	yyvstop+48,		/* stav 25 */
yycrank+98,	yysvec+15,	yyvstop+51,		/* stav 26 */
yycrank+108,	yysvec+15,	yyvstop+53,		/* stav 27 */
yycrank+164,	yysvec+15,	yyvstop+55,		/* stav 28 */
yycrank+187,	0,		0,			/* stav 29 */
yycrank+0,	yysvec+15,	yyvstop+57,		/* stav 30 */
yycrank+158,	0,		yyvstop+60,		/* stav 31 */
yycrank+103,	yysvec+15,	yyvstop+62,		/* stav 32 */
yycrank+94,	yysvec+15,	yyvstop+64,		/* stav 33 */
yycrank+117,	yysvec+15,	yyvstop+66,		/* stav 34 */
yycrank+112,	yysvec+15,	yyvstop+68,		/* stav 35 */
yycrank+0,	yysvec+15,	yyvstop+70,		/* stav 36 */
yycrank+121,	yysvec+15,	yyvstop+73,		/* stav 37 */
yycrank+125,	yysvec+15,	yyvstop+75,		/* stav 38 */
yycrank+197,	0,		yyvstop+77,		/* stav 39 */
yycrank+121,	yysvec+15,	yyvstop+79,		/* stav 40 */
yycrank+117,	yysvec+15,	yyvstop+81,		/* stav 41 */
yycrank+114,	yysvec+15,	yyvstop+83,		/* stav 42 */
yycrank+143,	yysvec+15,	yyvstop+85,		/* stav 43 */
yycrank+219,	0,		yyvstop+87,		/* stav 44 */
yycrank+242,	yysvec+15,	yyvstop+89,		/* stav 45 */
yycrank+0,	yysvec+30,	yyvstop+92,		/* stav 46 */
yycrank+146,	yysvec+15,	yyvstop+94,		/* stav 47 */
yycrank+0,	yysvec+15,	yyvstop+96,		/* stav 48 */
yycrank+158,	yysvec+15,	yyvstop+99,		/* stav 49 */
yycrank+140,	yysvec+15,	yyvstop+101,		/* stav 50 */
yycrank+146,	yysvec+15,	yyvstop+103,		/* stav 51 */
yycrank+208,	yysvec+15,	yyvstop+105,		/* stav 52 */
yycrank+0,	yysvec+15,	yyvstop+108,		/* stav 53 */
yycrank+143,	yysvec+15,	yyvstop+111,		/* stav 54 */
yycrank+0,	yysvec+15,	yyvstop+113,		/* stav 55 */
yycrank+144,	yysvec+15,	yyvstop+116,		/* stav 56 */
yycrank+0,	yysvec+15,	yyvstop+118,		/* stav 57 */
yycrank+0,	yysvec+15,	yyvstop+121,		/* stav 58 */
yycrank+167,	yysvec+15,	yyvstop+124,		/* stav 59 */
yycrank+0,	yysvec+15,	yyvstop+126,		/* stav 60 */
yycrank+164,	yysvec+15,	yyvstop+129,		/* stav 61 */
yycrank+0,	0,		yyvstop+131,		/* stav 62 */
yycrank+0,	yysvec+15,	yyvstop+133,		/* stav 63 */
yycrank+0,	yysvec+15,	yyvstop+136,		/* stav 64 */
yycrank+0,	yysvec+15,	yyvstop+139,		/* stav 65 */
yycrank+0,	yysvec+15,	yyvstop+142,		/* stav 66 */
0,	0,	0
};  /* koniec tabulky "yysvec" */ 

struct  yywork   *yytop  = yycrank + 344;
struct  yysvf    *yybgin = yysvec  + 1;

char  yymatch[]  =  {
                     00  ,01  ,01  ,01  ,01  ,01  ,01  ,01  ,
                     01  ,01  ,01  ,01  ,01  ,01  ,01  ,01  ,
                     01  ,01  ,01  ,01  ,01  ,01  ,01  ,01  ,
                     01  ,01  ,01  ,01  ,01  ,01  ,01  ,01  ,
                     01  ,01  ,01  ,01  ,01  ,01  ,01  ,01  ,
                     01  ,01  ,01  ,01  ,01  ,01  ,01  ,01  ,
                     '0' ,'0' ,'2' ,'2' ,'2' ,'2' ,'2' ,'2' ,
                     '8' ,'8' ,01  ,01  ,01  ,01  ,01  ,01  ,
                     01  ,'A' ,'A' ,'A' ,'A' ,'A' ,'A' ,'G' ,
                     'G' ,'G' ,'G' ,'G' ,'G' ,'G' ,'G' ,'G' ,
                     'G' ,'G' ,'G' ,'G' ,'G' ,'G' ,'G' ,'G' ,
                     'G' ,'G' ,'G' ,01  ,01  ,01  ,01  ,01  ,
                     01  ,'A' ,'A' ,'A' ,'A' ,'A' ,'A' ,'G' ,
                     'G' ,'G' ,'G' ,'G' ,'G' ,'G' ,'G' ,'G' ,
                     'G' ,'G' ,'G' ,'G' ,'G' ,'G' ,'G' ,'G' ,
                     'G' ,'G' ,'G' ,01  ,01  ,01  ,01  ,01  ,
                     0
}; /* koniec tabulky "yymatch" */ 

char  yyextra[]  =   {
                         0, 0, 0, 0, 0, 0, 0, 0, 
                         0, 0, 0, 0, 0, 0, 0, 0, 
                         0, 0, 0, 0, 0, 0, 0, 0, 
                         0, 0, 0, 0, 0, 0, 0, 0, 
                         0, 0, 0, 0, 0, 0, 0, 0, 
                         0
};  /* koniec tabulky "yyextra" */ 


/* ----------------------------------------------------------------------- */
/*             Section copied from skeleton file  "ncform"                 */
/* ----------------------------------------------------------------------- */

#define    YYU(x)    x
#define    NLSTATE   yyprevious = YYNEWLINE

extern struct   yysvf   *yyestate;
struct          yysvf   *yylstate [YYLMAX],
		       **yylsp,
		       **yyolsp;

int      yylineno  = 1;

char     yytext[ YYLMAX ];
char     yysbuf[YYLMAX];
char    *yysptr = yysbuf;
int     *yyfnd;
int      yyprevious = YYNEWLINE;


int  yylook( void )
{
    register struct  yysvf    *yystate, **lsp;
    register struct  yywork   *yyt;

    struct           yysvf    *yyz;
    struct           yywork   *yyr;

    int      yych;

#ifdef  LEXDEBUG
    int      debug;
#endif

    char    *yylastch;


    /* start off machines */

#ifdef LEXDEBUG
    debug  =  0;
#endif

    if ( !yymorfg  )  {
	yylastch  =  yytext;
    }
    else  {
	yymorfg  =  0;
	yylastch = yytext + yyleng;
    }

    for(;;)  {
	lsp = yylstate;
	yyestate = yystate = yybgin;

	if ( yyprevious == YYNEWLINE )
	   yystate++;

	for (;;)  {

#ifdef LEXDEBUG
	    if ( debug )
	       fprintf( yyout, "state %d\n", yystate - yysvec - 1 );
#endif
	    yyt = yystate->yystoff;

	    /* may not be any transitions */
	    if ( yyt == yycrank )  {
		yyz = yystate->yyother;

		if ( yyz == 0 )
		   break;

		if ( yyz->yystoff == yycrank )
		   break;
	    }

	    *yylastch++ = yych = input();

tryagain:

#ifdef LEXDEBUG
	    if ( debug ) {
	       fprintf( yyout, "char " );
	       allprint( yych );
	       putchar( '\n' );
	    }
#endif

	    yyr = yyt;

	    if ( (int) yyt > (int) yycrank )  {
		yyt = yyr + yych;

		if ( yyt <= yytop &&  yyt->verify + yysvec == yystate)  {

		    /* error transitions */
		    if ( yyt->advance + yysvec == YYLERR )  {
		       unput( *--yylastch );
		       break;
		    }

		    *lsp++ = yystate = yyt->advance + yysvec;
		    goto contin;
		}
	    }

#ifdef YYOPTIM

	    /* r < yycrank */
	    else if ( (int) yyt < (int) yycrank )  {
		yyt = yyr = yycrank + ( yycrank - yyt );

#ifdef LEXDEBUG
		if( debug )
		   fprintf( yyout, "compressed state\n" );
#endif

		yyt = yyt + yych;

		if ( yyt <= yytop  &&  yyt->verify + yysvec == yystate )  {

		   /* error transitions */
		   if ( yyt->advance + yysvec == YYLERR )  {
		      unput( *--yylastch );
		      break;
		   }

		   *lsp++ = yystate = yyt->advance + yysvec;
		   goto contin;
		}

		yyt = yyr + YYU( yymatch[yych] );

#ifdef LEXDEBUG
		if ( debug )  {
		   fprintf( yyout, "try fall back character " );
		   allprint( YYU( yymatch[yych] ) );
		   putchar( '\n' );
		}
#endif

		if ( yyt <= yytop  &&  yyt->verify + yysvec == yystate )  {

		   /* error transition */
		   if ( yyt->advance + yysvec == YYLERR )  {
		      unput( *--yylastch );
		      break;
		   }

		   *lsp++ = yystate = yyt->advance + yysvec;
		   goto contin;
		}
	    }

	    if (
		  ( yystate = yystate->yyother) != 0  &&
		  (yyt= yystate->yystoff) != yycrank
	       )  {

#ifdef  LEXDEBUG
	       if ( debug )
		  fprintf( yyout, "fall back to state %d\n",
				  yystate - yysvec - 1 );
#endif
	       goto  tryagain;
	    }

#endif
	    else  {
		unput( *--yylastch );
		break;
	    }

contin:

#ifdef  LEXDEBUG
	    if ( debug )  {
	       fprintf( yyout, "state %d char ", yystate - yysvec - 1 );
	       allprint( yych );
	       putchar( '\n' );
	    }
#endif
	    ;     /* empty command */

	}

#ifdef LEXDEBUG
	if ( debug )  {
	   fprintf( yyout, "stopped at %d with ", *(lsp - 1) - yysvec - 1 );
	   allprint( yych );
	   putchar( '\n' );
	}
#endif

	while ( lsp-- > yylstate )  {

	    *yylastch--  =  0;

	    if (
		 *lsp  !=  0  &&
		 ( yyfnd = (*lsp)->yystops ) != 0 &&
		 *yyfnd > 0
	       )  {

	       yyolsp = lsp;

	       /* must backup */
	       if ( yyextra[*yyfnd] )   {

		  while (
			  yyback( (*lsp)->yystops, -*yyfnd ) != 1 &&
			  lsp > yylstate
			)  {

			lsp--;
			unput( *yylastch-- );
		  }
	       }

	       yyprevious = YYU( *yylastch );
	       yylsp  = lsp;
	       yyleng = yylastch - yytext + 1;
	       yytext[yyleng]  =  0;

#ifdef LEXDEBUG
	       if ( debug )  {
		  fprintf( yyout, "\nmatch " );
		  sprint( yytext );
		  fprintf( yyout, " action %d\n", *yyfnd );
		}
#endif

		return( *yyfnd++ );
	    }

	    unput( *yylastch );
	}

	if ( yytext[0] == 0  )  {    /* && feof(yyin) */

	   yysptr = yysbuf;

	   return ( 0 );
	}

	yyprevious = yytext[0] = input();

	if ( yyprevious > 0 )
	   output( yyprevious );

	yylastch = yytext;

#ifdef LEXDEBUG
	if ( debug )
	   putchar( '\n' );
#endif

    }

}



int  yyback( int  *p, int  m )
{
    if ( p  == 0 )
       return ( 0 );

    while ( *p ) {
       if ( *p++  ==  m )
	  return ( 1 );
    }

    return ( 0 );
}


/* the following are only used in the lex library */

int  yyinput( void )
{
    return ( input() );
}


void  yyoutput( int  c )
{
    output( c );
}


void  yyunput( int c )
{
    unput( c );
}


