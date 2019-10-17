/*
 *  TECHNICKÁ UNIVERZITA KOŠICE
 *  FAKULTA ELETKROTECHNIKY A INFORMATIKY
 *  KATEDRA POÈÍTAÈOV
 * ---------------------------------------
 *  Èloveèe, nehnevaj sa !
 * ---------------------------------------
 *  Player.cpp
 *
 * (c) Copyright 2006, P. Jakubèo, E.Danková, O.Drusa
 *
 */

#include <math.h>
#include <windows.h>
#include "main.h"
#include "glScreens.h"
#include "Player.h"
#include "CGame.h"

/* Player */

// mapovanie suradnic policok na hracom plane
Point3D houses4[] = { { 107  ,0,-107  }, { 107,0, -85}, {  85  ,0, -85  }, {  85  ,0,-107  }, // cierny
                      {   0  ,0, -85  }, {   0,0, -64}, {   0  ,0, -42.5}, {   0  ,0, -21  },
                      
                      {-107  ,0,-107  }, {-107,0, -85}, { -85  ,0, -85  }, { -85  ,0,-107  }, // zlty
                      { -85  ,0,   0  }, { -64,0,   0}, { -42.5,0,   0  }, { -21  ,0,   0  },
                      
                      {-107  ,0, 107  }, {-107,0,  85}, { -85  ,0,  85  }, { -85  ,0, 107  }, // modry
                      {   0  ,0,  85  }, {   0,0,  64}, {   0  ,0,  42.5}, {   0  ,0,  21  },
                      
                      { 107  ,0, 107  }, { 107,0,  85}, {  85  ,0,  85  }, {  85  ,0, 107  }, // cerveny
                      {  85  ,0,   0  }, {  64,0,   0}, {  42.5,0,   0  }, {  21  ,0,   0  }
                    };

Point3D houses6[] = { { 116  ,0,-117  }, { 116,0,-102}, { 103  ,0,-117  }, { 103  ,0,-102  }, // cierny prvy domcek
                      {  87  ,0, -48  }, {  72,0, -33}, {  57  ,0, -20  }, {  43  ,0, -10  },
        
                      {-117  ,0,-117  }, {-117,0,-103}, {-103  ,0,-117  }, {-103  ,0,-103  }, // zlty  prvy domcek
                      {   0  ,0, -85  }, {   0,0, -64}, {   0  ,0, -42.5}, {   0  ,0, -21  }, // cielovy domcek
                      
                      {-117  ,0,  -8  }, {-117,0,   6}, {-104  ,0,  -8  }, {-104  ,0,   6  }, // ruzovy prvy domcek
                      { -87  ,0, -48  }, { -72,0, -33}, { -57  ,0, -20  }, { -43  ,0, -10  },

                      {-119  ,0, 102  }, {-119,0, 115}, {-105  ,0, 102  }, {-105  ,0, 115  }, // zeleny prvy domcek
                      { -88  ,0,  46  }, { -73,0,  33}, { -59  ,0,  20  }, { -44  ,0,   7  },

                      { 103  ,0, 101  }, { 103,0, 114}, { 116  ,0, 101  }, { 116  ,0, 114  }, // cerveny prvy domcek
                      {   0  ,0,  85  }, {   0,0,  64}, {   0  ,0,  42.5}, {   0  ,0,  21  },
                      
                      { 117  ,0,  -8  }, { 117,0,   6}, { 104  ,0,  -8  }, { 104  ,0,   6  }, // modry prvy domcek
                      {  87  ,0,  47  }, {  71,0,  32}, {  58  ,0,  20  }, {  43  ,0,   8  }
                    };

