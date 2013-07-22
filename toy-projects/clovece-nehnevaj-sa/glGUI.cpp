/*
 *  TECHNICKÁ UNIVERZITA KOŠICE
 *  FAKULTA ELETKROTECHNIKY A INFORMATIKY
 *  KATEDRA POÈÍTAÈOV
 * ---------------------------------------
 *  Èloveèe, nehnevaj sa !
 * ---------------------------------------
 *  glGUI.cpp
 *
 * (c) Copyright 2006, P. Jakubèo, E.Danková, O.Drusa
 *
 * oknove operacie, inicializacia OpenGL, projekcie
 *
 */

// potrebne kniznice
#pragma comment (lib,"opengl32.lib")
#pragma comment (lib,"glu32.lib")
#pragma comment (lib,"glaux.lib")

#include <windows.h>
#include <gl\gl.h>      // Hlavickovy subor pre OpenGL32
#include <gl\glu.h>     // Hlavickovy subor pre Glu32
#include <gl\glaux.h>   // Hlavickovy subor pre Glaux

#include "glGUI.h"
#include "main.h"

void glWindow::destroyWindow(void)
{
	if (this->fullscreen) {
		ChangeDisplaySettings(NULL,0);// If So Switch Back To The Desktop
	//	ShowCursor(TRUE);			// Show Mouse Pointer
	}
	if (this->hRC) {
		if (!wglMakeCurrent(NULL,NULL))
			MessageBox(NULL,"Uvo¾nenie DC a RC zlyhalo.","CHYBA VYPÍNANIA",MB_OK | MB_ICONINFORMATION);
		if (!wglDeleteContext(this->hRC))
			MessageBox(NULL,"Release Rendering Context Failed.","CHYBA VYPÍNANIA",MB_OK | MB_ICONINFORMATION);
		this->hRC=NULL;
	}
	if (this->hDC && !ReleaseDC(this->hWnd,this->hDC)) {
		MessageBox(NULL,"Uvo¾nenie Device Context zlyhalo.","CHYBA VYPÍNANIA",MB_OK | MB_ICONINFORMATION);
		this->hDC=NULL;
	}
	if (this->hWnd && !DestroyWindow(this->hWnd)) {
		MessageBox(NULL,"Nemôžem uvo¾ni hWnd.","CHYBA VYPÍNANIA",MB_OK | MB_ICONINFORMATION);
		this->hWnd=NULL;
	}
	if (!UnregisterClass("Clovece",this->hInstance)) {
		MessageBox(NULL,"Nemôžem odregistrova triedu okna.","CHYBA VYPÍNANIA",MB_OK | MB_ICONINFORMATION);
		this->hInstance=NULL;
	}
	this->SUCCESS = true;
}

void glWindow::initWindow(void)
{
    this->SUCCESS = false;
	glShadeModel(GL_SMOOTH);
	glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	glClearDepth(1.0f);
	glEnable(GL_DEPTH_TEST);
    glDepthFunc(GL_LEQUAL);
    glEnable(GL_ALPHA_TEST);

    glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
    glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
    glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); 
	glBlendFunc(GL_SRC_ALPHA,GL_ONE);					// Select The Type Of Blending
	glShadeModel(GL_SMOOTH);							// Enables Smooth Color Shading
	glEnable(GL_TEXTURE_2D);							// Enable 2D Texture Mapping
	this->SUCCESS = true;
}

/*	This Code Creates Our OpenGL Window.  Parameters Are:					*
 *	title			- Title To Appear At The Top Of The Window				*
 *	width			- Width Of The GL Window Or Fullscreen Mode				*
 *	height			- Height Of The GL Window Or Fullscreen Mode			*
 *	bits			- Number Of Bits To Use For Color (8/16/24/32)			*/

