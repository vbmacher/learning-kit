/*
 *  TECHNICKÁ UNIVERZITA KOŠICE
 *  FAKULTA ELETKROTECHNIKY A INFORMATIKY
 *  KATEDRA POÈÍTAÈOV
 * ---------------------------------------
 *  Èloveèe, nehnevaj sa !
 * ---------------------------------------
 *  main.cpp
 *
 * (c) Copyright 2006, P. Jakubèo, E.Danková, O.Drusa
 *
 */

// potrebne kniznice
//#pragma comment (lib,"opengl32.lib")
//#pragma comment (lib,"glu32.lib")
//#pragma comment (lib,"glaux.lib")

#include <windows.h>    // Hlavièkový soubor pro Windows
#include <gl\gl.h>      // Hlavièkový soubor pro OpenGL32 knihovnu
#include <gl\glu.h>     // Hlavièkový soubor pro Glu32 knihovnu
#include <gl\glaux.h>   // Hlavièkový soubor pro Glaux knihovnu
#include <math.h>

#include "main.h"
#include "profile.h"
#include "glGUI.h"
#include "glTextures.h"
#include "glScreens.h"
#include "CGame.h"

bool active = TRUE;      // Ponese informaci o tom, zda je okno aktivní
Point MousePt;

glWindow *gl_win = NULL; // okno OpenGL
CScreen *Screen = NULL;
GameStatus *game = NULL; // hra
GameScreen screen_type = UNKNOWN;
Profiles *profiles = NULL;

int aChar;               // aktivny znak (ascii hodnota pre WM_KEY...)
int vkCode;
bool keyPressed;

void SetNextScreen()
{
   if (Screen == NULL) {
       Screen = new CIntroMenu(gl_win);
       screen_type = INTRO_MENU;
       return;
   }
   if (Screen->getNextScreen() != screen_type) {
       switch(Screen->getNextScreen()) {
           case LOGO:
               delete Screen;
               Screen = new CLogo(gl_win);
               screen_type = LOGO;
               break;
           case INTRO_MENU:
               delete Screen;
               Screen = new CIntroMenu(gl_win);
               screen_type = INTRO_MENU;
               break;
           case GAME_SELECT:
               delete Screen;
          	   Screen = new CGameSelect(gl_win,game);
          	   screen_type = GAME_SELECT;
          	   break;
           case USERS_SELECT:
               delete Screen;
               Screen = new CUsersSelect(gl_win, game);
           	   screen_type = USERS_SELECT;
          	   break;
           case PROFILE_SELECT:
               delete Screen;
               Screen = new CProfileSelect(gl_win, game, profiles);
          	   screen_type = PROFILE_SELECT;
           	   break;
           case INTELLIGENCE_SELECT:
               delete Screen;
               Screen = new CIntelligenceSelect(gl_win, game);
               screen_type = INTELLIGENCE_SELECT;
               break;
           case STATISTICS:
               delete Screen;
               Screen = new CStatistics(gl_win, profiles);
               screen_type = STATISTICS;
               break;
           case GAME:
               delete Screen;
               Screen = new CGame(gl_win, game);
               screen_type = GAME;
               break;
           case GAME_OVER:
               delete Screen;
               Screen = new CGameOver(gl_win, game);
               screen_type = GAME_OVER;
               break;
           case CREDITS:
               delete Screen;
               Screen = new CCredits(gl_win);
               screen_type = CREDITS;
               break;
           default:
               break;
       }
   }
}