// 40 policok
Point3D fields4[] = { {-107  ,0, -21  }, { -85,0, -21}, { -64  ,0, -21  }, { -42.5,0, -21  }, { -21,0, -21},
                      { -21  ,0, -42.5}, { -21,0, -64}, { -21  ,0, -85  }, { -21  ,0,-107  }, {   0,0,-107},
                      {  21  ,0,-107  }, {  21,0, -85}, {  21  ,0, -64  }, {  21  ,0, -42.5}, {  21,0, -21},
                      {  42.5,0, -21  }, {  64,0, -21}, {  85  ,0, -21  }, { 107  ,0, -21  }, { 107,0,   0},
                      { 107  ,0,  21  }, {  85,0,  21}, {  64  ,0,  21  }, {  42.5,0,  21  }, {  21,0,  21},
                      {  21  ,0,  42.5}, {  21,0,  64}, {  21  ,0,  85  }, {  21  ,0, 107  }, {   0,0, 107},
                      { -21  ,0, 107  }, { -21,0,  85}, { -21  ,0,  64  }, { -21  ,0,  42.5}, { -21,0,  21},
                      { -42.5,0,  21  }, { -64,0,  21}, { -85  ,0,  21  }, {-107  ,0,  21  }, {-107,0,   0}
                    };

// 48 policok
Point3D fields6[] = {   { -21  ,0,-107  },
                        { -21  ,0, -85  }, { -21,0, -64}, { -40  ,0, -57  }, { -57  ,0, -46  }, { -72,0, -60},
                        { -86  ,0, -75  }, {-101,0, -62}, {-115  ,0, -48  }, {-101  ,0, -33  }, { -84,0, -20},
                        { -84  ,0,  -1  }, { -84,0,  18}, {-101  ,0,  32  }, {-115  ,0,  47  }, {-102,0,  61},
                        { -86  ,0,  74  }, { -72,0,  60}, { -57  ,0,  46  }, { -39  ,0,  56  }, { -21,0,  64},
                        { -21  ,0,  85  }, { -21,0, 107}, {   0  ,0, 107  }, {  21  ,0, 107  }, {  21,0,  85},
                        {  21  ,0,  67  }, {  39,0,  56}, {  57  ,0,  46  }, {  72  ,0,  60  }, {  86,0,  74},
                        { 102  ,0,  61  }, { 115,0,  47}, { 101  ,0,  32  }, {  84  ,0,  18  }, {  84,0,  -1},
                        {  84  ,0, -20  }, { 101,0, -33}, { 115  ,0, -48  }, { 101  ,0, -62  }, {  86,0, -75},
                        {  72  ,0, -60  }, {  57,0, -46}, {  40  ,0, -57  }, {  21  ,0, -64  }, {  21,0, -85},
                        {  21  ,0,-107  }, {   0,0,-107}
                    };

Player::Player(GameStatus *gstatus):gstatus(gstatus), selectedFigure(0), nowMoving(false)
{
    int i,j,k, p;
    
    // inicializacia pozicii
    k = gstatus->getGamePlayers();
    if (k == 6) {
        for (i = 0; i < k; i++)
            for (j = 0; j < 4; j++) {
                playerPosition[i][j].x = houses6[8 * i + j].x;
                playerPosition[i][j].y = houses6[8 * i + j].y;
                playerPosition[i][j].z = houses6[8 * i + j].z;
                playerIndex[i][j] = j;
                mouseOver[i][j] = false;
            }
    } else {
        for (i = 0; i < k; i++)
            for (j = 0; j < 4; j++) {
                playerPosition[i][j].x = houses4[8 * i + j].x;
                playerPosition[i][j].y = houses4[8 * i + j].y;
                playerPosition[i][j].z = houses4[8 * i + j].z;
                playerIndex[i][j] = j;
                mouseOver[i][j] = false;
            }
    }        

    /* displaylist panaka */
    this->figure = glGenLists(1);
 
    /* novy quadratic objekt */
    this->quadr = gluNewQuadric();
    glNewList(this->figure, GL_COMPILE);
   	   glPushMatrix();
   	       glRotatef(-90,1,0,0);
           gluCylinder(quadr, BASE_RADIUS, TOP_RADIUS, HEIGHT*0.82, 8, 5);
       glPopMatrix();
       glTranslatef(0.0f,HEIGHT,0.0f);
       gluSphere(quadr, BASE_RADIUS, QUALITY, QUALITY);
    glEndList();
}

Player::~Player()
{
    glDeleteLists(this->figure, 1);
    gluDeleteQuadric(this->quadr);
}

