/*
 *  TECHNICKÁ UNIVERZITA KOŠICE
 *  FAKULTA ELETKROTECHNIKY A INFORMATIKY
 *  KATEDRA POÈÍTAÈOV
 * ---------------------------------------
 *  Èloveèe, nehnevaj sa !
 * ---------------------------------------
 *  CGame.cpp
 *
 * (c) Copyright 2006, P. Jakubèo, E.Danková, O.Drusa
 *
 * Implementacia samotnej hry
 *
 */

#include <math.h>
#include <time.h>

#include "glGUI.h"
#include "glScreens.h"
#include "glTextures.h"
#include "CGame.h"

/* GAME */

CGame::CGame(glWindow *glwin, GameStatus *gstatus) 
: gstatus(gstatus), gl_win(glwin), setDragStart(false), wasThrowed(false), isOut(false)
{
    int i,j,k;

    this->setNextScreen(GAME);
    gstatus->startGame();
    
    glwin->setWindowType(false);
    glwin->resizeWindow(glwin->getWidth(), glwin->getHeight());

    scenePosition.x = 0;
    scenePosition.y = 30;
    scenePosition.z = -390; //-350;
    
    oldScenePosition.x = 0;
    oldScenePosition.y = 0;
    oldScenePosition.z = 0;

    lblHeading = new glLabel(glwin, "Na ahu:", 10,10,1,1,1,0);
    lblHeading2 = new glLabel(glwin, "Poèet možností:", 10,15+lblHeading->getHeight(),1,1,1,0);
    lblNick = new glLabel(glwin, "nick",10+lblHeading2->getWidth(),10, 0.6,0.6,0,0);
    lblThrows = new glLabel(glwin, "3",10+lblHeading2->getWidth(),15+lblHeading->getHeight(),0.6,0.6,0,0);
    lblOut = new glLabel(glwin, "Èloveèe, nehnevaj sa !", glwin->getWidth()/2-11*font_space,20,1,0,0,1);

    this->floor = glGenLists(1);
	glNewList(this->floor, GL_COMPILE);
		glBegin(GL_QUADS);
			glTexCoord2f(1.0f, 1.0f);glVertex3f(128.0f,0.0f,128.0f);
			glTexCoord2f(0.0f, 1.0f);glVertex3f(-128.0f,0.0f,128.0f);
			glTexCoord2f(0.0f, 0.0f);glVertex3f(-128.0f,0.0f,-128.0f);
			glTexCoord2f(1.0f, 0.0f);glVertex3f(128.0f,0.0f,-128.0f);
		glEnd();
	glEndList();

  this->playerPosition = new Player(gstatus);
  cube = new Cube(glwin, gstatus);
  this->canThrow = this->playerPosition->prepareThrow();
  glClearColor(0,0.3,0,0);
}

CGame::~CGame()
{
    delete playerPosition;
    delete lblHeading;
    delete lblHeading2;
    delete lblThrows;
    delete lblNick;
    delete lblOut;
    delete cube;
}

