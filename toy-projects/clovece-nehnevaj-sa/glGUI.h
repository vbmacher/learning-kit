/*
 *  TECHNICKÁ UNIVERZITA KOŠICE
 *  FAKULTA ELETKROTECHNIKY A INFORMATIKY
 *  KATEDRA POÈÍTAÈOV
 * ---------------------------------------
 *  Èloveèe, nehnevaj sa !
 * ---------------------------------------
 *  glGUI.h
 *
 * (c) Copyright 2006, P. Jakubèo, E.Danková, O.Drusa
 *
 */

#ifndef __GLGUI_H__
#define __GLGUI_H__

#include <gl\gl.h>
#include <gl\glu.h>
#include <gl\glaux.h>
#include <time.h>

#include "main.h"

#define GL_ORTHO 1
#define GL_PERSPECTIVE 2

// pre textboxy
#define CURSOR_PERIOD 0.4

class glWindow {
private:
	bool fullscreen;
	int depth_bits;      // bitova hlbka (8, 16, 24, 32)
	HDC hDC;             // Privatny GDI Device Context
	HGLRC hRC;           // Trvaly Rendering Context (OpenGL)
	HWND hWnd;           // handle okna
	HINSTANCE hInstance; // instancia aplikacie

    Size window_size;    // rozmery okna
    bool ortho_window;

	void destroyWindow(void);
	void initWindow(void);

	void resize3D(int width, int height);
	void resizeOrtho(int width, int height);
public:
	bool SUCCESS; // po kazdej operacii sa zmeni bud na true alebo false
	HDC getHDC() { return this->hDC; }
	HWND gethWnd() { return this->hWnd; }

	void createWindow(char* title, int width, int height, bool ortho);
	void resizeWindow(int width, int height);
	
	void setWindowType(bool ortho) { this->ortho_window = ortho; }

	int getWidth() { return this->window_size.width; }
	int getHeight() { return this->window_size.height; }

	glWindow(bool fullscreen, int depth_bits);
	~glWindow();
};


/* cisto virtualna trieda - predok vsetkych ovladacich prvkov */
class glControl {
protected:
	glWindow *gl_win;
    bool keys[256];          // Pole pre ukladanie vstupu z klavesnice
    bool isClicked;  // ci bolo kliknute mysou
    int activeChar;
public:
	bool SUCCESS; /* po kazdej operacii sa zmeni bud na true alebo false */
    glControl(glWindow *glwin);
    virtual ~glControl() {}
    // pri zmene velkosti okna zmeni prislusne suradnice a velkosti
    virtual bool UpdatePosition(int x, int y, bool isClicked) = 0;
    virtual void Redraw() = 0;
    virtual void KeyDown(int key, int kchar) { keys[key] = true; activeChar = kchar; }
    virtual void KeyUp(int key) { keys[key] = false;activeChar = 0; }
    bool KeyPressed(int key) { return keys[key]; }
    char getActiveChar(int key) { return activeChar; }
};

/* Trieda na vypis textov (obycajny 'label') */
class glLabel: public virtual glControl {
private:
    GLuint base;  // cislo zakladneho display listu znakov
    GLfloat r;
    GLfloat g;
    GLfloat b;
    int font_set;
protected:
    Point position;
    Size size;
    char text[256];
public:
	glLabel(glWindow *glwin, char *text, int x, int y, GLfloat r, GLfloat g, GLfloat b, int font_set);
	~glLabel() {}
	int getWidth() { return this->size.width; }
	int getHeight() { return this->size.height; }
	int getX() { return this->position.x; }
	int getY() { return this->position.y; }
	
    bool UpdatePosition(int x, int y, bool isClicked) {};
    void setText(char *new_text);
	void getText(char *where);
	void Redraw();
	void Reposition(int x, int y) { this->position.x = x; this->position.y = y; }
	void setColor(GLfloat r, GLfloat g, GLfloat b);
};

// trieda by sa mala pouzivat v projekcii Ortho
// ide o ovladaci prvok Button
class glButton: public virtual glControl, public glLabel {
	bool mouseOver;
public:
	glButton(glWindow *glwin, char *text, int x, int y, int font_set);
	~glButton() {}
	bool UpdatePosition(int x, int y, bool isClicked);
	void Redraw();
};

// ovladaci prvok Textbox
class glTextBox: public virtual glControl, public glLabel {
    GLuint border;  // displaylist ramceka textboxu
    bool showedCursor;
    clock_t seconds;
    int textPos;
public:
	glTextBox(glWindow *glwin, int x, int y);
	~glTextBox();
	bool UpdatePosition(int x, int y, bool isClicked) {}
    void setText(char *new_text);
	void Redraw();
    void KeyDown(int key, int kchar);
    void KeyUp(int key);
};

// vertikalne menu o max. 5-tich prvkoch
class glMenu: public virtual glControl {
      int count;
      glButton *buttons[5];
      PointSize Cposition;
      int clickedIndex;
public:
      glMenu(glWindow *glwin, char *labels[], int count, int x, int y);
      ~glMenu();
      int getWidth();
      int getHeight();
	  bool UpdatePosition(int x, int y, bool isClicked);
	  void Redraw();
	  int getClickedIndex() { return this->clickedIndex; }
};


#endif
