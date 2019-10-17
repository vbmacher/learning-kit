/*
 *  TECHNICKÁ UNIVERZITA KOŠICE
 *  FAKULTA ELETKROTECHNIKY A INFORMATIKY
 *  KATEDRA POÈÍTAÈOV
 * ---------------------------------------
 *  Èloveèe, nehnevaj sa !
 * ---------------------------------------
 *  glScreens.cpp
 *
 * (c) Copyright 2006, P. Jakubèo, E.Danková, O.Drusa
 *
 * Implementacia jednotlivych obrazoviek
 *
 */
 
#include <math.h>

#include "glGUI.h"
#include "glScreens.h"
#include "glTextures.h"

/* labely pre intromenu */
char *ml[] = { "Nová hra",
               "Štatistiky hráèov",
               "O autoroch",
               "Koniec"
             };

char *users[] = { "1. hráè: ",
                  "2. hráè: ",
                  "3. hráè: ",
                  "4. hráè: ",
                  "5. hráè: ",
                  "6. hráè: "
                };

char *pheadings[] = { "Výber profilu 1. hráèa:",
                      "Výber profilu 2. hráèa:",
                      "Výber profilu 3. hráèa:",
                      "Výber profilu 4. hráèa:",
                      "Výber profilu 5. hráèa:",
                      "Výber profilu 6. hráèa:"
                    };

/* LOGO */

CLogo::CLogo(glWindow *glwin):gl_win(glwin), alpha(0.0)
{
    this->setNextScreen(LOGO);

    glEnable(GL_TEXTURE_2D);
    this->logo = glGenLists(1);
	glNewList(this->logo, GL_COMPILE);
	glBegin(GL_QUADS);
		glTexCoord2f(0.0f, 0.0f);glVertex3f(0.0f,0.0f,0.0f);
		glTexCoord2f(1.0f, 0.0f);glVertex3f(512.0f,0.0f,0.0f);
		glTexCoord2f(1.0f, 1.0f);glVertex3f(512.0f,512.0f,0.0f);
		glTexCoord2f(0.0f, 1.0f);glVertex3f(0.0f,512.0f,0.0f);
	glEnd();
	glEndList();         
}

CLogo::~CLogo()
{
    glDeleteLists(this->logo,1);
    glDisable(GL_TEXTURE_2D);
    glDisable(GL_ALPHA_TEST);
}

void CLogo::Redraw()
{
    glLoadIdentity();
  	glBindTexture(GL_TEXTURE_2D, texture[1]);
  	glTranslated(gl_win->getWidth()/2-256,gl_win->getHeight()/2-256,0);
  	glColor3f(alpha,alpha,alpha);
  	glCallList(this->logo);
  	if (alpha < 1.0f) alpha += 0.02f;
}

void CLogo::KeyDown(int key, int kchar)
{
     this->setNextScreen(INTRO_MENU);
}

bool CLogo::MouseChange(int x, int y, bool isLClicked, bool isRClicked)
{
     if (isLClicked || isRClicked) {
         this->setNextScreen(INTRO_MENU);
         return true;
     }
     else return false;
}


/* INTRO_MENU */

CIntroMenu::CIntroMenu(glWindow *glwin)
{
    this->setNextScreen(INTRO_MENU);
    this->menu_labels = ml;
    this->mnuMenu = new glMenu(glwin, menu_labels, 4, glwin->getWidth()/2-50,glwin->getHeight()/2);
    this->lblHeading = new glLabel(glwin, "Èloveèe, nehnevaj sa !",glwin->getWidth()/2-90,glwin->getHeight()/2+50,1,1,1,0);
    glEnable(GL_TEXTURE_2D);
    glEnable(GL_BLEND);
}

CIntroMenu::~CIntroMenu()
{
    delete this->mnuMenu;
    delete this->lblHeading;
    glDisable(GL_TEXTURE_2D);
    glDisable(GL_BLEND);
}

void CIntroMenu::Redraw()
{
    this->mnuMenu->Redraw();
    this->lblHeading->Redraw();
}

bool CIntroMenu::MouseChange(int x, int y, bool isLClicked, bool isRClicked)
{
     if (this->mnuMenu->UpdatePosition(x,y,isLClicked) && isLClicked) {
         switch (mnuMenu->getClickedIndex()) {
             case 1:
                  this->setNextScreen(GAME_SELECT);
                  break;
             case 2:
                  this->setNextScreen(STATISTICS);
                  break;
             case 3:
                  this->setNextScreen(CREDITS);
                  break;
             case 4:
                  PostQuitMessage(0);
                  break;
             default:
                  return false;
         }
         return true;
     }
     return false;
}

void CIntroMenu::KeyDown(int key, int kchar)
{
     mnuMenu->KeyDown(key,kchar);
}

void CIntroMenu::KeyUp(int key)
{
     mnuMenu->KeyUp(key);
}

