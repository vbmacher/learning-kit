/*
 *  TECHNICKÁ UNIVERZITA KOŠICE
 *  FAKULTA ELETKROTECHNIKY A INFORMATIKY
 *  KATEDRA POÈÍTAÈOV
 * ---------------------------------------
 *  Èloveèe, nehnevaj sa !
 * ---------------------------------------
 *  Cube.cpp
 *
 * (c) Copyright 2006, P. Jakubèo, E.Danková, O.Drusa
 *
 * Implementacia hodu kockou
 *
 */

#include "Cube.h"
#include "glTextures.h"

/* CUBE */

Cube::Cube(glWindow *glwin, GameStatus *gstatus):isThrowing(false),result(0),start((clock_t)0), glwin(glwin), gstatus(gstatus)
{
	float cx;
	float cy;
	GLuint loop;

	this->cube_list = glGenLists(6); // vytvori 6 kociek (displaylisty)
	for (loop = 0; loop < 6; loop++) {
		cx = float(loop % 3) / 3.0f;
		cy = float(loop / 3) / 3.0f;
		glNewList(this->cube_list + loop, GL_COMPILE);
			glBegin(GL_QUADS);
				glTexCoord2f(cx,1-cy-0.3333f);
				glVertex2i(0,0);
				glTexCoord2f(cx+0.3333f,1-cy-0.3333f);
				glVertex2i(42,0);
				glTexCoord2f(cx+0.3333f,1-cy);
				glVertex2i(42,42);
				glTexCoord2f(cx,1-cy);
				glVertex2i(0,42);
			glEnd();
		glEndList();
	}
	this->btnStart = new glButton(glwin,"Hodi kocku", glwin->getWidth()-150,52,1);
}

Cube::~Cube()
{
    glDeleteLists(this->cube_list, 6);
    delete this->btnStart;
}

/* inicializuje hod kockou */
void Cube::initCube()
{
    this->isThrowing = false;
    this->start = clock();
}

/* zacne hod kockou, zastavi sa automaticky */
void Cube::startThrow()
{
    this->isThrowing = true;
    srand(clock());
    this->rotations = 10; //50 + rand() % 50; /* 50-100 otoceni kocky */
    this->act_rots = 0;
    this->btnStart->UpdatePosition(0,0,false); // aby nesvietilo
}
    
void Cube::stopThrow()
{
    this->isThrowing = false;
}

void Cube::redrawCube(bool& canThrow, bool& wasThrowed, bool isFigureMoving)
{
    bool comp; // hraje UI ?
    
    if (this->isThrowing) {
        // posledna rotacia ?
        if (act_rots+1 >= this->rotations)
            this->isThrowing = false;
        if ((clock()-this->start)/(double)CLK_TCK  > 0.1) {
            act_rots++; // akoze rotovanie kocky
            this->start = clock();
            this->result =  rand() % 6;
        }
    }

    comp = gstatus->getUserType(gstatus->getActivePlayer());
    if ((this->isThrowing == false) && (canThrow == true) && !isFigureMoving) {
        if (!comp) {
            this->btnStart->Reposition(glwin->getWidth()-150,52);
            this->btnStart->Redraw();
        } else {
            // UI, to je jedno aka uroven - ked je moznost okamzite hod kocku
            this->initCube();
            this->startThrow();
            wasThrowed = true;
            canThrow = false;
        }
    }

	glDisable(GL_BLEND);
    glBindTexture(GL_TEXTURE_2D, texture[5]);
	glDisable(GL_DEPTH_TEST);		// Disables Depth Testing

	glPushMatrix();
	glLoadIdentity();
	
    glTranslated(glwin->getWidth()-100,10,0);
    
    if (this->isThrowing) {
    	glColor3f(1,0,0);
       	glBegin(GL_QUADS);
    		glVertex2i(-3,-3);
    		glVertex2i(45,-3);
    		glVertex2i(45,45);
    		glVertex2i(-3,45);
       	glEnd();
    }
	glEnable(GL_TEXTURE_2D);
	glColor3f(1,1,1);
    glCallList(this->cube_list+this->result);
	glPopMatrix();
	glEnable(GL_DEPTH_TEST);
   	glDisable(GL_TEXTURE_2D);
}

bool Cube::UpdatePosition(int x, int y, bool isClicked)
{
    if (this->isThrowing == false)
      if (this->btnStart->UpdatePosition(x,y, isClicked) && isClicked) {
          this->initCube();
          this->startThrow();
          return true;
      }
    return false;
}

