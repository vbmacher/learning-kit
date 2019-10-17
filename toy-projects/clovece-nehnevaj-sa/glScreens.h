/*
 *  TECHNICKÁ UNIVERZITA KOŠICE
 *  FAKULTA ELETKROTECHNIKY A INFORMATIKY
 *  KATEDRA POÈÍTAÈOV
 * ---------------------------------------
 *  Èloveèe, nehnevaj sa !
 * ---------------------------------------
 *  glScreens.h
 *
 * (c) Copyright 2006, P. Jakubèo, E.Danková, O.Drusa
 *
 */
 
 #ifndef __GLSCREENS_H__
 #define __GLSCREENS_H__
 
 #include <gl\gl.h>
 #include <gl\glu.h>
 #include <gl\glaux.h>

 #include "glGUI.h"
 #include "profile.h"

 typedef enum {
        UNKNOWN,
        LOGO,
        INTRO_MENU,
        GAME_SELECT,
        USERS_SELECT,
        PROFILE_SELECT,
        INTELLIGENCE_SELECT,
        STATISTICS,
        GAME,
        GAME_OVER,
        CREDITS
 } GameScreen;
 
  typedef struct {
        double r;
        double g;
        double b;
 } color3f;

 typedef struct {
     int factor; /* ak je player user tak profileIndex, inak intelligence */
     bool computer;
 } playerType;

 class GameStatus;

 
 class CScreen {
       GameScreen nextScreen;
 protected:
       void setNextScreen(GameScreen ns) { this->nextScreen = ns; }
 public:
       CScreen():nextScreen(INTRO_MENU){};
       virtual ~CScreen() {}
       virtual void Redraw() = 0;
       virtual bool MouseChange(int x, int y, bool isLClicked, bool isRClicked) = 0;
       virtual void KeyDown(int key, int kchar) = 0;
       virtual void KeyUp(int key) = 0;
       virtual bool KeyPressed(int key) = 0;
       GameScreen getNextScreen() { return this->nextScreen; }
 };

 class CLogo: public CScreen {
       int logo;
       glWindow *gl_win;
       GLfloat alpha;
 public:
        CLogo(glWindow *glwin);
        ~CLogo();
        void Redraw();
        bool MouseChange(int x, int y, bool isLClicked, bool isRClicked);
        void KeyDown(int key, int kchar);
        void KeyUp(int key) {}
        bool KeyPressed(int key) { return false; }
 };

 class CIntroMenu: public CScreen {
       glMenu *mnuMenu;
   	   char **menu_labels;
   	   glLabel *lblHeading;
 public:
        CIntroMenu(glWindow *glwin);
        ~CIntroMenu();
        void Redraw();
        bool MouseChange(int x, int y, bool isLClicked, bool isRClicked);
        void KeyDown(int key, int kchar);
        void KeyUp(int key);
        bool KeyPressed(int key);
 };
 
 class CGameSelect: public CScreen {
       glLabel *lblHeading;
       glButton *btn4;
   	   glButton *btn6;
   	   GameStatus *gstatus;
 public:
        CGameSelect(glWindow *glwin, GameStatus *gstatus);
        ~CGameSelect();
        void Redraw();
        bool MouseChange(int x, int y, bool isLClicked, bool isRClicked);
        void KeyDown(int key, int kchar);
        void KeyUp(int key);
        bool KeyPressed(int key);
 };
 
 /*
 
 Typ pouzivatela:
     
     1. pouzivatel: PC   [Zmenit]
     2. pouzivatel: user [Zmenit]
     ...
     n. pouzivatel: PC   [Zmenit]
 
     [Spat]              [OK]
 */
 
 class CUsersSelect: public CScreen {
       glLabel *lblHeading;
       glLabel *lblUser[6]; // max 6 hracov
       glLabel *lblType[6]; // user/PC
       glButton *btnChange[6];
   	   glButton *btnOK;
   	   glButton *btnBack;
   	   GameStatus *gstatus;
 public:
        CUsersSelect(glWindow *glwin, GameStatus *gstatus);
        ~CUsersSelect();
        void Redraw();
        bool MouseChange(int x, int y, bool isLClicked, bool isRClicked);
        void KeyDown(int key, int kchar);
        void KeyUp(int key);
        bool KeyPressed(int key);
 };