bool CIntroMenu::KeyPressed(int key)
{
     return mnuMenu->KeyPressed(key);
}


/* GAME_SELECT */

CGameSelect::CGameSelect(glWindow *glwin, GameStatus *gstatus): gstatus(gstatus)
{
    this->setNextScreen(GAME_SELECT);
    this->lblHeading = new glLabel(glwin,"Vyberte si typ hry:",glwin->getWidth()/2-90,glwin->getHeight()/2+50,1,1,1,0);
    this->btn4 = new glButton(glwin, "4 hráèi",glwin->getWidth()/2-120,glwin->getHeight()/2,1);
    this->btn6 = new glButton(glwin, "6 hráèov",glwin->getWidth()/2+50,glwin->getHeight()/2,1);
    glEnable(GL_TEXTURE_2D);
    glEnable(GL_BLEND);
}

CGameSelect::~CGameSelect()
{
    delete this->lblHeading;
    delete this->btn4;
    delete this->btn6;
    glDisable(GL_TEXTURE_2D);
    glDisable(GL_BLEND);
}

void CGameSelect::Redraw()
{
    this->lblHeading->Redraw();
    this->btn4->Redraw();
    this->btn6->Redraw();
}

bool CGameSelect::MouseChange(int x, int y, bool isLClicked, bool isRClicked)
{
    bool status;
    if ((status = this->btn4->UpdatePosition(x,y,isLClicked)) && isLClicked)
        this->gstatus->setGameType(0);
    else if ((status = this->btn6->UpdatePosition(x,y,isLClicked)) && isLClicked)
        this->gstatus->setGameType(1);
    if (status) {
        this->setNextScreen(USERS_SELECT);
        return true;
    }
    return false;
}

void CGameSelect::KeyDown(int key, int kchar)
{
    // nie su urobene focusy, preto klavesy putuju vsetkym ovladacim prvkom
    this->btn4->KeyDown(key, kchar);
    this->btn6->KeyDown(key, kchar);
}

void CGameSelect::KeyUp(int key)
{
    this->btn4->KeyUp(key);
    this->btn6->KeyUp(key);
}

bool CGameSelect::KeyPressed(int key)
{
    // to je jedno, ktory button pouzijem
    return this->btn4->KeyPressed(key);
}


/* USERS_SELECT */

CUsersSelect::CUsersSelect(glWindow *glwin, GameStatus *gstatus): gstatus(gstatus)
{
    int i;
    
    this->setNextScreen(USERS_SELECT);
    this->lblHeading = new glLabel(glwin,"Vyberte typy hráèov:",glwin->getWidth()/2-90,glwin->getHeight()/2+50,1,1,1,0);
    
    for (i = 0; i < 6; i++) {
        this->lblUser[i] = NULL;
        this->lblType[i] = NULL;
        this->btnChange[i] = NULL;
    }
    
    for (i = 0; i < gstatus->getGamePlayers(); i++) {
        this->lblUser[i] = new glLabel(glwin, users[i], glwin->getWidth()/2-120,glwin->getHeight()/2 - i* this->lblHeading->getHeight(),1,1,1,0);
        if (gstatus->getUserType(i) == true)
            this->lblType[i] = new glLabel(glwin, "PC", glwin->getWidth()/2-20,glwin->getHeight()/2 - i* this->lblHeading->getHeight(),1,0,1,0);
        else
            this->lblType[i] = new glLabel(glwin, "používate¾", glwin->getWidth()/2-20,glwin->getHeight()/2 - i* this->lblHeading->getHeight(),1,0,1,0);
        this->btnChange[i] = new glButton(glwin, "Zmeni", glwin->getWidth()/2+120,glwin->getHeight()/2 - i* this->lblHeading->getHeight(),1);
    }
    this->btnOK = new glButton(glwin, "OK",glwin->getWidth()/2+50,glwin->getHeight()/2- 6*this->lblHeading->getHeight(),1);
    this->btnBack = new glButton(glwin, "Spä",glwin->getWidth()/2-50,glwin->getHeight()/2- 6*this->lblHeading->getHeight(),1);
    glEnable(GL_TEXTURE_2D);
    glEnable(GL_BLEND);
}


CUsersSelect::~CUsersSelect()
{
    int i;
    delete lblHeading;
    for (i = 0; i < 6; i++)
        if (this->lblUser[i] != NULL) {
            delete this->lblUser[i];
            delete this->lblType[i];
            delete this->btnChange[i];
        }
    delete btnOK;
    delete btnBack;
    glDisable(GL_TEXTURE_2D);
    glDisable(GL_BLEND);
}

void CUsersSelect::Redraw()
{
    int i;
    lblHeading->Redraw();
    for (i = 0; i < 6; i++)
        if (this->lblUser[i] != NULL) {
            this->lblUser[i]->Redraw();
            this->lblType[i]->Redraw();
            this->btnChange[i]->Redraw();
        }
    btnOK->Redraw();
    btnBack->Redraw(); 
}

