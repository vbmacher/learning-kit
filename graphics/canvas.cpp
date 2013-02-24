/* 
 * File:   SDLCanvas.cpp
 * Author: vbmacher
 * 
 * Created on Nedeľa, 2013, február 24, 10:21
 */
#include <stdexcept>
#include <string>
#include <iostream>

#include <boost/lexical_cast.hpp>

#include <SDL.h>

#include "canvas.h"

namespace github {
    namespace pong {

        Canvas::Canvas(int width, int height) : width(width), height(height) {
            assert(width > 0);
            assert(height > 0);
            
            if (SDL_Init(SDL_INIT_AUDIO | SDL_INIT_VIDEO) < 0) {
                throw std::runtime_error("Could not initialize SDL: " + std::string(SDL_GetError()));
            }
            screen = SDL_SetVideoMode(width, height, 16, SDL_SWSURFACE | SDL_RESIZABLE | SDL_DOUBLEBUF);
            if (screen == NULL) {
                throw std::runtime_error("Could not setup video mode " + boost::lexical_cast<std::string>(width) + "x"
                        + boost::lexical_cast<std::string>(height) + ": " + std::string(SDL_GetError()));
            }
            colorBlack = SDL_MapRGB(screen->format, 0, 0, 0);
            colorWhite = SDL_MapRGB(screen->format, 0xFF, 0xFF, 0xFF);
        }
        
        Canvas::~Canvas() {
            try {
                SDL_Quit();
            } catch (...) {
            }
        }

        void Canvas::drawPixel(Uint16 x, Uint16 y, Uint32 color) {
            if (SDL_MUSTLOCK(screen)) {
                if (SDL_LockSurface(screen) < 0) {
                    return;
                }
            }
            Uint16 *bufp = (Uint16 *) screen->pixels + y * screen->pitch / 2 + x;
            *bufp = color;
            if (SDL_MUSTLOCK(screen)) {
                SDL_UnlockSurface(screen);
            }
        }

        void Canvas::drawRect(Uint16 x, Uint16 y, Uint16 right_x, Uint16 bottom_y, Uint32 color) {
            if (SDL_MUSTLOCK(screen)) {
                if (SDL_LockSurface(screen) < 0) {
                    return;
                }
            }

            // Top line
            for (Uint16 xx = x; xx < right_x; xx++) {
                Uint16 *bufp = (Uint16 *) screen->pixels + y * screen->pitch / 2 + xx;
                *bufp = color;
            }

            // Left line
            for (Uint16 yy = y; yy < bottom_y; yy++) {
                Uint16 *bufp = (Uint16 *) screen->pixels + yy * screen->pitch / 2 + x;
                *bufp = color;
            }

            // Bottom line
            for (Uint16 xx = x; xx < right_x; xx++) {
                Uint16 *bufp = (Uint16 *) screen->pixels + bottom_y * screen->pitch / 2 + xx;
                *bufp = color;
            }

            // Right line
            for (Uint16 yy = y; yy < bottom_y; yy++) {
                Uint16 *bufp = (Uint16 *) screen->pixels + yy * screen->pitch / 2 + right_x;
                *bufp = color;
            }

            if (SDL_MUSTLOCK(screen)) {
                SDL_UnlockSurface(screen);
            }
        }
        
        void Canvas::fillRect(Uint16 x, Uint16 y, Uint16 right_x, Uint16 bottom_y, Uint32 color) {
            if (SDL_MUSTLOCK(screen)) {
                if (SDL_LockSurface(screen) < 0) {
                    return;
                }
            }
            for (int i = x; i < right_x; i++) {
                for (int j = y; j < bottom_y; j++) {
                    Uint16 *bufp = (Uint16 *) screen->pixels + j * screen->pitch / 2 + i;
                    *bufp = color;
                }
            }
            
            if (SDL_MUSTLOCK(screen)) {
                SDL_UnlockSurface(screen);
            }
        }

        void Canvas::dottedLine(Uint16 x0, Uint16 y0, Uint16 x1, Uint16 y1, Uint32 color) {
            Sint16 dx = abs((Sint16) x1 - (Sint16) x0);
            Sint16 dy = abs((Sint16) y1 - (Sint16) y0);
            Sint16 sx, sy;
            if (x0 < x1) {
                sx = 1;
            } else {
                sx = -1;
            }

            if (y0 < y1) {
                sy = 1;
            } else {
                sy = -1;
            }
            Sint16 err = dx - dy;

            if (SDL_MUSTLOCK(screen)) {
                if (SDL_LockSurface(screen) < 0) {
                    return;
                }
            }
            
            unsigned char dontPixel = 0;

            do {
                if (!dontPixel) {
                    Uint16 *bufp = (Uint16 *) screen->pixels + y0 * screen->pitch / 2 + x0;
                    *bufp = color;
                }
                dontPixel = (dontPixel + 1) % 5;

                if ((x0 == x1) && (y0 == y1)) {
                    break;
                }
                Sint16 e2 = 2 * err;
                if (e2 > -dy) {
                    err = err - dy;
                    x0 = x0 + sx;
                }
                if (e2 < dx) {
                    err = err + dx;
                    y0 = y0 + sy;
                }
            } while (1);

            if (SDL_MUSTLOCK(screen)) {
                SDL_UnlockSurface(screen);
            }
        }
        
#define setPixel(x, y) bufp = (Uint16 *) screen->pixels + (y) * screen->pitch / 2 + (x); \
                       *bufp = color;


        void Canvas::fillCircle(Uint16 x0, Uint16 y0, Uint16 radius, Uint32 color) {
            int f = 1 - radius;
            int ddF_x = 1;
            int ddF_y = -2 * radius;
            int x = 0;
            int y = radius;
            
            if (SDL_MUSTLOCK(screen)) {
                if (SDL_LockSurface(screen) < 0) {
                    return;
                }
            }
            Uint16 *bufp;
            
            setPixel(x0, y0 + radius);
            setPixel(x0, y0 - radius);
            setPixel(x0 + radius, y0);
            setPixel(x0 - radius, y0);

            while (x < y) {
                // ddF_x == 2 * x + 1;
                // ddF_y == -2 * y;
                // f == x*x + y*y - radius*radius + 2*x - y + 1;
                if (f >= 0) {
                    y--;
                    ddF_y += 2;
                    f += ddF_y;
                }
                x++;
                ddF_x += 2;
                f += ddF_x;
                setPixel(x0 + x, y0 + y);
                setPixel(x0 - x, y0 + y);
                setPixel(x0 + x, y0 - y);
                setPixel(x0 - x, y0 - y);
                setPixel(x0 + y, y0 + x);
                setPixel(x0 - y, y0 + x);
                setPixel(x0 + y, y0 - x);
                setPixel(x0 - y, y0 - x);
            }
            
            if (SDL_MUSTLOCK(screen)) {
                SDL_UnlockSurface(screen);
            }
        }
#undef setPixel
    }
}