void glWindow::createWindow(char* title, int width, int height, bool ortho)
{
	GLuint PixelFormat; // Holds The Results After Searching For A Match
	WNDCLASS wc;		// Windows Class Structure
	DWORD dwExStyle;	// Window Extended Style
	DWORD dwStyle;		// Window Style
	RECT WindowRect;	// Grabs Rectangle Upper Left / Lower Right Values

	this->window_size.width = width;
	this->window_size.height = height;
	WindowRect.left=(long)0; // Set Left Value To 0
	WindowRect.right=(long)width; // Set Right Value To Requested Width
	WindowRect.top=(long)0;	// Set Top Value To 0
	WindowRect.bottom=(long)height;	// Set Bottom Value To Requested Height
		
	this->hInstance = GetModuleHandle(NULL); // Grab An Instance For Our Window
	wc.style = CS_HREDRAW | CS_VREDRAW | CS_OWNDC;	// Redraw On Size, And Own DC For Window.
	wc.lpfnWndProc = (WNDPROC) WndProc; // WndProc Handles Messages
	wc.cbClsExtra = 0; // No Extra Window Data
	wc.cbWndExtra = 0; // No Extra Window Data
	wc.hInstance = hInstance; // Set The Instance
	wc.hIcon = LoadIcon(NULL, IDI_WINLOGO); // Load The Default Icon
	wc.hCursor = LoadCursor(NULL, IDC_ARROW); // Load The Arrow Pointer
	wc.hbrBackground = NULL; // No Background Required For GL
	wc.lpszMenuName = NULL; // We Don't Want A Menu
	wc.lpszClassName = "Clovece"; // Set The Class Name

	// Attempt To Register The Window Class
	if (!RegisterClass(&wc)) {
		MessageBox(NULL,"Nepodarilo sa registrova triedu okna.","CHYBA",MB_OK|MB_ICONEXCLAMATION);
		this->SUCCESS = false; return;
	}
	if (this->fullscreen) {
		DEVMODE dmScreenSettings; // Device Mode
		memset(&dmScreenSettings,0,sizeof(dmScreenSettings)); // Makes Sure Memory's Cleared
		dmScreenSettings.dmSize = sizeof(dmScreenSettings); // Size Of The Devmode Structure
		dmScreenSettings.dmPelsWidth = width; // Selected Screen Width
		dmScreenSettings.dmPelsHeight = height; // Selected Screen Height
		dmScreenSettings.dmBitsPerPel = depth_bits; // Selected Bits Per Pixel
		dmScreenSettings.dmFields=DM_BITSPERPEL|DM_PELSWIDTH|DM_PELSHEIGHT;

		// Try To Set Selected Mode And Get Results.  NOTE: CDS_FULLSCREEN Gets Rid Of Start Bar.
		if (ChangeDisplaySettings(&dmScreenSettings,CDS_FULLSCREEN)!=DISP_CHANGE_SUCCESSFUL) {
			if (MessageBox(NULL,"Požadovaný mód celej obrazovky nie je podporovaný\nvašim video adaptérom. Chcete použi okenný mód ?","Èloveèe, nehnevaj sa",MB_YESNO|MB_ICONEXCLAMATION)==IDYES)
				this->fullscreen = false;
			else {
				this->SUCCESS = false; return;
			}
		}
	}
	// este stale je fullscreen ?
	if (this->fullscreen)	{
		dwExStyle=WS_EX_APPWINDOW;	// Window Extended Style
		dwStyle=WS_POPUP;			// Windows Style
//		ShowCursor(FALSE);			// Hide Mouse Pointer
	} else {
		dwExStyle=WS_EX_APPWINDOW | WS_EX_WINDOWEDGE;	// Window Extended Style
		dwStyle=WS_OVERLAPPEDWINDOW;					// Windows Style
	}
	// Adjust Window To True Requested Size
	AdjustWindowRectEx(&WindowRect, dwStyle, FALSE, dwExStyle);
	if (!(hWnd=CreateWindowEx(dwExStyle,							// Extended Style For The Window
			"Clovece",						// Class Name
			title,							// Window Title
			dwStyle |						// Defined Window Style
			WS_CLIPSIBLINGS |				// Required Window Style
			WS_CLIPCHILDREN,				// Required Window Style
			0, 0,							// Window Position
			WindowRect.right-WindowRect.left,	// Calculate Window Width
			WindowRect.bottom-WindowRect.top,	// Calculate Window Height
			NULL,							// No Parent Window
			NULL,							// No Menu
			hInstance,						// Instance
			NULL)))							// Dont Pass Anything To WM_CREATE
	{
		this->destroyWindow();	// Reset The Display
		MessageBox(NULL,"Nepodarilo sa vytvori okno.","CHYBA",MB_OK|MB_ICONEXCLAMATION);
		this->SUCCESS = false; return;
	}
	static PIXELFORMATDESCRIPTOR pfd =	{ // pfd Tells Windows How We Want Things To Be
		sizeof(PIXELFORMATDESCRIPTOR),	// Size Of This Pixel Format Descriptor
		1,								// Version Number
		PFD_DRAW_TO_WINDOW |			// Format Must Support Window
		PFD_SUPPORT_OPENGL |			// Format Must Support OpenGL
		PFD_DOUBLEBUFFER,				// Must Support Double Buffering
		PFD_TYPE_RGBA,					// Request An RGBA Format
		depth_bits,							// Select Our Color Depth
		0, 0, 0, 0, 0, 0,				// Color Bits Ignored
		0,								// No Alpha Buffer
		0,								// Shift Bit Ignored
		0,								// No Accumulation Buffer
		0, 0, 0, 0,						// Accumulation Bits Ignored
		16,								// 16Bit Z-Buffer (Depth Buffer)  
		0,								// No Stencil Buffer
		0,								// No Auxiliary Buffer
		PFD_MAIN_PLANE,					// Main Drawing Layer
		0,								// Reserved
		0, 0, 0							// Layer Masks Ignored
	};
	if (!(hDC=GetDC(hWnd)))	{
		this->destroyWindow();	// Reset The Display
		MessageBox(NULL,"Nemôžem vytvori GL Device Context.","CHYBA",MB_OK|MB_ICONEXCLAMATION);
		this->SUCCESS = false; return;
	}
	if (!(PixelFormat=ChoosePixelFormat(hDC,&pfd)))	{
		this->destroyWindow();	// Reset The Display
		MessageBox(NULL,"Nemôžem nájs vhodný pixelformat.","CHYBA",MB_OK|MB_ICONEXCLAMATION);
		this->SUCCESS = false; return;
	}
	if(!SetPixelFormat(hDC,PixelFormat,&pfd)) {
		this->destroyWindow();	// Reset The Display
		MessageBox(NULL,"Nemôžem nastavi pixelformat.","CHYBA",MB_OK|MB_ICONEXCLAMATION);
		this->SUCCESS = false; return;
	}
	if (!(hRC=wglCreateContext(hDC))) {
		this->destroyWindow();	// Reset The Display
		MessageBox(NULL,"Nemôžem vytvori GL Rendering Context.","CHYBA",MB_OK|MB_ICONEXCLAMATION);
		this->SUCCESS = false; return;
	}
	if(!wglMakeCurrent(hDC,hRC)) {
		this->destroyWindow();	// Reset The Display
		MessageBox(NULL,"Nemôžem aktivova GL Rendering Context.","CHYBA",MB_OK|MB_ICONEXCLAMATION);
		this->SUCCESS = false; return;
	}

	ShowWindow(this->hWnd,SW_SHOW); // Show The Window
	SetForegroundWindow(this->hWnd);	// Slightly Higher Priority
	SetFocus(this->hWnd);				// Sets Keyboard Focus To The Window
	if (ortho)
	  this->resizeOrtho(width, height);
	else
	  this->resize3D(width, height);

	// Initialize Our Newly Created GL Window
	this->initWindow();
	if (!this->SUCCESS) {
		this->destroyWindow();	// Reset The Display
		MessageBox(NULL,"Inicializácia zlyhala.","CHYBA",MB_OK|MB_ICONEXCLAMATION);
		this->SUCCESS = false; return;
	}
	this->SUCCESS = true;
}

