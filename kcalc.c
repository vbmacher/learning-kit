/**********************************************************/
/* Interpretacia vyrazov                                  */
/*--------------------------------------------------------*/
/* E -> T { ( "+" | "-" ) T }                             */
/* T -> F { ( "*" | "/" ) F }                             */
/* F -> cislo | "(" E ")"                                 */
/**********************************************************/

#include <stdio.h>
#include <ctype.h>

/*--------------------------------------------------------*/
/* syntakticke procedury                                  */
/*--------------------------------------------------------*/

int sym; /* vstupny symbol */
int E(void),T(void),F(void); /* prototypy */

int E(void){
  int op;
  int h1,h2;
  h1=T();
  while(sym=='+' || sym=='-') {
    op=sym; 
    sym=getchar();
    h2=T();
    if(op=='+')
      h1=h1+h2;
    else
      h1=h1-h2;
  }
  return (h1);
}

int T(void){
  int op;
  int h1,h2;
  h1=F();
  while(sym=='*' || sym=='/') {
    op=sym; 
    sym=getchar();
    h2=F();
    if(op=='*')
      h1=h1*h2;
    else
      h1=h1/h2;
  } return (h1);
}

int F(void) {
  int h1;
  switch(sym) {
    case 'x':
      h1 = 0;
      while(((sym=getchar())>='0' && sym<='9') || (tolower(sym)>='a' && tolower(sym)<='f')) {
        sym = tolower(sym); 
        if (sym>='a' && sym<='f')
          h1=h1*16+sym-'a'+10;
        else
          h1=h1*16+sym-'0';
      }
      break; 
    case '0': case '1':case '2':case '3':case '4': case '5':case '6':case '7':
    case '8':case '9':
      h1=sym-'0';
      while((sym=getchar())>='0' && sym<='9')
        h1=h1*10+sym-'0';
      break;
    case '(':
      sym=getchar();
      h1=E();
      if(sym==')') 
        sym=getchar();
      else
        printf("\nChybajuca prava zatvorka\n");
      break;
    default: 
       printf("\nNedovoleny symbol %c\n",sym);
       h1=0;
       break;
  }
  return (h1);
}

/*--------------------------------------------------------*/
/* hlavny program                                         */
/*--------------------------------------------------------*/

int main(void) {
  printf("Kalculator v1.00\nEnter an expression: ");
  sym=getchar();
  int vysledok = E();
  printf("\nVysledok: %d\n     hex: 0x%X\n",vysledok,vysledok);
  
  system("PAUSE");
  return(0);
}


