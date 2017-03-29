/**
 * lecalc.h
 *
 * Copyright 2011 Peter Jakubco
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#ifndef __LECALC_H__
#define __LECALC_H__

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

enum {
  MODE_INTERACTIVE,
  MODE_FILE
};


int findVAR(char *name,int errorMissing);
int saveVAR(char *name, Tree *tree);
int fact(int x);
char *intToRadix(int num, int radix);
char *doubleToRadix(double num, int radix);
double evalTree(Tree *tree);
int isEvalError();

#endif
