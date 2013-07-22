/*
 *  TECHNICKÁ UNIVERZITA KOŠICE
 *  FAKULTA ELETKROTECHNIKY A INFORMATIKY
 *  KATEDRA POÈÍTAÈOV
 * ---------------------------------------
 *  Èloveèe, nehnevaj sa !
 * ---------------------------------------
 *  GLControls.cpp
 *
 * (c) Copyright 2006, P. Jakubèo, E.Danková, O.Drusa
 *
 * Implementacia GUI
 */

// potrebne kniznice
#pragma comment (lib,"opengl32.lib")
#pragma comment (lib,"glu32.lib")
#pragma comment (lib,"glaux.lib")

#include <windows.h>
#include <gl\gl.h>      // Hlavièkový soubor pro OpenGL32 knihovnu
#include <gl\glu.h>     // Hlavièkový soubor pro Glu32 knihovnu
#include <gl\glaux.h>   // Hlavièkový soubor pro Glaux knihovnu
#include <time.h>

#include "glTextures.h"
#include "glGUI.h"

glControl::glControl(glWindow *glwin): gl_win(glwin)
{
    for (int i = 0; i < 256; i++)
        this->keys[i] = false;
}

/* LABEL */
glLabel::glLabel(glWindow *glwin, char *text, int x, int y,GLfloat r, GLfloat g, GLfloat b, int font_set)
 : glControl(glwin),r(r),g(g),b(b),font_set(font_set)
{
    strcpy(this->text, text);

    this->position.x = x;
    this->position.y = y;

	// pri loadovani sceny ma pre akykolvek rozmer okna font vysku 16 pixelov a sirku podobne
	size.width = (strlen(text) * font_space);
	size.height = font_height;
}

void glLabel::Redraw()
{
	glEnable(GL_TEXTURE_2D);
	glEnable(GL_BLEND);
	if (!font_set) glBindTexture(GL_TEXTURE_2D, texture[0]);
	else glBindTexture(GL_TEXTURE_2D, texture[4]);
	glDisable(GL_DEPTH_TEST);
	glPushMatrix();
	glLoadIdentity();
	glTranslated(position.x,position.y,0);
	glColor3f(r,g,b);
	glListBase(font_base);
	glCallLists(strlen(this->text),GL_UNSIGNED_BYTE,this->text);
	glPopMatrix();
	glEnable(GL_DEPTH_TEST);
	glDisable(GL_TEXTURE_2D);
	glDisable(GL_BLEND);
}

void glLabel::setColor(GLfloat r, GLfloat g, GLfloat b)
{
     this->r = r;
     this->g = g;
     this->b = b;
}

void glLabel::setText(char *new_text)
{
    memset(this->text, 0, 256);
    strcpy(this->text, new_text);
  	// pri loadovani sceny ma pre akykolvek rozmer okna font vysku 16 pixelov a sirku podobne
	this->size.width = (strlen(this->text) * font_space);
	this->size.height = font_height;
}

void glLabel::getText(char *where)
{
    strcpy(where, this->text);
}

/* textButton */
glButton::glButton(glWindow *glwin, char *text, int x, int y, int font_set)
: glControl(glwin), glLabel(glwin,text,x,y,1,0,0,font_set), mouseOver(false)
{}

// x a y urcuju poziciu mysky cez WinAPI
bool glButton::UpdatePosition(int x, int y, bool isClicked)
{
	bool p1, p2;
	
	this->isClicked = isClicked;
	y = this->gl_win->getHeight() - y;
    p1 = (x >= position.x && x <= (position.x + size.width));
	p2 = (y <= (position.y + size.height) && y >= position.y);

	if (p1 && p2)
		this->mouseOver = true;
	else
		this->mouseOver = false;
	return this->mouseOver;
}

void glButton::Redraw()
{
	if (this->mouseOver)
        setColor(1.0f,1.0f,0.0f);
	else
		setColor(1.0f,0.0f,0.0f);
	glLabel::Redraw();
}

/* glTextBox */

