/*
 *  TECHNICK� UNIVERZITA KO�ICE
 *  FAKULTA ELETKROTECHNIKY A INFORMATIKY
 *  KATEDRA PO��TA�OV
 * ---------------------------------------
 *  �love�e, nehnevaj sa !
 * ---------------------------------------
 *  glTextures.h
 *
 * (c) Copyright 2006, P. Jakub�o, E.Dankov�, O.Drusa
 *
 */

#ifndef __GLTEXTURES_H__
#define __GLTEXTURES_H__

	#include <windows.h>    // Hlavi�kov� soubor pro Windows
	#include <gl\gl.h>      // Hlavi�kov� soubor pro OpenGL32 knihovnu

	#define textureCount 6
	#define font_height 23
    #define font_space 10
	
	extern GLuint texture[textureCount];
    extern GLuint font_base;

	AUX_RGBImageRec *texLoadBMP(char *Filename);
	bool texLoadTextures();
    void deleteFont();

#endif