bool CUsersSelect::MouseChange(int x, int y, bool isLClicked, bool isRClicked)
{
    int i,k;
    bool comp, can, status;
    
    for (i = 0; i < gstatus->getGamePlayers(); i++) {
        if (this->btnChange[i]->UpdatePosition(x,y, isLClicked) && isLClicked) {
            comp = this->gstatus->getUserType(i);
            if (comp == false) {
                // pred zmenou pouzivatela na PC je nutne skontrolovat, ci je este
                // nejaky pouzivatel (musi byt min.1). Ak nie, nie je povolena zmena.
                can = false;
                for (k = 0; k < gstatus->getGamePlayers(); k++)
                    if (k != i)
                        if (this->gstatus->getUserType(k) == false) {
                            can = true;
                            break;
                        }
            } else can = true;
            if (can) {
                if (this->gstatus->getUserType(i) == false) {
                    this->gstatus->setUserType(i, true);
                    this->lblType[i]->setText("PC");
                } else {
                    this->gstatus->setUserType(i, false);
                    this->lblType[i]->setText("používate¾");
                }
            }
        }
    }
    if ((status = this->btnOK->UpdatePosition(x,y,isLClicked)) && isLClicked)
        this->setNextScreen(PROFILE_SELECT);
    else if ((status = this->btnBack->UpdatePosition(x,y,isLClicked)) && isLClicked)
        this->setNextScreen(INTRO_MENU);
    if (status) return true;
    return false;
}

void CUsersSelect::KeyDown(int key, int kchar)
{
    // nie su urobene focusy, preto je jedno kam klavesy putuju
    this->btnOK->KeyDown(key, kchar);
}

void CUsersSelect::KeyUp(int key)
{
    this->btnOK->KeyUp(key);
}

bool CUsersSelect::KeyPressed(int key)
{
    // to je jedno, ktory button pouzijem, ale musi byt zhodny s KeyDown a KeyUp
    return this->btnOK->KeyPressed(key);
}

/* PROFILE_SELECT */

CProfileSelect::CProfileSelect(glWindow *glwin, GameStatus *gstatus, Profiles *profiles)
: gstatus(gstatus), profiles(profiles), selectedProfile(0), gl_win(glwin)
{
    int i;
    Profile *tmp;
    
    this->setNextScreen(PROFILE_SELECT);
    for (i = 0; i < MAX_PROFILES; i++)
        this->btnProfiles[i] = NULL;
    
    for (i = 0; i < gstatus->getGamePlayers(); i++)
      if (gstatus->getUserType(i) == false) {
          this->selectIndex = i;
          break;
      }
    this->lblHeading = new glLabel(glwin, pheadings[this->selectIndex],glwin->getWidth()/2-90,glwin->getHeight()/2+110,1,1,1,0);
    this->lblNick = new glLabel(glwin, "Nick: ",glwin->getWidth()/2-100,glwin->getHeight()/2+50,1,1,1,0);
    this->txtNick = new glTextBox(glwin,glwin->getWidth()/2-30,glwin->getHeight()/2+50);
    this->btnChange = new glButton(glwin, "Zmeni",glwin->getWidth()/2+100,glwin->getHeight()/2+50,1);
    
    for (i = 0; i < profiles->getProfilesCount(); i++) {
        tmp = profiles->operator[](i);
        this->btnProfiles[i] = new glButton(glwin, tmp->getNick(),glwin->getWidth()/2-30,glwin->getHeight()/2+10-i*this->lblHeading->getHeight(),1);
    }
    if (i < MAX_PROFILES) {
        this->btnProfiles[i] = new glButton(glwin, "PRÁZDNY",glwin->getWidth()/2-30,glwin->getHeight()/2+10-i*this->lblHeading->getHeight(),1);
        i++;
    }
    
    this->btnOK = new glButton(glwin, "OK",glwin->getWidth()/2+100,glwin->getHeight()/2-50- i*this->lblHeading->getHeight(),1);
    this->btnBack = new glButton(glwin, "Spä",glwin->getWidth()/2-100,glwin->getHeight()/2-50- i*this->lblHeading->getHeight(),1);

    if (this->selectedProfile < this->profiles->getProfilesCount()) {
        tmp = this->profiles->operator[](this->selectedProfile);
        this->txtNick->setText(tmp->getNick());
    } else this->txtNick->setText("");
    glEnable(GL_TEXTURE_2D);
    glEnable(GL_BLEND);
}

CProfileSelect::~CProfileSelect()
{
    int i;
    
    delete this->lblHeading;
    delete this->lblNick;
    delete this->txtNick;
    delete this->btnChange;
    delete this->btnOK;
    delete this->btnBack;
    
    for (i = 0; i < MAX_PROFILES; i++)
        if (this->btnProfiles[i])
            delete this->btnProfiles[i];
    glDisable(GL_TEXTURE_2D);
    glDisable(GL_BLEND);
}

