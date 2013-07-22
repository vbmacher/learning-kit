/*
  SUDOKU.C

   Copyright 2006-2007 vbmacher <pjakubco AT gmail DOT com>

*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct {
  int mark;
  int moves[9];
  int square;  /* in what square it is located */
} field;

  field sudoku[9][9];

/******************* SEARCHING, CHECKING, ANALYSIS *************************/

/* returns 1 if sudoku has been changed, 0 otherwise */
/* it can be used only within 'findmoves' function because it can't recognize threat-like moves */
int can(int x, int y, int mark)
{
  int i, j, istart, jstart, ipodm, jpodm;
  /* at first it looks at whole row */
  for (j = 0; j < 9; j++)
    if ((j != x) && (sudoku[y][j].mark == mark)) return 0;

  /* then it looks at whole column */
  for (i = 0; i < 9; i++)
    if ((i != y) && (sudoku[i][x].mark == mark)) return 0;

  /* and then it looks at little square */
  istart = y - (y % 3);
  jstart = x - (x % 3);
  ipodm = istart + 3;
  jpodm = jstart + 3;

  for (i = istart; i < ipodm; i++)
    for (j = jstart; j < jpodm; j++)
      if (sudoku[i][j].mark == mark)
        if ((j != x) || (i != y)) return 0;
  return 1;
}

/* returns 1 if sudoku has been changed, 0 otherwise */
/* finds out a move based on already found allowed moves */
int canmove(int x, int y, int mark)
{
  return sudoku[y][x].moves[mark-1];
}

void findmoves()
{
  int i,j,k;

  for (i = 0; i < 9; i++)
    for (j = 0; j < 9; j++)
      if (!sudoku[i][j].mark)
	for (k = 0; k < 9; k++)
	  sudoku[i][j].moves[k] = can(j,i,k+1);
}

int countmoves(int x, int y)
{
  int k, cnt = 0;
  for (k = 0; k < 9; k++)
    if (sudoku[y][x].moves[k]) cnt++;
  return cnt;
}

/* returns 1 if sudoku has been changed, 0 otherwise */
/* action: checks actual square except area which is hit by the selection */
int analyzesquare(int istart, int jstart, int mark, int attack, int horizontal)
{
  int ipodm, jpodm;
  int i,j,k;

  ipodm = istart + 3;
  jpodm = jstart + 3;
  /*
     Function is used for deep analysis of already found moves using 'removebadmoves' function.
     It finds the 'mark' move in the 'sq' square out of its row having y = 'attack_y' iff 'vertical != 0',
     i.e. out of its column with x = 'attack_x' iff 'vertical == 0'.
     If the move was found (in the 'sq' square) it means that we have to leave the selection as it is.
     In the other case, it means that the 'sq' square contains the 'mark' move only in "hit" area, i.e.
     it conflicts with the original selection, which now has to step aside.
  */
  for (i = istart; i < ipodm; i++)
    for (j = jstart; j < jpodm; j++)
      if ((horizontal && (i != attack)) ||
	      (!horizontal && (j != attack))) {
          if (!sudoku[i][j].mark && sudoku[i][j].moves[mark-1])
	          return 1;
	      else if (sudoku[i][j].mark == mark) return 1;
      }
  return 0;
}

/********************* SELECTION *****************************************/

/* returns 1 if sudoku has been changed, 0 otherwise */
int markonly()
{
  int i,j,k,w= 0;

  for (i = 0; i < 9; i++)
    for (j = 0; j < 9; j++)
      if (countmoves(j,i) == 1) {
	for (k = 0; k < 9; k++)
	  if (sudoku[i][j].moves[k]) break;
	sudoku[i][j].mark = k+1;
	sudoku[i][j].moves[k] = 0;
	w = 1;
      }
  return w;
}

/* returns 1 if sudoku has been changed, 0 otherwise */
int markonlycol(int y)
{
  int j,k;
  int moves[10]; /* moves which are contained in a column */

  memset(moves, 0, sizeof(int) * 10);
  for (j = 0; j < 9; j++)
    moves[sudoku[y][j].mark]++;
  /* in moves[0] will be count of free fields */
  if (!moves[0] || moves[0] > 1) return 0;

  /* find free field */
  for (j = 0; j < 9; j++)
    if (!sudoku[y][j].mark) break;
  for (k = 1; k < 10; k++)
    if (!moves[k] && canmove(j,y,k)) {
      sudoku[y][j].mark = k;
      memset(sudoku[y][j].moves, 0, sizeof(int) * 9);
      return 1;
    }

  return 0;
}