// do struktury field zapise poziciu policka index (jednoznacne suradnice)
void Player::fillField(int player, int index, Point3D &field)
{
    int i;
    if (gstatus->getGamePlayers() == 6) {
        if (index < 4) {
            // prvy domcek
            field.x = houses6[8 * player + index].x;
            field.y = houses6[8 * player + index].y;
            field.z = houses6[8 * player + index].z;
        } else if (index >= 52) {
            // cielovy domcek
            field.x = houses6[8 * player + index - 48].x;
            field.y = houses6[8 * player + index - 48].y;
            field.z = houses6[8 * player + index - 48].z;
        } else if (index >3) {
            // policko na plane + 4
            switch (player) {
                case 0: // cierny
                    i = index-4+40; break;
                case 1: // zlty
                    i = index-4; break;
                case 2: // fialovy
                    i = index-4+8; break;
                case 3: // zeleny
                    i = index-4+16; break;
                case 4: // cerveny
                    i = index-4+24; break;
                case 5: // modry
                    i = index-4+32; break;
            }
            i %= 48;
            field.x = fields6[i].x; field.y = fields6[i].y; field.z = fields6[i].z;
        }
    } else {
        if (index < 4) {
            field.x = houses4[8 * player + index].x;
            field.y = houses4[8 * player + index].y;
            field.z = houses4[8 * player + index].z;
        } else if (index >= 44) {
            // cielovy domcek
            field.x = houses4[8 * player + index - 40].x;
            field.y = houses4[8 * player + index - 40].y;
            field.z = houses4[8 * player + index - 40].z;
        } else if (index > 3) {
            // policko na plane + 4
            switch (player) {
                case 0: // cierny
                    i = index-4+10; break;
                case 1: // zlty
                    i = index-4; break;
                case 2: // modry
                    i = index-4+30; break;
                case 3: // cerveny
                    i = index-4+20; break;
            }
            i %= 40;
            field.x = fields4[i].x; field.y = fields4[i].y; field.z = fields4[i].z;
        }
    }
}

/* vracia poradove cislo policka, na ktorom sa hrac nachadza,
   pricom do premennej resultPoint sa ulozia hodnoty suradnic policka
   Index policka zacina stale 0, pricom 0-3 urcuje prvy domcek a posledne 4
   urcuju druhy domcek
*/
int Player::getPlayerPosition(int player, int figure)
{
    this->resultPoint.x = playerPosition[player][figure].x;
    this->resultPoint.y = playerPosition[player][figure].y;
    this->resultPoint.z = playerPosition[player][figure].z;
    return playerIndex[player][figure];
}

/* 7.1.2007 pridana implementacia pohybu figuriek */
void Player::setPlayerPosition(int player, int figure, int index)
{
    if (player == gstatus->getActivePlayer()) {
        memcpy(&movingPosition, &playerPosition[player][figure], sizeof(Point3D));
        movingIndex = playerIndex[player][figure];
        movingFigure = figure;
        movingPlayer = gstatus->getActivePlayer();
        if (movingIndex >= 4 && index >= 4) {
            nowMoving = true;
            startMoving = clock();
        }
    }
    playerIndex[player][figure] = index;
    fillField(player, index, playerPosition[player][figure]);
}

// jednoducha implementacia pohybu figuriek
// kazdych 0.2s sa pohne o 1 policko
void Player::moveNext()
{
     int finishIndex;
     
     finishIndex = playerIndex[movingPlayer][movingFigure];
     if (movingIndex == finishIndex) {
         nowMoving = false;
         return;
     }
     
     // mozu nastat 2 pripady:
     // 1. policka (0- 3) -> (4-..) alebo naopak
     // 2. policka (4-..) -> (4-..)
     if ((clock() - startMoving) / (double)CLK_TCK < 0.3) return;
     if (movingIndex >= 4 && finishIndex >= 4) {
        movingIndex++;
        // mod o 1 viac, lebo moze byt aj 55 resp. 47
        if (gstatus->getGamePlayers() == 6) movingIndex %= 56;
        else movingIndex %= 48;
     } else {
        movingIndex = finishIndex;
     }
     fillField(movingPlayer, movingIndex, movingPosition);
     startMoving = clock();
}