void CProfileSelect::Redraw()
{
     int i;
     
     this->lblHeading->Redraw();
     this->lblNick->Redraw();
     this->txtNick->Redraw();

     this->btnOK->Redraw();
     this->btnBack->Redraw();
     this->btnChange->Redraw();
     for (i = 0; i < MAX_PROFILES; i++)
         if (this->btnProfiles[i])
             this->btnProfiles[i]->Redraw();

     // vykreslenie horizontalnej ciary
 	 glDisable(GL_TEXTURE_2D);
 	 glLoadIdentity();
     glTranslated(gl_win->getWidth()/2-100,gl_win->getHeight()/2+34,0);
     glColor3f(0,0,1);
     glBegin(GL_QUADS);
         glVertex3f(0,0,0);
         glVertex3f(270,0,0);
         glVertex3f(270,2,0);
         glVertex3f(0,2,0);
     glEnd();
     
     // ak je niektory profil oznaceny, vykresli k nemu sipku
     glLoadIdentity();
     glTranslated(gl_win->getWidth()/2-90,gl_win->getHeight()/2+18-this->selectedProfile *this->lblHeading->getHeight(),0);
     glColor3f(1,1,0);
     glBegin(GL_QUADS);
         glVertex3f(0,0,0);
         glVertex3f(10,0,0);
         glVertex3f(10,5,0);
         glVertex3f(0,5,0);
     glEnd();
     glTranslated(10,0,0);
     glBegin(GL_TRIANGLES);
         glVertex3f(0,8,0);
         glVertex3f(0,-3,0);
         glVertex3f(5,2.5,0);
     glEnd();
	 glEnable(GL_TEXTURE_2D);
}

bool CProfileSelect::MouseChange(int x, int y, bool isLClicked, bool isRClicked)
{
     bool status;
     char n[10];
     int i;
     Profile *tmp;
     
     this->txtNick->getText(n);
     if (this->btnChange->UpdatePosition(x,y,isLClicked) && isLClicked && n[0]) {
         // zmeni(pripadne prida) profil s indexom selectedProfile
         if (this->selectedProfile == this->profiles->getProfilesCount() && ((this->selectedProfile+1) < MAX_PROFILES))
             this->btnProfiles[this->selectedProfile+1] = new glButton(gl_win, "PRÁZDNY", gl_win->getWidth()/2-30,gl_win->getHeight()/2+10-(this->selectedProfile+1)*this->lblHeading->getHeight(),1);
         
         if (this->selectedProfile >= this->profiles->getProfilesCount())
             this->profiles->AddProfile(n);
         else {
             tmp = this->profiles->operator[](this->selectedProfile);
             tmp->setNick(n);
             this->profiles->saveProfiles();
         }
         this->btnProfiles[this->selectedProfile]->setText(n);
         return true;
     }
     for (i = 0; i < MAX_PROFILES; i++)
         if (this->btnProfiles[i] && this->btnProfiles[i]->UpdatePosition(x,y,isLClicked) && isLClicked) {
             this->selectedProfile = i;
             if (this->selectedProfile < this->profiles->getProfilesCount()) {
                 tmp = this->profiles->operator[](this->selectedProfile);
                 this->txtNick->setText(tmp->getNick());
             } else this->txtNick->setText("");
             return true;
         }
     if ((status = this->btnOK->UpdatePosition(x,y,isLClicked)) && isLClicked && this->profiles->getProfilesCount()) {
         if (this->selectedProfile >= profiles->getProfilesCount()) return false;
         gstatus->setFactor(this->selectIndex, this->selectedProfile);
         // po stlaceni OK sa nastavi vyber profilu dalsieho hraca. Ak nie je, postupuje sa bud na vyber inteligencie, alebo na hru
         for (i = this->selectIndex+1; i < gstatus->getGamePlayers(); i++)
           if (gstatus->getUserType(i) == false) {
              this->selectIndex = i;
              this->lblHeading->setText(pheadings[this->selectIndex]);
              this->selectedProfile = 0;
              if (this->selectedProfile < this->profiles->getProfilesCount()) {
                  tmp = this->profiles->operator[](this->selectedProfile);
                  this->txtNick->setText(tmp->getNick());
              } else this->txtNick->setText("");
              return true;
           }
         // ak nenajdem hraca PC, nedavam uz screen intelligence, ale uz rovno hru
         this->setNextScreen(GAME);
         for (i = 0; i < gstatus->getGamePlayers(); i++)
             if (this->gstatus->getUserType(i) == true) {
                 this->setNextScreen(INTELLIGENCE_SELECT);
                 break;
             }
     }
     if ((status = this->btnBack->UpdatePosition(x,y,isLClicked)) && isLClicked)
         this->setNextScreen(INTRO_MENU);
     if (status) return true;
     return false;
}

void CProfileSelect::KeyDown(int key, int kchar)
{
     this->txtNick->KeyDown(key,kchar);
}