void CGame::Redraw()
{
    static char n[10];
    int i;
    double phi;

    if (!this->cube->getIsThrowing() && this->wasThrowed)
        if (!playerPosition->canGo(cube->getResult()+1)) {
            // tu sa dostane ak skoncil hod kockou a hrac nemoze ist
            // toto nema implementaciu v MouseChange
            this->wasThrowed = false;
            this->canThrow = true;

            // ak hrac nemoze ist a hodil 6-tku tak ma pravo na dalsi hod
            if (cube->getResult() == 5) 
                gstatus->resetThrows(2);
            int i = 0;
            if (gstatus->updateThrows())
                while (!(this->canThrow = playerPosition->prepareThrow()) && (i < gstatus->getGamePlayers())) {
                    gstatus->updateThrows();
                    i++;
                }
            // koniec hry ??
            if (!this->canThrow) {
                this->cube->stopThrow();
                this->setNextScreen(GAME_OVER);
                PostMessage(gl_win->gethWnd(), WM_KEYDOWN, 27, 0);
            }
        }

    // Implementacia UI
    if (gstatus->getUserType(gstatus->getActivePlayer()))
        if (cube->getIsThrowing() == false && this->wasThrowed && !playerPosition->isMoving()) {
            wasThrowed = false;
            // ak bola 6-tka, ma pravo na este 1 hod
            if (cube->getResult() == 5) {
                gstatus->resetThrows(2); // bude update (cize sa hod odpocita)
                this->canThrow = true;
            } else {
                gstatus->resetThrows(1); // inak uz ide iny hrac
                this->canThrow = false;
            }

            // nasledujuca metoda podla urovne inteligencie PC hraca vyberie panaka
            if (playerPosition->UISelectPlayer(cube->getResult()+1))
                this->isOut = true;
            
            // a posledny krok: update hraca
            int i = 0;
            
            if (gstatus->updateThrows())
                while (!(this->canThrow = playerPosition->prepareThrow()) && (i < gstatus->getGamePlayers())) {
                    gstatus->updateThrows();
                    i++;
                }
            // koniec hry ??
            if (!this->canThrow) {
                this->cube->stopThrow();
                this->setNextScreen(GAME_OVER);
                PostMessage(gl_win->gethWnd(), WM_KEYDOWN, 27, 0);
            }
        }

    glTranslatef(0.0f, 20.0f, scenePosition.z);
    glRotatef(scenePosition.y, 1.0f, 0.0f, 0.0f);  /* rotacia sceny podla pohybu kurzoru mysi */
    glRotatef(scenePosition.x, 0.0f, 1.0f, 0.0f);

    glDisable(GL_BLEND);
    glColor3f(1,1,1);
   	glEnable(GL_TEXTURE_2D);
   	if (gstatus->getGamePlayers() == 6) glBindTexture(GL_TEXTURE_2D, texture[3]);
  	else glBindTexture(GL_TEXTURE_2D, texture[2]);
  	glCallList(this->floor);
   	glDisable(GL_TEXTURE_2D);

    playerPosition->RedrawPlayers(scenePosition);

    // ortho projekcia
	glMatrixMode(GL_PROJECTION);
	glPushMatrix();
	glLoadIdentity();
    glOrtho(0,(GLdouble)gl_win->getWidth(),0,(GLdouble)gl_win->getHeight(),-1,1);
	glMatrixMode(GL_MODELVIEW);
	glPushMatrix();					// Store The Modelview Matrix
	glLoadIdentity();				// Reset The Modelview Matrix

    // najprv pozadie - menu
    glColor3f(0,0,0);
    glBegin(GL_QUADS);
        glVertex3f(0,0,0);
        glVertex3f(gl_win->getWidth(),0,0);
        glVertex3f(gl_win->getWidth(),80,0);
        glVertex3f(0,80,0);
    glEnd();

    if (this->isOut && !playerPosition->isMoving()) {
        // nastal vyhadzov - hlaska "Clovece, nehnevaj sa !" na 1.2 sekundy
        clock_t t1;
        lblOut->Redraw();
        glFlush();
		SwapBuffers(gl_win->getHDC()); // zobraz hlasku
        t1 = clock();
        while (((clock()-t1) / (double)CLK_TCK) < 2.2) ;
		SwapBuffers(gl_win->getHDC()); // naspat buffer
        isOut = false;
    }

    // labely v ortho
    lblHeading->Redraw();
    lblHeading2->Redraw();

    memset(n, 0, 10);
    itoa(gstatus->getThrows(gstatus->getActivePlayer()),n,10);
    lblThrows->setText(n);
    lblThrows->Redraw();

    lblNick->setText(gstatus->getActiveNick());
    lblNick->Redraw();
    
    glDisable(GL_TEXTURE_2D);
    glDisable(GL_BLEND);
    glTranslatef(25+this->lblHeading->getWidth(),10,0);
    switch (gstatus->getActivePlayer()) {
        case 0:  // cierna
            glColor3f(0.2,0.2,0.2); break;
        case 1:  // zlta
            glColor3f(1.0,0.8,0.0); break;
        case 2:  // fialova(6) modra(4)
            if (gstatus->getGamePlayers() == 4) glColor3f(0.0,0.0,0.8);
            else glColor3f(1.0,0.0,0.8);
            break;
        case 3:  // zelena(6) cervena(4)
            if (gstatus->getGamePlayers() == 4) glColor3f(0.8,0.0,0.0);
            else glColor3f(0.0,0.8,0.0);
            break;
        case 4:  // cervena
            glColor3f(0.8,0.0,0.0); break;
        case 5:  // modra
            glColor3f(0.0,0.0,0.8); break;
    }

    // kreslenie 2D panaka kto je na tahu
    glBegin(GL_TRIANGLES);
        glVertex3f(0,0,0);
        glVertex3f(15,0,0);
        glVertex3f(7.5,16,0);
    glEnd();
    phi = 0;
    glBegin(GL_POLYGON);
        for (i = 0; i < 50; i++) {
        	glVertex3f(7.5 + cos(phi) * 6, 20 + sin(phi) * 6, 0);
        	phi += 2*M_PI/50;
        }
    glEnd();

    // prekreslenie kocky, ortho projekcia
    this->cube->redrawCube(this->canThrow, this->wasThrowed, playerPosition->isMoving());

    // spat na 3D projekciu
	glMatrixMode(GL_PROJECTION);
	glPopMatrix();
	glMatrixMode(GL_MODELVIEW);
	glPopMatrix();
}

