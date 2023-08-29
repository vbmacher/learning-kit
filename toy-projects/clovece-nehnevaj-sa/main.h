/*
 *  TECHNICK� UNIVERZITA KO�ICE
 *  FAKULTA ELETKROTECHNIKY A INFORMATIKY
 *  KATEDRA PO��TA�OV
 * ---------------------------------------
 *  �love�e, nehnevaj sa !
 * ---------------------------------------
 *  main.h
 *
 * (c) Copyright 2006, P. Jakub�o, E.Dankov�, O.Drusa
 *
 */

#ifndef __MAIN_H__
#define __MAIN_H__

// Deklarace procedury okna (funk�n� prototyp)
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