/* implementacia statickej logiky, zisti, ci by nastala kolizia hraca s pollickom index */
bool Player::getIndexCollision(int player, int figure, int index)
{
    Point3D field;
    int i,k;
        
    fillField(player, index, field);
    resultPlayer = -1;
    resultFigure = -1;

    // prehladavanie vsetkych hracov
    for (i = 0; i < gstatus->getGamePlayers(); i++)
        for (k = 0; k < 4; k++)
            if (i != player || k != figure) {
                if ((playerPosition[i][k].x == field.x) &&
                    (playerPosition[i][k].y == field.y) &&
                    (playerPosition[i][k].z == field.z)) {
                    resultPlayer = i;
                    resultFigure = k;
                    return true;
                }
            }
    return false;
}

Vector3D Player::getMouseRay(int mousex, int mousey)
{
    double fx,fy,fz;        // pre daleku rovinu orezu
	Vector3D ray;
	double ModelMatrix[16];
   	double ProjMatrix[16];
   	float mousez;
   	int viewport[4];
   	double size;

   	glGetDoublev(GL_MODELVIEW_MATRIX, ModelMatrix);
   	glGetDoublev(GL_PROJECTION_MATRIX, ProjMatrix);
   	glGetIntegerv(GL_VIEWPORT, viewport);

	mousey = (viewport[3] - mousey + viewport[1]);
    mousez = 0.1f; //blizka rovina orezu
    gluUnProject((double)mousex,(double)mousey,(double)mousez,ModelMatrix,ProjMatrix, viewport,&objx,&objy,&objz);
    mousez = 5000.0f; //daleka rovina orezu
    gluUnProject((double)mousex,(double)mousey,(double)mousez,ModelMatrix,ProjMatrix, viewport,&fx,&fy,&fz);

    // vektor
	ray.x = fx - objx; ray.y = fy - objy; ray.z = fz - objz;

    // normalizacia
    size = sqrt(ray.x * ray.x + ray.y * ray.y + ray.z * ray.z);
    if (size) { ray.x = ray.x / size; ray.y = ray.y / size; ray.z = ray.z / size; }
    else { ray.x = 0; ray.y = 0; ray.z = 0; }
	return ray;
}

/* implementacia statickej logiky, vrati true ak figurka moze ist
   tead bud pohnut sa o result policok alebo vybrat figurku z prveho domceka
   
    figurka sa moze hybat ak su splnene vsetky tieto podmienky:
        a1. cielove policko je na hracej ploche alebo v cielovom domceku
        a2. na cielovom policku sa nenachadza hrac rovnakej farby
        a3. pozicia figurky je na hracej ploche alebo v cielovom domceku
        a4. cielove policko je dalej ako pozicia figurky
        a5. hrac musi byt na rade !!

   Figurku mozno vybrat (umiestnit na inicialnu poziciu 4 na hracej ploche), ak su splnene
   vsetky nasledujuce podmienky:
        b1. figurka musi byt v prvom domceku
        b2. na inicialnej pozicii na hracej ploche sa nesmie nachadzat figurka toho isteho hraca
        b3. hrac musi byt na rade !!
        b4. predchadzajuci hod bol 6

*/
bool Player::canGo(int player, int figure, int result)
{
    int i;
    bool status = false;

    // a5, b3
    if (player != gstatus->getActivePlayer()) return false;

    // a3, b1
    i = this->getPlayerPosition(player, figure);
    if (i < 4) {
        // pozicia figurky je v prvom domceku
        // b4
        if (result != 6) return false;
        // b2
        if (this->getIndexCollision(player, figure, 4)) {
            // kolizia by nastala, je to hrac tej istej farby ?
            if (this->resultPlayer == player) return false;
        }
        return true;
    }
    // a1
    i += result; // a4
    if (gstatus->getGamePlayers() == 4) {
        if (i > 47) return false;
    } else {
        // 6 hracov
        if (i > 55) return false;
    }
    // a2
    if (this->getIndexCollision(player, figure, i)) {
        // kolizia by nastala, je to hrac tej istej farby ?
        if (this->resultPlayer == player) return false;
    }
    return true;
}

// vrati true, ak hrac moze ist (bud moze vybrat panaka alebo moze dajakym hybat)
bool Player::canGo(int result)
{
    int i, k;
    bool status = false;
    
    i = gstatus->getActivePlayer();
    for (k = 0; k < 4; k++)
      if (canGo(i,k,result)) {
          status = true;
          break;
      }
    return status;
}

