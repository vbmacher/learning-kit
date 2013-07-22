/*
 *  TECHNICKÁ UNIVERZITA KOŠICE
 *  FAKULTA ELETKROTECHNIKY A INFORMATIKY
 *  KATEDRA POÈÍTAÈOV
 * ---------------------------------------
 *  Èloveèe, nehnevaj sa !
 * ---------------------------------------
 *  profile.h
 *
 * (c) Copyright 2006, P. Jakubèo, E.Danková, O.Drusa
 *
 */
 
 #ifndef __PROFILE_H__
 #define __PROFILE_H__
 
 #include <stdio.h>
  
 #define MAX_PROFILES 10
 #define MAX_NICK_LENGTH 10

 typedef struct {
     char pNick[MAX_NICK_LENGTH];
     int pCount4;
     int pCount6;
     int pWin4;
     int pWin6;
 } ProfileStruct;

 /* Profil hraca */
    class Profile {
        char nick[MAX_NICK_LENGTH];
        int count4;
        int count6;
        int win4;
        int win6;
    public:
    	Profile(char *nick, int count4, int count6, int win4, int win6);
    	~Profile(){}
    	char *getNick();
    	void setNick(char *nick);

    	int getCount4();
    	void setCount4(int count);
    	
    	int getCount6();
    	void setCount6(int count);
    	
    	int getWin4();
    	void setWin4(int count);
    	
    	int getWin6();
    	void setWin6(int count);
    	
    	int getGameCount();

    	int getPercentWin4();
    	int getPercentWin6();
    	int getPercentWin();
    };
    
    class Profiles {
    	Profile *profiles[MAX_PROFILES];
    	int profilesCount;
    
    	int loadProfiles();
    	int SearchProfile(char *nick);
    public:
    	Profiles();
    	~Profiles();
    
    	int AddProfile(char *nick);
       	int AddGame(int index, int type, int win);
    	int saveProfiles();
    	int getProfilesCount();
    	
       	Profile* operator[](int);
    };

 #endif
 
 