bool CGame::MouseChange(int x, int y, bool isLClicked,  bool isRClicked)
{
    Vector3D ray;
    bool status = false;
    
    if (isRClicked == true) {
        if (setDragStart == false) {
            cursorPosition.x = x;
            cursorPosition.y = y;
            setDragStart = true;
        }
        scenePosition.x = oldScenePosition.x+x-cursorPosition.x;
        scenePosition.y = oldScenePosition.y+y-cursorPosition.y;
        scenePosition.y = scenePosition.y < 5 ? 5 : scenePosition.y;    /* Max rotation */
        scenePosition.y = scenePosition.y > 80 ? 80 : scenePosition.y;  /* Min rotation */    
    } else if (isLClicked == true) {
        // oznacenie panaka
        setDragStart = false;
        oldScenePosition.x = scenePosition.x;
        oldScenePosition.y = scenePosition.y;
    } else {
        setDragStart = false;
        oldScenePosition.x = scenePosition.x;
        oldScenePosition.y = scenePosition.y;
    }
    
    // implementacia dynamickej logiky
    if (!cube->getIsThrowing() && !playerPosition->isMoving()) {
        // implementaciu CHEATu - kliknut na kocku pojde vzdy ked sa netoci, cize
        // aj ked neni viditelna a tak si hrac moze zmenit hodnotu dalsim hodom ktory ho nic nestoji :-)
        if (this->cube->UpdatePosition(x,y,isLClicked) == true) {
            // bola zapnuta kocka
            this->wasThrowed = true;
            this->canThrow = false; // kym si nevyberie panaka
        } else if (this->wasThrowed) {
            // kontrola ci je mys na panakovi, iba ak bolo hodene kockou a niektory panak
            // moze ist - teda vyber panaka
            status = playerPosition->UpdatePosition(x,y, isLClicked, cube->getResult()+1);
            if (status) {
                wasThrowed = false;
                // ak bola 6-tka, ma pravo na este 1 hod
                if (cube->getResult() == 5) {
                    gstatus->resetThrows(2); // bude update (cize sa hod odpocita)
                    this->canThrow = true;
                } else {
                    gstatus->resetThrows(1); // inak uz ide iny hrac
                    this->canThrow = false;
                }
                if (playerPosition->goPlayer(this->cube->getResult()+1))
                    this->isOut = true;
                
                // a posledny krok: update hraca
                int i = 0;
                
                if (gstatus->updateThrows())
                    while (!(this->canThrow = playerPosition->prepareThrow()) && (i < gstatus->getGamePlayers())) {
                        gstatus->updateThrows();
                        i++;
                    }
                // koniec hry ??
                if (!this->canThrow)
                    this->setNextScreen(GAME_OVER);
            }
        }
    }
    return status;
}

void CGame::KeyDown(int key, int kchar)
{
    if (key == 27)
        this->setNextScreen(GAME_OVER);
    
    // cheat - posle vsetkych panakov domov
    if (kchar == 'H' || kchar == 'h') {
        int i,j;
        if (gstatus->getGamePlayers() == 6) j = 52;
        else j = 44;
        for (i = 0; i < 4; i++)
            playerPosition->setPlayerPosition(gstatus->getActivePlayer(),i,j+i);
    }
}

void CGame::KeyUp(int key)
{}

bool CGame::KeyPressed(int key)
{
     return false;
}