LRESULT CALLBACK WndProc(HWND hWnd, UINT uMsg, WPARAM wParam, LPARAM lParam)
{
	switch (uMsg) {
        case WM_CHAR:
            aChar = wParam;
            if (keyPressed) {
              Screen->KeyDown(vkCode, aChar);
              SetNextScreen();
            }       
            return 0;
		case WM_ACTIVATE:
			if (!HIWORD(wParam)) active=TRUE;
			else active=FALSE;
			return 0;
		case WM_SYSCOMMAND:
			switch (wParam) {
				case SC_SCREENSAVE:					// Screensaver Trying To Start?
				case SC_MONITORPOWER:				// Monitor Trying To Enter Powersave?
				return 0;							// Prevent From Happening
			}
			return 0;									// Exit
		case WM_CLOSE:
			PostQuitMessage(0);						// Send A Quit Message
			return 0;								// Jump Back
		case WM_KEYDOWN:
            vkCode = wParam;
            keyPressed = true;
            if (vkCode == VK_LEFT || vkCode == VK_RIGHT || vkCode == VK_HOME || vkCode == VK_END)
                Screen->KeyDown(vkCode, 0);
			return 0;								// Jump Back
		case WM_KEYUP:
            Screen->KeyUp(wParam);
			return 0;								// Jump Back
		case WM_SIZE:
			gl_win->resizeWindow(LOWORD(lParam),HIWORD(lParam));  // LoWord=Width, HiWord=Height
			return 0;								// Jump Back
		case WM_MOUSEMOVE:
            Screen->MouseChange(LOWORD(lParam), HIWORD(lParam), 
                 (LOWORD(wParam) & MK_LBUTTON) ? true : false,
                  (LOWORD(wParam) & MK_RBUTTON) ? true : false);
			return 0;
		case WM_LBUTTONUP:	// Uvolnìní levého tlaèítka
	        Screen->MouseChange(LOWORD(lParam), HIWORD(lParam), false,
                  (LOWORD(wParam) & MK_RBUTTON) ? true : false);
    		return 0;
		case WM_RBUTTONUP:	// Uvolnìní pravého tlaèítka
	        Screen->MouseChange(LOWORD(lParam), HIWORD(lParam),
                  (LOWORD(wParam) & MK_LBUTTON) ? true : false, false);
			return 0;
		case WM_LBUTTONDOWN:// Kliknutí levým tlaèítkem
	        Screen->MouseChange(LOWORD(lParam), HIWORD(lParam), true,
                  (LOWORD(wParam) & MK_RBUTTON) ? true : false);
    	    SetNextScreen();
			return 0;
		case WM_RBUTTONDOWN:// Kliknutí pravým tlaèítkem
	        Screen->MouseChange(LOWORD(lParam), HIWORD(lParam),
                  (LOWORD(wParam) & MK_LBUTTON) ? true : false, true);
			return 0;
	}
	// Pass All Unhandled Messages To DefWindowProc
	return DefWindowProc(hWnd,uMsg,wParam,lParam);
}

int WINAPI WinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance, LPSTR lpCmdLine, int nCmdShow)
{
	MSG msg;			// Windows Message Structure
	BOOL done = FALSE;  // Bool Variable To Exit Loop

	gl_win = new glWindow(true, 32);
	gl_win->createWindow("Èloveèe, nehnevaj sa!",800,600, GL_ORTHO);

	if (gl_win->SUCCESS == false) {
		delete gl_win;
		return 0;
	}
	if (!texLoadTextures()) {
		delete gl_win;
		MessageBox(NULL,"Nepodarilo sa naèíta textúry !","CHYBA",MB_OK|MB_ICONEXCLAMATION);
		return 0;
	}
	profiles = new Profiles();
	game = new GameStatus(profiles);
	Screen = new CLogo(gl_win);
	screen_type = LOGO;

    keyPressed = false;
	while(!done) {
		if (PeekMessage(&msg,NULL,0,0,PM_REMOVE)) {
			if (msg.message==WM_QUIT)				// Have We Received A Quit Message?
				done=TRUE;							// If So done=TRUE
            else {									// If Not, Deal With Window Messages 
				TranslateMessage(&msg);				// Translate The Message
				DispatchMessage(&msg);				// Dispatch The Message
		    }
		} else {
			// Draw The Scene.  Watch For ESC Key And Quit Messages From DrawGLScene()
			if (active) {
				if (Screen->KeyPressed(VK_ESCAPE)) {
                    if (screen_type == INTRO_MENU)
                        done=TRUE;
                    else {
                        delete Screen;
                        Screen = new CIntroMenu(gl_win);
                        screen_type = INTRO_MENU;
                    }
                } else {
                	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);// Smaže obrazovku a hloubkový buffer
                	glLoadIdentity();// Reset matice
                	Screen->Redraw();
					SwapBuffers(gl_win->getHDC());	// Swap Buffers (Double Buffering)
				}
			}
		}
	}

	// Shutdown
	delete game;
	delete Screen;
	delete profiles;
	delete gl_win;
	deleteFont();
	return (msg.wParam);							// Exit The Program
}
