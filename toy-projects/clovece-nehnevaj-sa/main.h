/*
 *  TECHNICKÁ UNIVERZITA KOŠICE
 *  FAKULTA ELETKROTECHNIKY A INFORMATIKY
 *  KATEDRA POÈÍTAÈOV
 * ---------------------------------------
 *  Èloveèe, nehnevaj sa !
 * ---------------------------------------
 *  main.h
 *
 * (c) Copyright 2006, P. Jakubèo, E.Danková, O.Drusa
 *
 */

#ifndef __MAIN_H__
#define __MAIN_H__

// Deklarace procedury okna (funkèní prototyp)
LRESULT CALLBACK WndProc(HWND, UINT, WPARAM, LPARAM);

typedef struct {
	int x;
	int y;
} Point;

typedef struct {
	double x;
	double y;
	double z;
} Point3D;

typedef struct {
	double x;
	double y;
	double z;
} Vector3D;


typedef struct {
        int width;
        int height;
} Size;

typedef struct {
        int x;
        int y;
        int width;
        int height;
} PointSize;

#endif