void CProfileSelect::KeyUp(int key)
{
     this->txtNick->KeyUp(key);
}

bool CProfileSelect::KeyPressed(int key)
{
     return this->txtNick->KeyPressed(key);
}

/* STATISTICS */

CStatistics::CStatistics(glWindow *glwin, Profiles *profiles): profiles(profiles), gl_win(glwin)
{
    int i;
    Profile *tmp;
    char n[100];
    char o[10];
    
    this->lblHeading = new glLabel(glwin, "Štatistiky hráèov" ,glwin->getWidth()/2-90,glwin->getHeight()/2+100,1,0,1,0);
    this->lblTabHeading = new glLabel(glwin, "Profil     Poèet hier  Vyhrané 4-hry  Vyhrané 6-hry  Úspech celkovo",
                                glwin->getWidth()/2-330,glwin->getHeight()/2+30,0,1,1,1);
 
    for (i = 0; i < profiles->getProfilesCount(); i++) {
        memset(n,0,100);
        tmp = profiles->operator[](i);
        this->lblProfiles[i] = new glLabel(glwin, tmp->getNick(),glwin->getWidth()/2-330,glwin->getHeight()/2-i*lblHeading->getHeight(),0,1,0.3,0);
        itoa(tmp->getGameCount(), n, 10);
        this->lblGameCount[i] = new glLabel(glwin, n,glwin->getWidth()/2-220,glwin->getHeight()/2-i*lblHeading->getHeight(),1,1,1,1);
        itoa(tmp->getWin4(),n, 10);
        memcpy (n+strlen(n), " (",2);
        itoa(tmp->getPercentWin4(),o,10);
        strcpy (n+strlen(n), o);
        memcpy (n+strlen(n), " %)",3);
        this->lblWin4[i] = new glLabel(glwin, n, glwin->getWidth()/2-100,glwin->getHeight()/2-i*lblHeading->getHeight(),1,1,1,1);
        itoa(tmp->getWin6(),n, 10);
        strcpy (n+strlen(n), " (");
        itoa(tmp->getPercentWin6(),o,10);
        strcpy (n+strlen(n), o);
        strcpy (n+strlen(n), " %)");
        this->lblWin6[i] = new glLabel(glwin, n, glwin->getWidth()/2+50,glwin->getHeight()/2-i*lblHeading->getHeight(),1,1,1,1);
        itoa(tmp->getPercentWin(), n, 10);
        strcpy (n+strlen(n), " %");
        this->lblWin[i] = new glLabel(glwin, n, glwin->getWidth()/2+200,glwin->getHeight()/2-i*lblHeading->getHeight(),1,1,1,1);
    }
    this->btnBack = new glButton(glwin, "Spä", glwin->getWidth()/2-50,glwin->getHeight()/2-30-i*lblHeading->getHeight(),1);
    glEnable(GL_TEXTURE_2D);
    glEnable(GL_BLEND);
}

CStatistics::~CStatistics()
{
    int i;
    delete this->lblHeading;
    delete this->btnBack;
    delete this->lblTabHeading;
    for (i = 0; i < profiles->getProfilesCount(); i++) {
        delete this->lblProfiles[i];
        delete this->lblGameCount[i];
        delete this->lblWin4[i];
        delete this->lblWin6[i];
        delete this->lblWin[i];
    }
    glDisable(GL_TEXTURE_2D);
    glDisable(GL_BLEND);
}

void CStatistics::Redraw()
{
    this->lblHeading->Redraw();
    this->lblTabHeading->Redraw();
    this->btnBack->Redraw();
    for (int i = 0; i < profiles->getProfilesCount(); i++) {
        this->lblProfiles[i]->Redraw();
        this->lblGameCount[i]->Redraw();
        this->lblWin4[i]->Redraw();
        this->lblWin6[i]->Redraw();
        this->lblWin[i]->Redraw();
    }

     // vykreslenie horizontalnej ciary
 	 glDisable(GL_TEXTURE_2D);
 	 glLoadIdentity();
     glTranslated(gl_win->getWidth()/2-330,gl_win->getHeight()/2+23,0);
     glColor3f(0,0,1);
     glBegin(GL_QUADS);
         glVertex3f(0,0,0);
         glVertex3f(680,0,0);
         glVertex3f(680,2,0);
         glVertex3f(0,2,0);
     glEnd();
	 glEnable(GL_TEXTURE_2D);
}

bool CStatistics::MouseChange(int x, int y, bool isLClicked, bool isRClicked)
{
     if (this->btnBack->UpdatePosition(x,y,isLClicked) && isLClicked) {
         this->setNextScreen(INTRO_MENU);
         return true;
     }
     return false;
}

void CStatistics::KeyDown(int key, int kchar)
{
     this->btnBack->KeyDown(key,kchar);
}

void CStatistics::KeyUp(int key)
{
     this->btnBack->KeyUp(key);
}