// zisti ci je mys na panakovi, podla luca
bool Player::UpdatePosition(int mousex, int mousey, bool isClicked, int result)
{
  Vector3D v,ray;
  int i,k;
  double t;
  bool status = false;

  /* 
    kazdy panak sa nahradi valcom o polomere SIRKA1 a vyske VYSKA
    A[objx, objy, objz] urcuju bod na blizkej rovine orezu
    S[pozx,pozy+VYSKA/2,pozz] je stred panaka
    potom vektor v = S - A
  */ 
  i = gstatus->getActivePlayer();

  // ak hra UI, pouzivatel si nemoze vybrat panaka
  if (gstatus->getUserType(i) == true) return false;

  for (k = 0; k < 4; k++) {
      if (!this->canGo(i,k,result)) continue;
      ray = getMouseRay(mousex, mousey);
      
      // stred panaka
      S.x = playerPosition[i][k].x;
      S.y = playerPosition[i][k].y + HEIGHT/2.0;
      S.z = playerPosition[i][k].z;

      v.x = S.x - objx;
      v.y = S.y - objy;
      v.z = S.z - objz;
      
      /*  Ked mam vektor v, mam aj normalizovany vektor ray
          teraz vypocitam vzdialenost t
          t = v . ray
      */
      t = v.x * ray.x + v.y * ray.y + v.z * ray.z;
      
      /*  Teraz treba bod P, ktory je kolmym priemetom bodu S
          P = obj + t . ray
      */
      P.x = objx + t * ray.x;
      P.y = objy + t * ray.y;
      P.z = objz + t * ray.z;
      
      /* a zistenie, ci mys je nad panakom je tu:
         pre gulu:   vzdialenost |PS| < r
         pre kvader: ymag < VYSKA/2 AND xmag <= r AND zmag <= r
         pre valec:  ymag < VYSKA/2 AND sqrt(xmag * xmag + zmag * zmag) <= r
      */
      xmag = fabs(P.x - S.x);
      ymag = fabs(P.y - S.y);
      zmag = fabs(P.z - S.z);

      //  Polomer valca je najvacsia sirka panaka / 2
      if (ymag <= HEIGHT/2 && sqrt(xmag * xmag + zmag * zmag) <= BASE_RADIUS) {
          mouseOver[i][k] = status = true;
          this->selectedFigure = k;
      }
      else
          mouseOver[i][k] = false;
    }
    // pri kliknuti "vypnem" hracov
    if (isClicked == true)
      for (i = 0; i < gstatus->getGamePlayers(); i++)
          for (k = 0; k < 4; k++)
              mouseOver[i][k] = false;
    return (status && isClicked);
}

void Player::RedrawPlayers(Point3D scenePosition)
{
    int i,k;
	double ModelMatrix[16];
  
   	glGetDoublev(GL_MODELVIEW_MATRIX, ModelMatrix);
    
    glDisable(GL_TEXTURE_2D);
    glDisable(GL_BLEND);
    glDisable(GL_ALPHA_TEST);
    glClear(GL_DEPTH_BUFFER_BIT); // verim, ze toto vyriesi problem s vykreslovanim figuriek
    for (i = 0; i < gstatus->getGamePlayers(); i++)
        for (k = 0; k < 4; k++) {
          glLoadMatrixd(ModelMatrix);
          if (nowMoving && (i == movingPlayer) && (k == movingFigure)) {
              this->moveNext();
              glTranslatef(movingPosition.x, movingPosition.y,movingPosition.z);
          } else
              glTranslatef(playerPosition[i][k].x, playerPosition[i][k].y,playerPosition[i][k].z);
            switch (i) {
                case 0:  // cierna
                    glColor3f(0.2,0.2,0.2);
                    break;
                case 1:  // zlta
                    glColor3f(0.8,0.8,0.0);
                    break;
                case 2:  // fialova(6) modra(4)
                    if (gstatus->getGamePlayers() == 4)
                        glColor3f(0.0,0.0,0.6);
                    else
                        glColor3f(0.8,0.0,0.8);
                    break;
                case 3:  // zelena(6) cervena(4)
                    if (gstatus->getGamePlayers() == 4)
                        glColor3f(0.8,0.0,0.0);
                    else
                        glColor3f(0.0,0.8,0.0);
                    break;
                case 4:  // cervena
                    glColor3f(0.8,0.0,0.0);
                    break;
                case 5:  // modra
                    glColor3f(0.0,0.0,0.6);
                    break;
            }
            if (mouseOver[i][k] == true) glColor3f(1,1,1);
  	        glCallList(this->figure);
        }
    glLoadMatrixd(ModelMatrix);
}

