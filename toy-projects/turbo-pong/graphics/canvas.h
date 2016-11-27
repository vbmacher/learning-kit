/*
 * File:   SDLCanvas.h
 * Author: vbmacher
 *
 * Created on Nedeľa, 2013, február 24, 10:21
 */

#ifndef CANVAS_H
#define	CANVAS_H

#include <boost/noncopyable.hpp>

#include <SDL.h>
#include "SDL_endian.h"

#if SDL_BYTEORDER == SDL_LIL_ENDIAN
#define SWAP16(X)    (X)
#define SWAP32(X)    (X)
#else
#define SWAP16(X)    SDL_Swap16(X)
#define SWAP32(X)    SDL_Swap32(X)
#endif

namespace github {
    namespace pong {

        class Canvas : private boost::noncopyable {
            SDL_Surface *screen;
            Uint32 colorWhite;
            Uint32 colorBlack;
            unsigned int width;
            unsigned int height;

        public:
            Canvas(int width, int height);
            ~Canvas();

            static bool inTriangle(double x0, double y0, double x1, double y1, double x2, double y2,
                double px, double py);

            static float sqrt7(float x);

            void updateScreen() {
                SDL_UpdateRect(screen, 0, 0, width, height);
            }

            void clearScreen() {
                fillRect(0, 0, width, height, colorBlack);
            }

            void drawPixel(Uint16 x, Uint16 y) {
                drawPixel(x, y, colorWhite);
            }

            void drawPixel(Uint16 x, Uint16 y, Uint8 R, Uint8 G, Uint8 B) {
                Uint32 color = SDL_MapRGB(screen->format, R, G, B);
                drawPixel(x, y, color);
            }

            void drawPixel(Uint16 x, Uint16 y, Uint32 color);

            void drawRect(Uint16 x, Uint16 y, Uint16 right_x, Uint16 bottom_y, Uint32 color);

            void drawRect(Uint16 x, Uint16 y, Uint16 right_x, Uint16 bottom_y) {
                drawRect(x, y, right_x, bottom_y, colorWhite);
            }

            void fillLine(Uint16 x0, Uint16 y0, Uint16 x1, Uint16 y1, Uint16 width) {
                fillLine(x0, y0, x1, y1, width, colorWhite);
            }

            void fillLine(Uint16 x0, Uint16 y0, Uint16 x1, Uint16 y1, Uint16 width, Uint32 color);

            void line(Uint16 x0, Uint16 y0, Uint16 x1, Uint16 y1) {
                dottedLine(x0, y0, x1, y1, colorWhite, 0);
            }

            void line(Uint16 x0, Uint16 y0, Uint16 x1, Uint16 y1, Uint32 color) {
                dottedLine(x0, y0, x1, y1, color, 0);
            }

            void dottedLine(Uint16 x0, Uint16 y0, Uint16 x1, Uint16 y1) {
                dottedLine(x0, y0, x1, y1, colorWhite, 5);
            }

            void dottedLine(Uint16 x0, Uint16 y0, Uint16 x1, Uint16 y1, Uint32 color) {
                dottedLine(x0, y0, x1, y1, color, 5);
            }

            void dottedLine(Uint16 x0, Uint16 y0, Uint16 x1, Uint16 y1, Uint32 color, int dotGap);

            void fillRect(Uint16 x, Uint16 y, Uint16 right_x, Uint16 bottom_y, Uint32 color);

            void fillRect(Uint16 x, Uint16 y, Uint16 right_x, Uint16 bottom_y) {
                fillRect(x, y, right_x, bottom_y, colorWhite);
            }

            void drawCircle(Uint16 x, Uint16 y, Uint16 radius) {
                drawCircle(x, y, radius, colorWhite);
            }

            void drawCircle(Uint16 x, Uint16 y, Uint16 radius, Uint32 color);

            void fillCircle(Uint16 x, Uint16 y, Uint16 radius) {
                fillCircle(x, y, radius, colorWhite);
            }

            void fillCircle(Uint16 x, Uint16 y, Uint16 radius, Uint32 color);

            int getWidth() const { return width; }

            int getHeight() const { return height; }

        };

    }
}
#endif	/* CANVAS_H */