bool CStatistics::KeyPressed(int key)
{
     return this->btnBack->KeyPressed(key);
}


/* INTELLIGENCE_SELECT */

 CIntelligenceSelect::CIntelligenceSelect(glWindow *glwin, GameStatus *gstatus): gstatus(gstatus)
 {
     int i,k;
     
     this->setNextScreen(INTELLIGENCE_SELECT);
     for (i = 0; i < 5; i++) {
         this->lblUser[i] = NULL;
         this->lblType[i] = NULL;
         this->btnChange[i] = NULL;
         this->usersIndex[i] = 0;
     }
         
     this->lblHeading = new glLabel(glwin, "Vyberte inteligenciu PC hráèov:" ,glwin->getWidth()/2-130,glwin->getHeight()/2+50,1,1,1,0);
     for (i = 0, k = 0; i < gstatus->getGamePlayers(); i++)
         if (gstatus->getUserType(i) == true) {
             this->lblUser[k] = new glLabel(glwin, users[i],glwin->getWidth()/2-100,glwin->getHeight()/2-k*lblHeading->getHeight(),1,1,1,0);
             if (!gstatus->getFactor(i))
                 this->lblType[k] = new glLabel(glwin, "1. úroveò", glwin->getWidth()/2,glwin->getHeight()/2-k*lblHeading->getHeight(),1,0,1,0);
             else
                 this->lblType[k] = new glLabel(glwin, "2. úroveò", glwin->getWidth()/2,glwin->getHeight()/2-k*lblHeading->getHeight(),1,0,1,0);
             this->btnChange[k] = new glButton(glwin, "Zmeni",glwin->getWidth()/2+120,glwin->getHeight()/2-k*lblHeading->getHeight(),1);
             this->usersIndex[k] = i;
             k++;
         }
     this->btnBack = new glButton(glwin, "Spä", glwin->getWidth()/2-90,glwin->getHeight()/2-10-k*lblHeading->getHeight(),1);
     this->btnOK = new glButton(glwin, "ŠTART", glwin->getWidth()/2+90,glwin->getHeight()/2-10-k*lblHeading->getHeight(),1);
     glEnable(GL_TEXTURE_2D);
     glEnable(GL_BLEND);
 }
 
 CIntelligenceSelect::~CIntelligenceSelect()
 {
     int i;
     delete this->lblHeading;
     delete this->btnBack;
     delete this->btnOK;
     
     for (i = 0; i < 5; i++)
         if (this->lblUser[i]) {
             delete this->lblUser[i];
             delete this->lblType[i];
             delete this->btnChange[i];
         }
     glDisable(GL_TEXTURE_2D);
     glDisable(GL_BLEND);
 }

 void CIntelligenceSelect::Redraw()
 {
     int i;
     this->lblHeading->Redraw();
     this->btnBack->Redraw();
     this->btnOK->Redraw();

     for (i = 0; i < 5; i++)
         if (this->lblUser[i]) {
             this->lblUser[i]->Redraw();
             this->lblType[i]->Redraw();
             this->btnChange[i]->Redraw();
         }
 }

 bool CIntelligenceSelect::MouseChange(int x, int y, bool isLClicked, bool isRClicked)
 {
     int i;
     bool status;
     
     for (i = 0; i < 5; i++)
         if (this->btnChange[i])
             if (this->btnChange[i]->UpdatePosition(x,y,isLClicked) && isLClicked) {
                 if (!gstatus->getFactor(this->usersIndex[i])) {
                     gstatus->setFactor(this->usersIndex[i], 1);
                     this->lblType[i]->setText("2. úroveò");
                 } else {
                     gstatus->setFactor(this->usersIndex[i], 0);
                     this->lblType[i]->setText("1. úroveò");
                 }
                 return true;
             }
     if ((status = this->btnBack->UpdatePosition(x,y,isLClicked)) && isLClicked)
         this->setNextScreen(INTRO_MENU);
     else if ((status = this->btnOK->UpdatePosition(x,y,isLClicked)) && isLClicked)
         this->setNextScreen(GAME);
     return status;
 }

 void CIntelligenceSelect::KeyDown(int key, int kchar)
 {
      this->btnOK->KeyDown(key, kchar);
 }
 
 void CIntelligenceSelect::KeyUp(int key)
 {
      this->btnOK->KeyUp(key);
 }
 
 bool CIntelligenceSelect::KeyPressed(int key)
 {
      return this->btnOK->KeyPressed(key);
 }

 /* CREDITS */
 
 CCredits::CCredits(glWindow *glwin)
 {
     this->lblHeading = new glLabel(glwin, "O Autoroch" ,glwin->getWidth()/2-50,glwin->getHeight()/2+50,1,0,1,0);
     this->lblCopyright = new glLabel(glwin, "© Copyright 2006-2007, P. Jakubèo, E. Danková, O. Drusa" ,50,lblHeading->getY()-50,1,1,0.2,0);
     this->lblCopyright2 = new glLabel(glwin, "Právne podrobnosti si pozrite v súbore" ,50,lblCopyright->getY()-20,0.2,0.2,1,0);
     this->lblCopyright3 = new glLabel(glwin, "licencia.txt" ,lblCopyright2->getX()+lblCopyright2->getWidth()+2,lblCopyright2->getY(),0.2,1,1,0);
     this->btnBack = new glButton(glwin, "Spä",glwin->getWidth()/2-20,lblCopyright3->getY()-50,1);
     glEnable(GL_TEXTURE_2D);
     glEnable(GL_BLEND);
 }

 CCredits::~CCredits()
 {
     delete this->lblHeading;
     delete this->lblCopyright;
     delete this->lblCopyright2;
     delete this->lblCopyright3;
     delete this->btnBack;
     glDisable(GL_TEXTURE_2D);
     glDisable(GL_BLEND);
 }

 void CCredits::Redraw()
 {
      this->lblHeading->Redraw();
      glScalef(0.2,0.2,0.2);
      this->lblCopyright->Redraw();
      this->lblCopyright2->Redraw();
      this->lblCopyright3->Redraw();
      glLoadIdentity();
      this->btnBack->Redraw();
 }

 bool CCredits::MouseChange(int x, int y, bool isLClicked, bool isRClicked)
 {
      if (this->btnBack->UpdatePosition(x,y,isLClicked) && isLClicked) {
          this->setNextScreen(INTRO_MENU);
          return true;
      }
      return false;
 }

 void CCredits::KeyDown(int key, int kchar)
 {
      this->btnBack->KeyDown(key, kchar);
 }
 
 void CCredits::KeyUp(int key)
 {
      this->btnBack->KeyUp(key);
 }
 
 bool CCredits::KeyPressed(int key)
 {
      return this->btnBack->KeyPressed(key);
 }

 /* GAME_OVER */

 CGameOver::CGameOver(glWindow *glwin, GameStatus *gstatus):gstatus(gstatus), winCount(0)
 {
     int i,j;
     double phi = 0.0;
     char n[20];

     this->setNextScreen(GAME_OVER);
     glwin->setWindowType(true);
     glwin->resizeWindow(glwin->getWidth(), glwin->getHeight());

     this->lblHeading = new glLabel(glwin, "KONIEC HRY" ,glwin->getWidth()/2-45,glwin->getHeight()/2+50,0.3,0.3,1,0);
     
     for (i = 0; i < gstatus->getGamePlayers(); i++) 
         for (j = 0; j < gstatus->getGamePlayers(); j++)
             if (gstatus->getPlayerHome(j) == i+1) {
                 itoa(i+1,n,10);
                 strcpy(n+strlen(n), ".    ");
                 strcpy(n+strlen(n), gstatus->getNick(j));
                 this->lblWins[i] = new glLabel(glwin, n, glwin->getWidth()/2-50,glwin->getHeight()/2-i*lblHeading->getHeight(),0,1,0.5,0);
                 switch (j) {
                     case 0:  // cierna
                         colors[i].r = 0.2; colors[i].g = 0.2; colors[i].b = 0.2;
                         break;
                     case 1:  // zlta
                         colors[i].r = 1.0; colors[i].g = 0.8; colors[i].b = 0.0;
                         break;
                     case 2:  // fialova(6) modra(4)
                         if (gstatus->getGamePlayers() == 4) {
                             colors[i].r = 0.0; colors[i].g = 0.0; colors[i].b = 0.8;
                         } else {
                             colors[i].r = 1.0; colors[i].g = 0.0; colors[i].b = 0.8;
                         }
                         break;
                     case 3:  // zelena(6) cervena(4)
                         if (gstatus->getGamePlayers() == 4) {
                             colors[i].r = 0.8; colors[i].g = 0.0; colors[i].b = 0.0;
                         } else {
                             colors[i].r = 0.0; colors[i].g = 0.8; colors[i].b = 0.0;
                         }
                         break;
                     case 4:  // cervena
                         colors[i].r = 0.8; colors[i].g = 0.0; colors[i].b = 0.0;
                         break;
                     case 5:  // modra
                         colors[i].r = 0.0; colors[i].g = 0.0; colors[i].b = 0.8;
                         break;
                 }
                 this->winCount++;
                 break;
             }
     this->btnBack = new glButton(glwin, "Spä",glwin->getWidth()/2-20,glwin->getHeight()/2-10-winCount*lblHeading->getHeight(),1);

     this->figure2D = glGenLists(1);
     glNewList(this->figure2D, GL_COMPILE);
         glBegin(GL_TRIANGLES);
             glVertex3f(0,0,0);
             glVertex3f(15,0,0);
             glVertex3f(7.5,16,0);
         glEnd();
         glBegin(GL_POLYGON);
             for (i = 0; i < 50; i++) {
                 glVertex3f(7.5 + cos(phi) * 6, 20 + sin(phi) * 6, 0);
                 phi += 2*M_PI/50;
             }
         glEnd();
     glEndList();
     glClearColor(0,0,0,0);
     gstatus->stopGame();
 }

 CGameOver::~CGameOver()
 {
     int i;

     glDeleteLists(this->figure2D, 1);
     for (i = 0; i < winCount; i++)
         delete this->lblWins[i];
     delete this->lblHeading;
     delete this->btnBack;
     glDisable(GL_TEXTURE_2D);
     glDisable(GL_BLEND);
 }

 void CGameOver::Redraw()
 {
     int i;

     for (i = 0; i < winCount; i++)
         lblWins[i]->Redraw();

     glDisable(GL_TEXTURE_2D);
     glDisable(GL_BLEND);
     for (i = 0; i < winCount; i++) {
         glLoadIdentity();
         glTranslatef(25+lblWins[i]->getX(),lblWins[i]->getY(),0);
         glColor3f(colors[i].r,colors[i].g,colors[i].b);
         glCallList(this->figure2D);
     }
     glEnable(GL_TEXTURE_2D);
     glEnable(GL_BLEND);
     
     this->lblHeading->Redraw();
     this->btnBack->Redraw();
 }

 bool CGameOver::MouseChange(int x, int y, bool isLClicked, bool isRClicked)
 {
      if (this->btnBack->UpdatePosition(x,y,isLClicked) && isLClicked) {
          this->setNextScreen(INTRO_MENU);
          return true;
      }
      return false;
 }

 void CGameOver::KeyDown(int key, int kchar)
 {
      this->btnBack->KeyDown(key, kchar);
 }
 
 void CGameOver::KeyUp(int key)
 {
      this->btnBack->KeyUp(key);
 }
 
 bool CGameOver::KeyPressed(int key)
 {
      return this->btnBack->KeyPressed(key);
 }

