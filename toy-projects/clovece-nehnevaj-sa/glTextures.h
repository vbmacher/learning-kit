/*
 *  TECHNICKÁ UNIVERZITA KOŠICE
 *  FAKULTA ELETKROTECHNIKY A INFORMATIKY
 *  KATEDRA POÈÍTAÈOV
 * ---------------------------------------
 *  Èloveèe, nehnevaj sa !
 * ---------------------------------------
 *  glTextures.h
 *
 * (c) Copyright 2006, P. Jakubèo, E.Danková, O.Drusa
 *
 */

#ifndef __GLTEXTURES_H__
#define __GLTEXTURES_H__

	#include <windows.h>    // Hlavièkový soubor pro Windows
	#include <gl\gl.h>      // Hlavièkový soubor pro OpenGL32 knihovnu

	#define textureCount 6
	#define font_height 23
    #define font_space 10
	
	extern GLuint texture[textureCount];
    extern GLuint font_base;

	AUX_RGBImageRec *texLoadBMP(char *Filename);
	bool texLoadTextures();
    void deleteFont();

#endif