/*
   implementacia statickej logiky - priprava hodu kockou
   hrac moze hadzat kockou ak:
    1. je na rade (to sa nerata, lebo funkcia pracuje iba s hracom, ktory je na rade)
    2. ma nejakeho aktivneho hraca (bud na hracej ploche, alebo v prvom domceku
    
    Dalej sa vypocita pocet hodov, ktore moze hrac mat (ked je iba v prvom domceku
    tak 3, ked je iba v cielovom domceku tak 0, inak 1)
    
    + ak je iba v cielovom domceku tak sa TU pripocita jeho vitazstvo
*/
bool Player::prepareThrow()
{
    int i, finals,j;
    bool w;
    
    if (gstatus->getGamePlayers() == 6) finals = 52;
    else finals = 44;
    
    for (i = 0, w = false; i < 4; i++)
        if (this->getPlayerPosition(gstatus->getActivePlayer(), i) < finals) {
            w = true;
            break;
        }
    if (w == false) {
        // hrac je uz doma komplet zo vsetkymi figurkami
        // je uz oznaceny v poradi vitazov ?
        if (!gstatus->getPlayerHome(gstatus->getActivePlayer()))
            gstatus->setPlayerHome(gstatus->getActivePlayer());
        gstatus->resetThrows(0);
        return false;
    }
    
    // ok, moze hadzat kockou. teraz vypocet poctu hodov
    // ak je len doma (v prvom alebo druhom domceku), ma 3 hody inak 1
    for (i = 0, w = false; i < 4; i++) {
        j = this->getPlayerPosition(gstatus->getActivePlayer(), i);
        if ((j > 3) && (j < finals)) {
            w = true;
            break;
        }
    }
    if (w) gstatus->resetThrows(1);
    else gstatus->resetThrows(3);
    return true;
}

// posle panaka domov do prveho domceka - najde volny a umiestni ho tam
void Player::goHome(int player, int figure)
{
    int i,j;
    bool can;
    for (i = 0; i < 4; i++) {
        can = true;
        for (j = 0; j < 4; j++)
            if (playerIndex[player][j] == i) {
                can = false;
                break;
            }
        if (can) {
            this->setPlayerPosition(player, figure, i);
            break;
        }
    }
}


/* implementacia dynamickej logiky
 podla pozicie hraca bud s nim ide alebo ho vyberie na inicialnu poziciu
 ak nastal vyhadzov, vrati true
*/
bool Player::goPlayer(int result)
{
    int i, ind, k,l,m;
    bool status = false;
    
    i = gstatus->getActivePlayer();
    
    ind = this->getPlayerPosition(i,selectedFigure);
    // ak je ind < 4, figurka je v domceku
    if (ind < 4) {
        if (this->getIndexCollision(i,selectedFigure,4)) {
            // nastal vyhadzov
            this->goHome(resultPlayer, resultFigure);
            status = true;
        }
        this->setPlayerPosition(i,selectedFigure, 4);
    }
    else {
        // inak ak je figurka na hracej ploche, treba vyriesit
        // aj kolizie - vyhadzovy
        k = ind + result;
        // mod o 1 viac, lebo moze byt aj 55 resp. 47
        if (gstatus->getGamePlayers() == 6) k %= 56;
        else k %= 48;
        if (this->getIndexCollision(i,selectedFigure,k)) {
            // nastal vyhadzov
            this->goHome(resultPlayer, resultFigure);
            status = true;
        }
        this->setPlayerPosition(i, selectedFigure, k);
    }
    return status;
}
    