/* returns 1 if sudoku has been changed, 0 if not */
int markonlyrow(int x)
{
  int i,k;
  int moves[10]; /* moves which are contained in a row */

  memset(moves, 0, sizeof(int) * 10);
  for (i = 0; i < 9; i++)
    moves[sudoku[i][x].mark]++;
  /* in moves[0] will be count of free fields */
  if (!moves[0] || moves[0] > 1) return 0;

  /* find free field */
  for (i = 0; i < 9; i++)
    if (!sudoku[i][x].mark) break;
  for (k = 1; k < 10; k++)
    if (!moves[k] && canmove(x,i,k)) {
      sudoku[i][x].mark = k;
      memset(sudoku[i][x].moves, 0, sizeof(int) * 9);
      return 1;
    }
  return 0;
}

/* returns 1 if sudoku has been changed, 0 otherwise */
int markonlysq(int sq)
{
  int i,j,k;
  int istart, jstart, ipodm, jpodm;
  int moves[10]; /* moves within a square */

  memset(moves, 0, sizeof(int) * 10);

  switch (sq) {
    case 0: istart = jstart = 0; break;
    case 1: istart = 0; jstart = 3; break;
    case 2: istart = 0; jstart = 6; break;
    case 3: istart = 3; jstart = 0; break;
    case 4: istart = jstart = 3; break;
    case 5: istart = 3; jstart = 6; break;
    case 6: istart = 6; jstart = 0; break;
    case 7: istart = 6; jstart = 3; break;
    case 8: istart = jstart = 6; break;
  }
  ipodm = istart + 3;
  jpodm = jstart + 3;

  for (i = istart; i < ipodm; i++)
    for (j = jstart; j < jpodm; j++)
      moves[sudoku[i][j].mark]++;
  /* in moves[0] will be count of free fields */
  if (!moves[0] || moves[0] > 1) return 0;

  /* find a free field */
  for (i = istart; i < ipodm; i++)
    for (j = jstart; j < jpodm; j++)
      if (!sudoku[i][j].mark) goto sem;
sem:
  for (k = 1; k < 10; k++)
    if (!moves[k] && canmove(j,i,k)) {
      sudoku[i][j].mark = k;
      memset(sudoku[i][j].moves, 0, sizeof(int) * 9);
      return 1;
    }

  return 0;
}

/* returns 1 if sudoku has changed, 0 otherwise */
/* action: selects a field, which contains unique move within the column */
int markfreerow(int x)
{
  int moves[9];
  int i,k,w = 0;

  /* count of all moves in a column */
  memset(moves, 0, sizeof(int) * 9);
  for (i = 0; i < 9; i++)
    if (!sudoku[i][x].mark)
      for (k = 0; k < 9; k++)
    	moves[k] += sudoku[i][x].moves[k];

  /* finds moves which are unique within a column */
  for (i = 0; i < 9; i++)
    if (!sudoku[i][x].mark)
      for (k = 0; k < 9; k++)
	if (moves[k] == 1 && canmove(x,i,k+1)) {
	  sudoku[i][x].mark = k+1;
	  memset(sudoku[i][x].moves, 0, sizeof(int) * 9);
	  w = 1;
	  break;
	}
  return w;
}

/* returns 1 if the sudoku has been changed, 0 otherwise */
/* action: selects a field, which contains an unique move within the row */
int markfreecol(int y)
{
  int moves[9];
  int j,k,w = 0;

  /* count of all moves in a row */
  memset(moves, 0, sizeof(int) * 9);
  for (j = 0; j < 9; j++)
    if (!sudoku[y][j].mark)
      for (k = 0; k < 9; k++)
	    moves[k] += sudoku[y][j].moves[k];

  /* finds moves which are unique within a row */
  for (j = 0; j < 9; j++)
    if (!sudoku[y][j].mark)
      for (k = 0; k < 9; k++)
	if (moves[k] == 1 && canmove(j,y,k+1)) {
	  sudoku[y][j].mark = k+1;
	  memset(sudoku[y][j].moves, 0, sizeof(int) * 9);
	  w = 1;
	  break;
	}
  return w;
}