/* GameStatus */

 GameStatus::GameStatus(Profiles *profiles): profiles(profiles), activePlayer(0), winIndex(0)
 {
     for (int i=0; i < 6; i++) {
         players[i].factor = 0;
         players[i].computer = false;
         homePlayers[i] = 0;
     }
 }

 GameStatus::~GameStatus()
 {}

 void GameStatus::setGameType(int type)
 {
      if (type) this->gameType = 6;
      else  this->gameType = 4;
 }
 
 int GameStatus::getGamePlayers()
 {
     return this->gameType;
 }

 void GameStatus::setUserType(int playerIndex, bool computer)
 {
      players[playerIndex].computer = computer;
 }
 
 bool GameStatus::getUserType(int playerIndex)
 {
      return players[playerIndex].computer;
 }
 
 void GameStatus::setFactor(int playerIndex, int factor)
 {
      players[playerIndex].factor = factor;
 }
 
 int GameStatus::getFactor(int playerIndex)
 {
     return players[playerIndex].factor;
 }

 void GameStatus::startGame()
 {
     this->activePlayer = 0;
 }
 
 void GameStatus::stopGame()
 {
     int i,j = -1,k;
     
     if (this->gameType == 4) k = 0;
     else k = 1;
     for (i = 0; i < this->gameType; i++)
         if (homePlayers[i] == 1) {
             j = i;
             break;
         }
     for (i = 0; i < this->gameType; i++)
         if (players[i].computer == false) {
             if (i == j)
                 profiles->AddGame(players[i].factor, k,1);
             else
                 profiles->AddGame(players[i].factor, k,0);
         }
 }

 // vrati true ak bol zmeneny hrac
 bool GameStatus::updateThrows()
 {
     throws[activePlayer]--;
     if (throws[activePlayer] <= 0) {
         activePlayer++;
         activePlayer %= getGamePlayers();
         return true;
     }
     return false;
 }

 char *GameStatus::getActiveNick()
 {
    Profile *p;
    static char n[20];
    
    if (players[this->activePlayer].computer == false) {
        p = this->profiles->operator[](players[this->activePlayer].factor);
        return p->getNick();
    } else {
        strcpy(n, "PC (");
        itoa(players[this->activePlayer].factor+1,n+strlen(n),10);
        strcpy(n+strlen(n),". úroveò)");
        return n;
    }
 }

 char *GameStatus::getNick(int player)
 {
    Profile *p;
    static char n[20];
    
    if (players[player].computer == false) {
        p = this->profiles->operator[](players[player].factor);
        return p->getNick();
    } else {
        strcpy(n, "PC (");
        itoa(players[player].factor+1,n+strlen(n),10);
        strcpy(n+strlen(n),". úroveò)");
        return n;
    }
 }

 void GameStatus::setPlayerHome(int player)
 {
      homePlayers[player] = ++winIndex;
 }

 int GameStatus::getPlayerHome(int player)
 {
     return homePlayers[player];
 }
