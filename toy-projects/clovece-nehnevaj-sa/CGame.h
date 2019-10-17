/*
 *  TECHNICKÁ UNIVERZITA KOŠICE
 *  FAKULTA ELETKROTECHNIKY A INFORMATIKY
 *  KATEDRA POÈÍTAÈOV
 * ---------------------------------------
 *  Èloveèe, nehnevaj sa !
 * ---------------------------------------
 *  CGame.h
 *
 * (c) Copyright 2006, P. Jakubèo, E.Danková, O.Drusa
 *
 */

#ifndef __CGAME_H__
#define __CGAME_H__
 
 #include <time.h>
 
 #include "main.h"
 #include "glScreens.h"
 #include "Cube.h"
 #include "Player.h"

 class CGame: public CScreen {
        GameStatus *gstatus;
        glWindow *gl_win;
        GLuint floor;
        
        glLabel *lblHeading;
        glLabel *lblHeading2;
        glLabel *lblThrows;
        glLabel *lblNick;
        glLabel *lblOut;
        
        Point3D scenePosition;
        Point3D oldScenePosition;
        Point3D cursorPosition;
        bool setDragStart;

        Player *playerPosition;
        Cube *cube;
        bool canThrow; /* moze kockou hadzat ?*/
        bool wasThrowed; /* bolo uz kockou hodene ? */
        bool isOut; /* nastal vyhadzov ?? */
        
 public:
        CGame(glWindow *glwin, GameStatus *gstatus);
        ~CGame();
        void Redraw();
        bool MouseChange(int x, int y, bool isLClicked, bool isRClicked);
        void KeyDown(int key, int kchar);
        void KeyUp(int key);
        bool KeyPressed(int key);

 };

#endif
