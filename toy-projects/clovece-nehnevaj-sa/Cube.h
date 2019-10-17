/*
 *  TECHNICKÁ UNIVERZITA KOŠICE
 *  FAKULTA ELETKROTECHNIKY A INFORMATIKY
 *  KATEDRA POÈÍTAÈOV
 * ---------------------------------------
 *  Èloveèe, nehnevaj sa !
 * ---------------------------------------
 *  Cube.h
 *
 * (c) Copyright 2006, P. Jakubèo, E.Danková, O.Drusa
 *
 */

#ifndef __CUBE_H__
#define __CUBE_H__

 #include <gl\gl.h>
 #include <gl\glu.h>
 #include <gl\glaux.h>
 #include <time.h>

 #include "glGUI.h"
 #include "glScreens.h"

 class Cube {
     bool isThrowing;
     glButton *btnStart;
     GLuint cube_list;
     int result;
     clock_t start;
     glWindow *glwin;
     GameStatus *gstatus;
     
     int rotations; // pocet otoceni kocky
     int act_rots;
 public:
     Cube(glWindow *glwin, GameStatus *gstatus);
     ~Cube();
    
     /* inicializuje hod kockou */
     void initCube();
     /* zacne hod kockou, zastavi sa automaticky */
     void startThrow();
     void stopThrow(); /* zastavi kocku */
    
     // prekresli kocku, pricom button START sa zobrazi iba ak
     // isThrowing == false a zaroven canThrow == true
     void redrawCube(bool& canThrow, bool& wasThrowed, bool isFigureMoving);
     bool UpdatePosition(int x, int y, bool isClicked);
    
     /* zisti ci sa kocka toci */
     bool getIsThrowing() { return this->isThrowing; }
     int getResult() { return this->result; }
 };

#endif
