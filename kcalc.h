/**
 * kcalc.h
 *
 * (c) Copyright 2011, P. Jakubčo <pjakubco@gmail.com>
 *
 * KISS, YAGNI
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

#ifndef __KCALC_H__
#define __KCALC_H__

#define MAX_VARIABLES 100

enum treetype {operator_node, value_node, variable_node};

typedef struct tree {
  enum treetype nodetype;
  union {
    struct {
      struct tree *left, *right;
      int oper;
    } an_operator;

    double value;

    char* variable;
  } body;
} Tree;

int findVAR(char *name,int errorMissing);
int saveVAR(char *name, Tree *tree);
int fact(int x);
char *intToRadix(int num, int radix);
char *doubleToRadix(double num, int radix);
double evalTree(Tree *tree);
void printResult(double value);
void printHelp();

#endif
