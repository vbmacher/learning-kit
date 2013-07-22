/*
 *
 *  TECHNICKÁ UNIVERZITA KOŠICE
 *  FAKULTA ELETKROTECHNIKY A INFORMATIKY
 *  KATEDRA POÈÍTAÈOV
 * ---------------------------------------
 *  Èloveèe, nehnevaj sa !
 * ---------------------------------------
 *  profile.cpp
 *
 * (c) Copyright 2006, P. Jakubèo, E.Danková, O.Drusa
 *
 * Modul pre pracu s profilmi hracov
 *
 */

#include <windows.h>
#include <stdio.h>
#include <string.h>

#include "glGUI.h"
#include "glScreens.h"
#include "profile.h"

// Profily sa budu ukladat do suboru
const char *profiles_file = "profiles.dat";

/* Profil hraca */

	// Konstruktor
	Profile::Profile(char *nick, int count4, int count6, int win4, int win6)
	: count4(count4), count6(count6), win4(win4), win6(win6)
	{
      this->nick[0] = 0;
      this->setNick(nick);
	}


	void Profile::setNick(char *nick)
    { 
        int size;
 
        if (strlen(nick) > MAX_NICK_LENGTH) size = MAX_NICK_LENGTH;
        else size = strlen(nick);
        memset(this->nick, 0, MAX_NICK_LENGTH);
        memcpy(this->nick, nick, size);
    }

	char *Profile::getNick()           { return this->nick; }
	int Profile::getCount4()	       { return this->count4; }
  	void Profile::setCount4(int count) { this->count4 = count; }
	int Profile::getCount6()	       { return this->count6; }
   	void Profile::setCount6(int count) { this->count6 = count; }
	int Profile::getWin4()	           { return this->win4; }
   	void Profile::setWin4(int win)     { this->win4 = win; }
	int Profile::getWin6()             { return this->win6; }
   	void Profile::setWin6(int win)     { this->win6 = win; }
	int Profile::getGameCount()        { return (this->count4+this->count6); }

	// Percentualna uspesnost v 4-hracovych hrach
	// % = 100*win4 / count4
	int Profile::getPercentWin4()
    {
        if (!count4) return 0;
        return (100 * this->win4 / this->count4);
    }

	// Percentualna uspesnost v 6-hracovych hrach
	// % = 100*win6 / count6
	int Profile::getPercentWin6()
    {
        if (!count6) return 0;
        return (100 * this->win6 / this->count6);
    }

	// Percentualna uspesnost vo vsetkych hrach
	// % = 100*win / count
	int Profile::getPercentWin()
    {
        if (!getGameCount()) return 0;
        return (100 * (this->win4 + this->win6) / this->getGameCount());
    }

/* Profiles */

	int Profiles::loadProfiles()
	{
        ProfileStruct prof;
		FILE *fin;

        fin = fopen(profiles_file, "rb");
        if (fin == NULL) return 0;

		this->profilesCount = 0;
		while (fread(&prof, sizeof(ProfileStruct),1,fin)) {
			profiles[this->profilesCount++] = new Profile(prof.pNick, prof.pCount4, prof.pCount6, prof.pWin4, prof.pWin6);
			if (this->profilesCount >= MAX_PROFILES) break;
		}
		fclose(fin);
		return 1; 
	}

	// Zapise profily do suboru. Ak sa nepodarilo zapisat profily,
	// vracia 0. Inak 1.
	int Profiles::saveProfiles()
	{
		int i, size, j;
		FILE *fout;
		ProfileStruct prof;

        fout = fopen(profiles_file, "wb");
		if (fout == NULL) return 0;

		for (i = 0; i < this->profilesCount; i++) {
            strcpy(prof.pNick, profiles[i]->getNick());
			prof.pCount4 = profiles[i]->getCount4();
            prof.pCount6 = profiles[i]->getCount6();
            prof.pWin4 = profiles[i]->getWin4();
            prof.pWin6 = profiles[i]->getWin6();
			fwrite(&prof, sizeof(ProfileStruct),1,fout);
		}
		fclose(fout);
		return 1;
	}

	Profiles::Profiles():profilesCount(0)
    {
        this->loadProfiles();
    }
	Profiles::~Profiles()
	{
		int i;
		for (i = 0; i < this->profilesCount; i++)
			delete this->profiles[i];
		this->profilesCount = 0;
	}

	// Prida novy profil pouzivatela
	// Vrati 0 ak chyba, inak 1.
	int Profiles::AddProfile(char *nick)
	{
		if (profilesCount >= MAX_PROFILES) return 0;
		if (this->SearchProfile(nick) != -1) return 1;
		this->profiles[profilesCount++] = new Profile(nick,0,0,0,0);
		return this->saveProfiles();
	}

	// Prida novu hru pouzivatela (plus vysledok hry: 0 prehra, 1 vyhra)
	// typ hry : 0 - 4 hraci, inak - 6 hracov
	// Vrati 0 ak chyba, inak 1.
	int Profiles::AddGame(int index, int type, int win)
	{
		if (index < 0 || index >= this->profilesCount) return 0;

		if (!type) {
			profiles[index]->setCount4(profiles[index]->getCount4()+1);
            profiles[index]->setWin4(profiles[index]->getWin4()+win);
        }
		else {
			profiles[index]->setCount6(profiles[index]->getCount6()+1);
            profiles[index]->setWin6(profiles[index]->getWin6()+win);
        }
		return this->saveProfiles();
	}

	// Pretazeny indexovy operator. Pozor ! Nikde tu nie je definovany
	// operator= ! to znamena, ze priradovanie je mozne, ale urobi sa
	// iba priradenie odkazov, nie prekopirovanie celeho obsahu profilu.
	Profile* Profiles::operator[](int index)
	{
		return profiles[index];
	}

   	int Profiles::SearchProfile(char *nick)
   	{
        char *tmp;
        int i;
        for (i = 0; i < this->profilesCount; i++) {
            tmp = this->profiles[i]->getNick();
            if (!strcmp(tmp,nick)) {
            	MessageBox(NULL,tmp,"CHYBA",MB_OK|MB_ICONEXCLAMATION);
                return i;
            }
        }
        return -1;
    }

    int Profiles::getProfilesCount()
    {
         return this->profilesCount;
    }