/* Implementacia umelej inteligencie 
   a) najprv sa zisti, kolko figuriek je na hracej ploche
      1.uroven: ak nie je ziadny a result = 6 vyber niektoreho z domceka von
      2.uroven: ak nie je ziadny alebo je jeden hrac na hracej ploche ktory nestoji
                na indexe=4 a result = 6 vyber niektoreho z domceka von
      opusti proceduru
  
   b) ked su figurky na hracej ploche (mimo cieloveho domceka)
      1.uroven: ak je to mozne chod s panakom o result policok
      2.uroven: - vypocitaj cielove policko kazdej figurky na hracej ploche co moze ist
                - ak na niektorom je cudzia figurka (nastal by vyhadzov) tak vyber tu figurku
                - ak nie tak vyber figurku, ktora je blizsie k domceku
      opusti proceduru
  
   c) ak sa figurky nemozu hybat na hracej ploche a je ich dostatok
      vsetky urovne: - pokus sa vybrat figurku z domceka
                     - ak sa neda, posledna moznost musi byt pohnutie hraca v cielovom
                       domceku - zober prveho ktory sa moze pohnut
*/
bool Player::UISelectPlayer(int result)
{
    int UIlevel,i,j,k,min, tmp_count;
    int finals;
    bool w;
    
    if (gstatus->getGamePlayers() == 6) finals = 52;
    else finals = 44;
    UIlevel = gstatus->getFactor(gstatus->getActivePlayer());

    // a)
    if (result == 6) {
        for (i = tmp_count = 0; i < 4; i++)
            if (this->getPlayerPosition(gstatus->getActivePlayer(),i) > 3)
                tmp_count++;
        if (!tmp_count) {
            this->selectedFigure = 0;
            return this->goPlayer(result);
        }
        if (tmp_count == 1 && UIlevel == 1) {
            for (i = 0; i < 4; i++)
                if (this->getPlayerPosition(gstatus->getActivePlayer(),i)<4 
                    && this->canGo(gstatus->getActivePlayer(),i,result)) {
                    this->selectedFigure = i;
                    return this->goPlayer(result);
                }
        }
    }
    // b)
    if (!UIlevel) {
        // najde hraca na hracej ploche
        for (i = 0; i < 4; i++) {
            j = this->getPlayerPosition(gstatus->getActivePlayer(),i);
            if (j > 3 && j < finals && this->canGo(gstatus->getActivePlayer(),i,result)) {
                this->selectedFigure = i;
                return this->goPlayer(result);
            }
        }
    } else {
        // najprv zisti ci mozne figurky by niekoho vyhodili
        min = finals;
        w = false;
        for (i = 0; i < 4; i++) {
            j = this->getPlayerPosition(gstatus->getActivePlayer(),i);
            if (j > 3 && j < finals && this->canGo(gstatus->getActivePlayer(),i,result)) {
                w = true;
                // vypocet cieloveho policka
                k = j + result;
                // mod o 1 viac, lebo moze byt aj 55 resp. 47
                if (gstatus->getGamePlayers() == 6) k %= 56;
                else k %= 48;
                if (this->getIndexCollision(gstatus->getActivePlayer(),i,k)) {
                    // nastal by vyhadzov
                    this->selectedFigure = i;
                    return this->goPlayer(result);
                } else {
                  // ak nie tak vyberie tu blizsie k domceku
                  if (finals-j < min) {
                        this->selectedFigure = i;
                        min = finals-j;
                  }
                }
            }
        }
        // ide sa s figurkou blizsie k domceku
        if (w) return this->goPlayer(result);
    }
    // c) - posledna moznost, vsetky UI urovne
    if (result == 6)
        // najprv pokus vybrat panaka
        for (i = 0; i < 4; i++)
            if (this->getPlayerPosition(gstatus->getActivePlayer(), i) < 4
                && this->canGo(gstatus->getActivePlayer(),i,result)) {
                this->selectedFigure = i;
                return this->goPlayer(result);
            }
    // inak pokus vybrat hocijakeho co sa da...
    for (i = 0; i < 4; i++)
        if (this->canGo(gstatus->getActivePlayer(),i,result)) {
            this->selectedFigure = i;
            return this->goPlayer(result);
        }
    return false;
}