/*
    Vyber profilu:
          
    Nick: [          ] [Zmenit]
    +-------------------------+
    | o    Profil 1           |
    |      Profil 2           |
    |      ...                |
    +-------------------------+
    [Spat]                 [OK]

*/
 class CProfileSelect: public CScreen {
       glLabel *lblHeading;
       glLabel *lblNick;
       glTextBox *txtNick;
       glButton *btnProfiles[MAX_PROFILES];
       glButton *btnChange;
       glButton *btnOK;
       glButton *btnBack;
       GameStatus *gstatus;
       int selectIndex;
       Profiles *profiles;
       int selectedProfile;
       glWindow *gl_win;
 public:
        CProfileSelect(glWindow *glwin, GameStatus *gstatus, Profiles *profiles);
        ~CProfileSelect();
        void Redraw();
        bool MouseChange(int x, int y, bool isLClicked, bool isRClicked);
        void KeyDown(int key, int kchar);
        void KeyUp(int key);
        bool KeyPressed(int key);
 };
 
 /*
    Vyberte inteligenciu PC hracov:
            
            1. pouzivatel: 1. uroven  [Zmenit]
            3. pouzivatel: 2. uroven  [Zmenit]
    [Spat]                     [START]
 
 */
 
 class CIntelligenceSelect: public CScreen {
       glLabel *lblHeading;
       glLabel *lblUser[5]; // minimalne 1 musi byt user, preto max. PC hracov je 5
       glLabel *lblType[5]; // 1. uroven/2. uroven
       glButton *btnChange[5];
   	   glButton *btnOK;
   	   glButton *btnBack;
   	   GameStatus *gstatus;
       int usersIndex[5];
 public:
        CIntelligenceSelect(glWindow *glwin, GameStatus *gstatus);
        ~CIntelligenceSelect();
        void Redraw();
        bool MouseChange(int x, int y, bool isLClicked, bool isRClicked);
        void KeyDown(int key, int kchar);
        void KeyUp(int key);
        bool KeyPressed(int key);
 };

 /*
     Statistika
     
     Profil  |   Pocet hier | Vyhrane 4-hry | Vyhrane 6-hry | Uspech celkovo
 
     [Spat]
 
 */

 class CStatistics: public CScreen {
       glLabel *lblHeading;
       glLabel *lblTabHeading;
       glLabel *lblProfiles[MAX_PROFILES];
       glLabel *lblGameCount[MAX_PROFILES];
       glLabel *lblWin4[MAX_PROFILES];
       glLabel *lblWin6[MAX_PROFILES];
       glLabel *lblWin[MAX_PROFILES];
   	   glButton *btnBack;
   	   Profiles *profiles;
       glWindow *gl_win;
 public:
        CStatistics(glWindow *glwin, Profiles *profiles);
        ~CStatistics();
        void Redraw();
        bool MouseChange(int x, int y, bool isLClicked, bool isRClicked);
        void KeyDown(int key, int kchar);
        void KeyUp(int key);
        bool KeyPressed(int key);
 };

 class CGameOver: public CScreen {
       glLabel *lblHeading;
   	   glButton *btnBack;
   	   
   	   glLabel *lblWins[6];
   	   color3f colors[6];
   	   GLuint figure2D;
   	   
   	   GameStatus *gstatus;
   	   int winCount;
 public:
        CGameOver(glWindow *glwin, GameStatus *gstatus);
        ~CGameOver();
        void Redraw();
        bool MouseChange(int x, int y, bool isLClicked, bool isRClicked);
        void KeyDown(int key, int kchar);
        void KeyUp(int key);
        bool KeyPressed(int key);
 };

/*
    Credits
    
    (c) Coppyright 2006, vbmacher
    
    [Spat]

*/

 class CCredits: public CScreen {
       glLabel *lblHeading;
       glLabel *lblCopyright;
       glLabel *lblCopyright2;
       glLabel *lblCopyright3;
   	   glButton *btnBack;
 public:
        CCredits(glWindow *glwin);
        ~CCredits();
        void Redraw();
        bool MouseChange(int x, int y, bool isLClicked, bool isRClicked);
        void KeyDown(int key, int kchar);
        void KeyUp(int key);
        bool KeyPressed(int key);
 };

 class GameStatus {
     playerType players[6];  /* max. 6 hracov */
     int gameType; /* 0 - 4 hraci, 1 - 6 hracov*/
     Profiles *profiles;
        
     /* priebeh hry */
     int activePlayer;
     int throws[6];
     
     int homePlayers[6]; // sluzi na indikaciu kto vyhral, ked sa hrac dostane domov, zapisa sa do neho
     int winIndex;       // winIndex a inkrementuje sa
 public:
     GameStatus(Profiles *profiles);
     ~GameStatus();

     /* GAME_SELECT */
     void setGameType(int type);
     int getGamePlayers();

     /* USERS_SELECT */
     void setUserType(int playerIndex, bool computer);
     bool getUserType(int playerIndex);

     /* PROFILE_SELECT, INTELLIGENCE_SELECT */
     void setFactor(int playerIndex, int factor);
     int getFactor(int playerIndex);
     
     /* GAME */
     void startGame();
     void stopGame();
     int getActivePlayer() { return this->activePlayer; }
     int getThrows(int player) { return this->throws[player]; }
     bool updateThrows();
     void resetThrows(int throws) { this->throws[activePlayer] = throws; }
     char *getActiveNick();
     char *getNick(int player);
     void setPlayerHome(int player);
     int getPlayerHome(int player);

 };
 
 #endif
 
