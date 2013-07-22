/*
 *  TECHNICKÁ UNIVERZITA KOŠICE
 *  FAKULTA ELETKROTECHNIKY A INFORMATIKY
 *  KATEDRA POÈÍTAÈOV
 * ---------------------------------------
 *  Èloveèe, nehnevaj sa !
 * ---------------------------------------
 *  glTextures.cpp
 *
 * (c) Copyright 2006, P. Jakubèo, E.Danková, O.Drusa
 *
 * Praca s texturami - nacitanie, prevod bitmapy atd..
 */

// potrebne kniznice
#pragma comment (lib,"opengl32.lib")
#pragma comment (lib,"glu32.lib")
#pragma comment (lib,"glaux.lib")

#include <windows.h>		// Header File For Windows
#include <math.h>			// Header File For Windows Math Library
#include <stdio.h>			// Header File For Standard Input/Output
#include <gl\gl.h>			// Header File For The OpenGL32 Library
#include <gl\glu.h>			// Header File For The GLu32 Library
#include <gl\glaux.h>		// Header File For The Glaux Library

#include "glTextures.h"

GLuint texture[textureCount];
GLuint font_base;  // display list znakov fontu

AUX_RGBImageRec *texLoadBMP(char *Filename)
{
	FILE *File = NULL;
    if (!Filename) return NULL;
    
	File=fopen(Filename,"r");
    if (File) {
		fclose(File);
        return auxDIBImageLoad(Filename);
    }
    return NULL;
}

void buildFont()
{
	float cx; // Holds Our X Character Coord
	float cy; // Holds Our Y Character Coord
	GLuint loop;

	font_base = glGenLists(256); // Creating 256 Display Lists
	for (loop=0; loop<256; loop++) {
		cx=float(loop%16)/16.0f; // X Position Of Current Character
		cy=float(loop/16)/16.0f; // Y Position Of Current Character

		glNewList(font_base+loop,GL_COMPILE); // Start Building A List
			glBegin(GL_QUADS);	// Use A Quad For Each Character
				glTexCoord2f(cx,1-cy-0.0625f);	// Texture Coord (Bottom Left)
				glVertex2i(0,0); // Vertex Coord (Bottom Left)
				glTexCoord2f(cx+0.0625f,1-cy-0.0625f);	// Texture Coord (Bottom Right)
				glVertex2i(font_height,0);	// Vertex Coord (Bottom Right)
				glTexCoord2f(cx+0.0625f,1-cy); // Texture Coord (Top Right)
				glVertex2i(font_height,font_height); // Vertex Coord (Top Right)
				glTexCoord2f(cx,1-cy);	// Texture Coord (Top Left)
				glVertex2i(0,font_height); // Vertex Coord (Top Left)
			glEnd();		// Done Building Our Quad (Character)
			glTranslated(font_space,0,0); // Move To The Right Of The Character
		glEndList(); // Done Building The Display List
	}
}

void deleteFont()
{
    glDeleteLists(font_base, 256);
}

bool texLoadTextures()
{
	bool Status = false;
	GLuint loop;
    AUX_RGBImageRec *TextureImage[textureCount];

    memset(TextureImage,0,sizeof(void *)*textureCount);
    if ((TextureImage[0] = texLoadBMP("data/font1.bmp"))
         && (TextureImage[1] = texLoadBMP("data/logo.bmp"))
         && (TextureImage[2] = texLoadBMP("data/4players.bmp"))
         && (TextureImage[3] = texLoadBMP("data/6players.bmp"))
         && (TextureImage[4] = texLoadBMP("data/font2.bmp"))
         && (TextureImage[5] = texLoadBMP("data/cube.bmp"))) {
		Status=TRUE;
        glGenTextures(textureCount, &texture[0]); // 2 textures
		for (loop=0; loop<textureCount; loop++) {
			glBindTexture(GL_TEXTURE_2D, texture[loop]);
			glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
			glTexImage2D(GL_TEXTURE_2D, 0, 3, TextureImage[loop]->sizeX, TextureImage[loop]->sizeY, 0, GL_RGB, GL_UNSIGNED_BYTE, TextureImage[loop]->data);
		}
    }
	for (loop=0; loop<textureCount; loop++)
		if (TextureImage[loop]) {
			if (TextureImage[loop]->data)
		        free(TextureImage[loop]->data);
			free(TextureImage[loop]);
		}
	if (Status)
	    buildFont();
    return Status;
}