/* returns 1 if sudoku has been changed, 0 otherwise */
/* action: selects a field, which contains unique move within all the square */
int markfreesq(int sq)
{
  int i,j,k,w=0;
  int istart, jstart, ipodm, jpodm;
  int moves[10]; /* moves which are contained in a square */

  memset(moves, 0, sizeof(int) * 10);

  /* find boundaries of a square */
  switch (sq) {
    case 0: istart = jstart = 0; break;
    case 1: istart = 0; jstart = 3; break;
    case 2: istart = 0; jstart = 6; break;
    case 3: istart = 3; jstart = 0; break;
    case 4: istart = jstart = 3; break;
    case 5: istart = 3; jstart = 6; break;
    case 6: istart = 6; jstart = 0; break;
    case 7: istart = 6; jstart = 3; break;
    case 8: istart = jstart = 6; break;
  }
  ipodm = istart + 3;
  jpodm = jstart + 3;

  /* count of all moves in a square */
  for (i = istart; i < ipodm; i++)
    for (j = jstart; j < jpodm; j++)
      if (!sudoku[i][j].mark)
	for (k = 0; k < 9; k++)
	  moves[k+1] += sudoku[i][j].moves[k];

  /* finds unique moves in one square */
  for (i = istart; i < ipodm; i++)
    for (j = jstart; j < jpodm; j++)
      if (!sudoku[i][j].mark)
	for (k = 1; k < 10; k++)
	  if (moves[k] == 1 && canmove(j,i,k)) {
	    sudoku[i][j].mark = k;
	    memset(sudoku[i][j].moves, 0, sizeof(int) * 9);
	    w = 1;
	    break;
	  }
  return w;
}

/* removes invalid moves */
/* returns 1 if sudoku has changed, 0 otherwise */
int removebadmoves()
{
  int i, j, k, res, w=0,istart,jstart;

  for (i = 0; i < 9; i++)
    for (j = 0; j < 9; j++)
      if (!sudoku[i][j].mark) {
        istart = i - (i % 3);
        jstart = j - (j % 3);
        for (k = 0; k < 9; k++)
	        if (sudoku[i][j].moves[k]) {
	           /* squares analysis */
               res = 1;
	           switch (sudoku[i][j].square) {
                  case 0:
                    res &= analyzesquare(0,3,k+1,i,1); /* right */
                    res &= analyzesquare(0,6,k+1,i,1); /* right */
	                res &= analyzesquare(3,0,k+1,j,0); /* down */
	                res &= analyzesquare(6,0,k+1,j,0); /* down */
            		break;
                  case 1:
            		res &= analyzesquare(0,0,k+1,i,1); /* left */
            		res &= analyzesquare(0,6,k+1,i,1); /* right */
            		res &= analyzesquare(3,3,k+1,j,0); /* down */
	                res &= analyzesquare(6,3,k+1,j,0); /* down */
            		break;
        	      case 2:
            		res &= analyzesquare(0,3,k+1,i,1); /* left */
            		res &= analyzesquare(0,0,k+1,i,1); /* left */
            		res &= analyzesquare(3,6,k+1,j,0); /* down */
            		res &= analyzesquare(6,6,k+1,j,0); /* down */
            		break;
        	      case 3:
            		res &= analyzesquare(0,0,k+1,j,0); /* up */
            		res &= analyzesquare(3,3,k+1,i,1); /* right */
	                res &= analyzesquare(3,6,k+1,i,1); /* right */
            		res &= analyzesquare(6,0,k+1,j,0); /* down */
            		break;
        	      case 4:
            		res &= analyzesquare(0,3,k+1,j,0); /* up */
	                res &= analyzesquare(3,0,k+1,i,1); /* right */
            		res &= analyzesquare(3,6,k+1,i,1); /* right */
            		res &= analyzesquare(6,3,k+1,j,0); /* down */
            		break;
        	      case 5:
            		res &= analyzesquare(3,0,k+1,i,1); /* left */
                    res &= analyzesquare(3,3,k+1,i,1); /* left */
            		res &= analyzesquare(0,6,k+1,j,0); /* up */
            		res &= analyzesquare(6,6,k+1,j,0); /* down */
            		break;
        	      case 6:
            		res &= analyzesquare(0,0,k+1,j,0); /* up */
            		res &= analyzesquare(3,0,k+1,j,0); /* up */
            		res &= analyzesquare(6,3,k+1,i,1); /* right */
            		res &= analyzesquare(6,6,k+1,i,1); /* right */
            		break;
        	      case 7:
            		res &= analyzesquare(0,3,k+1,j,0); /* up */
            		res &= analyzesquare(3,3,k+1,j,0); /* up */
            		res &= analyzesquare(6,0,k+1,i,1); /* left */
            		res &= analyzesquare(6,6,k+1,i,1); /* right */
            		break;
        	      case 8:
            		res &= analyzesquare(0,6,k+1,j,0); /* up */
            		res &= analyzesquare(3,6,k+1,j,0); /* up */
	                res &= analyzesquare(6,0,k+1,i,1); /* left */
            		res &= analyzesquare(6,3,k+1,i,1); /* left */
            		break;
	    }
        sudoku[i][j].moves[k] = res;
        w |= !res;
	  }
    }
  return w;
}

