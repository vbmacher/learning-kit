/*
 * Player.h
 *
 * (c) Copyright 2006, vbmacher, danka & jozefK
 *
 */

#ifndef __PLAYER_H__
#define __PLAYER_H__

#include <time.h>

#include "main.h"
#include "glScreens.h"

  // rozmery panaka
  #define QUALITY 8        // pocet bodov podstavy pri kresleni kuzela alebo gule
  #define BASE_RADIUS 3.5f // polomer dolnej podstavy kuzela
  #define TOP_RADIUS 0.8f  // polomer hornej podstavy kuzela
  #define HEIGHT 15.0f     // vyska panaka

class Player {
        GameStatus *gstatus;
        Point3D playerPosition[6][4];
        int playerIndex[6][4];

        Point3D movingPosition; // pri pohybe figurky sa vyuzivaju tato docasna pozicia figurky
        int movingIndex;
        int movingPlayer;
        int movingFigure;
        clock_t startMoving;
        bool nowMoving;

        bool mouseOver[6][4]; // ci je mys nad figurkou
        int selectedFigure;   // pri kliknuti na figurku sa tu zapise jej index

        Point3D S,P;
        double xmag, ymag, zmag; // vzdialenosti bodu P (kolmy priemet S) od bodu S (stred panaka)
        // objektove suradnice mysi (pomocou gluUnProject)
        // spolu tieto suradnice tvoria normalovy vektor f-obj
        double objx, objy,objz; // pre blizku rovinu orezu


        GLuint figure; // max. 6 hracov po 4 figurky (24), min. 4 hraci (16)
                       // preto je vyhodnejsie urobit si displaylist 1 panaka
        GLUquadricObj* quadr; // objekt gule a kuzela
        
        void fillField(int player, int index, Point3D &field);
        Vector3D getMouseRay(int mousex, int mousey);
        
        // funkcia vrati figurku do prveho domceka
        void goHome(int player, int figure);
        // funkcia pohne figurkou o 1 policko
        void moveNext();
 public:
        Point3D resultPoint;
        int resultPlayer;
        int resultFigure;
 
        /*
            Hracia plocha pre 4-och aj pre 6-tich hracoch je mapovana podobne, cize
            indexy policok su pridelovane rovnakym principom. Policka su indexovane
            od 0.
            
            Kazdy hrac ma vlastne mapovanie plochy, cize sa NEvychadza z jednoznacneho
            cislovania policok, kazdy hrac ma vlastne cislovanie.
            Teda pre kazdeho hraca samostatne plati:
            Policka s indexom 0-3 ukazuju na jeho domcek v rohu, pricom 0-3 je cislo
            policka v domceku.
            Policka s indexom 4-43 (pre 4 hracov) alebo 4-51 (pre 6 hracov) urcuju
            hracie policka za sebou s tym, ze policko s indexom 4 je vzdy prvym polickom,
            kde sa ma figurka nachadzat (inicialna pozicia), ked sa hodi 6-tka. Cize kazdy hrac ma k dispozicii
            indexy 4-.. a pritom kazdy index podla typu hraca ukazuje na ine policko (v jednoznacnej
            identifikacii).
            Indexy 44-47 (4 hraci) alebo 52-55 (6 hracov) ukazuju na cielovy domcek daneho hraca,
            pricom jednotlive indexy urcuju poradie policka v domceku.
        */
        
        Player(GameStatus *gstatus);
        ~Player();

        /* vracia poradove cislo policka, na ktorom sa hrac nachadza,
           pricom do premennej resultPoint sa ulozia hodnoty suradnic policka
           Index policka zacina stale 0, pricom 0-3 urcuje prvy domcek a posledne 4
           urcuju druhy domcek */
        int getPlayerPosition(int player, int figure);
        void setPlayerPosition(int player, int figure, int index);
        /* implementacia statickej logiky - zisti, ci BY nastala kolizia hraca player s
           polickom index (ci je na nom nieco).
           To, aky hrac a aka figurka je na policku index sa zapise do premennej
           resultPlayer a resultFigure */
        bool getIndexCollision(int player, int figure, int index);

        /* implementacia statickej logiky, vrati true ak hrac moze ist, teda pohnut
           sa o result policok alebo vybrat hraca z domceka */
        bool canGo(int result);
        /* implementacia statickej logiky, vrati true ak hracova figurka moze ist, teda pohnut
           sa o result policok alebo vybrat hraca z domceka */
        bool canGo(int player, int figure, int result);

        /* funkcia pripravi hod kockou pre aktualneho hraca. Vrati true, ak hrac je schopny hrat */
        bool prepareThrow();
        /* urobi s hracom co treba, dynamicka logika - vrati true ak nastal vyhadzov */
        bool goPlayer(int result);
        // vrati true ak sa hrac este hybe
        bool isMoving() { return this->nowMoving;}
        
        /* Implementacia umelej inteligencie */
        bool UISelectPlayer(int result);

        /* podla vysledku hodu kocky a dalsej hernej logiky dovoli oznacenie figuriek */
        bool UpdatePosition(int mousex, int mousey, bool isClicked, int result);
        // prekresli hracov
        void RedrawPlayers(Point3D scenePosition);
 };



#endif