void glWindow::resize3D(int width, int height)
{
    this->SUCCESS = false;
    if (!height) height=1;

	this->window_size.width = width;
	this->window_size.height = height;

	// viewport bude cele okno
	glViewport(0,0,width,height);
	// Select The Projection Matrix
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	// uhol 45° v smere y, pomer x/y, najblizsia rovina orezu, najvzdialenejsia rovina orezu
	gluPerspective(45.0f,(GLfloat)width/(GLfloat)height,0.1f,5000.0f);
	// Select The Modelview Matrix
	glMatrixMode(GL_MODELVIEW);	
	glLoadIdentity();
	this->SUCCESS = true;
}

void glWindow::resizeOrtho(int width, int height)
{
    this->SUCCESS = false;
	if (!height) height=1;

	this->window_size.width = width;
	this->window_size.height = height;

	// viewport bude cele okno
	glViewport(0,0,width,height);
	// Select The Projection Matrix
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	// kolma projekcia
    glOrtho(0.0f,(GLdouble)width,0.0f,(GLdouble)height,-1.0f,1.0f);
	glMatrixMode(GL_MODELVIEW);	
	glLoadIdentity();
	this->SUCCESS = true;
}

void glWindow::resizeWindow(int width, int height)
{
    if (this->ortho_window)
        this->resizeOrtho(width, height);
    else
        this->resize3D(width, height);
}

glWindow::glWindow(bool fullscreen, int bits) :
                        fullscreen(fullscreen), hDC(NULL),
                        hRC(NULL), hWnd(NULL), hInstance(NULL),
                        depth_bits(bits)
{
    this->window_size.width = this->window_size.height = 0;
}

glWindow::~glWindow()
{
	this->destroyWindow();
}