/* returns 1 if the sudoku has been changed, 0 otherwise */
int removecrossmoves()
{
  int i,j,k;

  for (i = 0; i < 9; i++)
    for (j = 0; j < 9; j++)
      if (!sudoku[i][j].mark)
	for (k = 0; k < 9; k++)
	  if (sudoku[i][j].moves[k])
	    sudoku[i][j].moves[k] = can(j,i,k+1);

}


/************************* PRINT ********************************************/

void print_sudoku()
{
  int i,j;
  printf("\n");
  for (i = 0; i < 9; i++) {
    if (i && !(i % 3)) printf("\n");
    for (j = 0; j < 9; j++) {
      if (j && !(j % 3)) printf("  ");
      printf("%d ", sudoku[i][j].mark);
    }
    printf("\n");
  }
}

void print_squares()
{
  int i,j;
  printf("\n");
  for (i = 0; i < 9; i++) {
    if (i && !(i % 3)) printf("\n");
    for (j = 0; j < 9; j++) {
      if (j && !(j % 3)) printf("  ");
      printf("%d ", sudoku[i][j].square);
    }
    printf("\n");
  }
}


void print_moves()
{
  int i,j,k;
  printf("\n");
  for (i = 0; i < 9; i++) {
    for (j = 0; j < 9; j++) {
      if (!sudoku[i][j].mark) {
	printf("%d,%d - ", i, j);
	for (k = 0; k < 9; k++)
	  if (sudoku[i][j].moves[k]) printf("%d,",k+1);
	printf("   | ");
      }
    }
    printf("\n");
  }
}


int main()
{
  FILE *fr;
  int i,j, sq = 0;
  int zmena = 0, res;

  printf("\nSUDOKU v1.1");
  printf("\n(c) Copyright 2006, vbmacher");

  if ((fr = fopen("sudoku.txt", "r")) == NULL) {
    printf("\nFile sudoku.txt cannot be found.");
    return 0;
  }
  sq = 2;
  for (i = 0; i < 9; i++) {
    if (i && !(i % 3)) sq++;
    else sq -= 2;
    for (j = 0; j < 9; j++) {
      fscanf(fr, "%d ", &sudoku[i][j].mark);
      memset(sudoku[i][j].moves, 0, sizeof(int) * 9);
      if (j && !(j % 3)) sq++;
      sudoku[i][j].square = sq;
    }
  }
  fclose(fr);

  print_sudoku();
  printf("\n-------------------------------------------");

    findmoves();
zac:
    removebadmoves();
    removecrossmoves();
    if (markonly()) goto zac;
    for (i = 0; i < 9; i++) {
      if (markonlycol(i)) goto zac;
      if (markfreecol(i)) goto zac;
    }
    for (j = 0; j < 9; j++) {
      if (markonlyrow(j)) goto zac;
      if (markfreerow(j)) goto zac;
    }
    for (i = 0; i < 9; i++) {
      if (markonlysq(i)) goto zac;
      if (markfreesq(i)) goto zac;
    }

  print_sudoku();
  print_moves();

  getchar();
  getchar();
  return 0;
}