glTextBox::glTextBox(glWindow *glwin, int x, int y)
: glControl(glwin), glLabel(glwin, "", x, y,1,1,1,1), seconds((clock_t)0), textPos(0),showedCursor(false)
{ 
     int wW, wH;

     memset(this->text, 0, 256);
     /* textovy ramcek je o 5 sirsi a vyssi a je pre 10 znakov */
     wW = 110;
     wH = this->getHeight();

     this->border = glGenLists(1); // vytvori 1 border (display list)
     glNewList(this->border,GL_COMPILE);
         glBegin(GL_QUADS);
            glVertex2i(0, 0);
            glVertex2i(wW+10,0);
            glVertex2i(wW+10,1);
            glVertex2i(0,1);
    
            glVertex2i(wW+10, 0);
            glVertex2i(wW+10,wH+10);
            glVertex2i(wW-1+10,wH+10);
            glVertex2i(wW-1+10,0);
    
            glVertex2i(wW+10, wH+10);
            glVertex2i(0,wH+10);
            glVertex2i(0,wH+10+1);
            glVertex2i(wW+10,wH+10+1);
    
            glVertex2i(0, 0);
            glVertex2i(1,0);
            glVertex2i(1,wH+10);
            glVertex2i(0,wH+10);
        glEnd();
     glEndList();
}

glTextBox::~glTextBox()
{
    glDeleteLists(this->border, 1);
}

void glTextBox::Redraw()
{
    int wH;
    wH = this->getHeight();

	glLoadIdentity();
    glTranslated(position.x-5,position.y-5,0);

	glDisable(GL_TEXTURE_2D);
    glColor3f(0.5f,0.5f,1.0f);// Svìtle modrá barva
	glCallList(this->border);
    glEnable(GL_TEXTURE_2D);
 	glLabel::Redraw();
 	
    if ((clock()-this->seconds)/(double)CLK_TCK >= CURSOR_PERIOD) {
      this->showedCursor = !this->showedCursor;
      this->seconds = clock();
    }
 	if (this->showedCursor == true) {
        glLoadIdentity();
        glTranslated(position.x+this->textPos*font_space+5,position.y,0);
    	glDisable(GL_TEXTURE_2D);
        glColor3f(0.5f,0.5f,1.0f);// Svìtle modrá barva
        glBegin(GL_QUADS);
          glVertex2i(0,0);
          glVertex2i(1,0);
          glVertex2i(1,wH);
          glVertex2i(0,wH);
        glEnd();
    	glEnable(GL_TEXTURE_2D);
    }
}

void glTextBox::KeyDown(int key, int kchar)
{
    glControl::KeyDown(key, kchar);
    if ((key >= 'A' && key <= 'Z') || (key == ' ')) {
        if (this->textPos >= 10) return;
        this->text[this->textPos++] = kchar;
    } else if (key == 8 && this->textPos > 0) {
        this->text[this->textPos-1] = '\0';
        strcpy(this->text+this->textPos-1, this->text+this->textPos);
        this->textPos--;
    } else if (key == VK_END)
        this->textPos = strlen(this->text);
    else if (key == VK_HOME)
        this->textPos = 0;
    else if (key == VK_LEFT && this->textPos > 0)
        this->textPos--;
    else if (key == VK_RIGHT && this->textPos < strlen(this->text))
        this->textPos++;
}

void glTextBox::KeyUp(int key)
{
     glControl::KeyUp(key);
}

void glTextBox::setText(char *new_text)
{
    this->textPos = 0;
    glLabel::setText(new_text);
}

/* glMenu */

glMenu::glMenu(glWindow *glwin, char *labels[], int count, int x, int y)
: glControl(glwin), count(count)
{
    int i;
    int maxw_i = 0, height = 0;

    int ax = x;
    int ay = gl_win->getHeight() - y;
    for (i = 0; (i < count) && (i < 5); i++) {
        this->buttons[i] = new glButton(glwin, labels[i],ax, ay,1);
        ay -= this->buttons[i]->getHeight();
        if (this->buttons[i]->getWidth() > this->buttons[maxw_i]->getWidth()) maxw_i = i;
        height += this->buttons[i]->getHeight();
    }
    Cposition.x = x;
    Cposition.y = y;
    Cposition.width = maxw_i;
    Cposition.height = height;
}

glMenu::~glMenu()
{
    int i;
    for (i = 0; i < this->count; i++)
        delete this->buttons[i];
}

int glMenu::getWidth()
{
    return buttons[Cposition.width]->getWidth();
}

int glMenu::getHeight()
{
    return Cposition.height;
}

// x a y urcuju poziciu mysky cez WinAPI
bool glMenu::UpdatePosition(int x, int y, bool isClicked)
{
     int i;
     bool status = false;
     bool s;
     for (i = 0; i < this->count; i++) {
         s = buttons[i]->UpdatePosition(x,y,isClicked);
         status |= s;
         if (s && isClicked)
           this->clickedIndex = (i+1);
     }
     return status;
}

void glMenu::Redraw()
{
     int i;
     for (i = 0; i < this->count; i++)
         buttons[i]->Redraw();
}
