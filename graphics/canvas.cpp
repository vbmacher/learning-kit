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
            SDL_WM_SetCaption("Pong Game", "Pong Game Lesson");
            colorBlack = SDL_MapRGB(screen->format, 0, 0, 0);
            colorWhite = SDL_MapRGB(screen->format, 0xFF, 0xFF, 0xFF);
        }

        Canvas::~Canvas() {
            try {
                SDL_Quit();
            } catch (...) {
            }
        }

#define setPixel(x, y) bufp = (Uint16 *) screen->pixels + (y) * screen->pitch / 2 + (x); \
                       *bufp = color

        void Canvas::drawPixel(Uint16 x, Uint16 y, Uint32 color) {
            if (SDL_MUSTLOCK(screen)) {
                if (SDL_LockSurface(screen) < 0) {
                    return;
                }
            }
            Uint16 *bufp;
            setPixel(x, y);

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
            Uint16 *bufp;

            // Top line
            for (Uint16 xx = x; xx < right_x; xx++) {
                setPixel(xx, y);
            }

            // Left line
            for (Uint16 yy = y; yy < bottom_y; yy++) {
                setPixel(x, yy);
            }

            // Bottom line
            for (Uint16 xx = x; xx < right_x; xx++) {
                setPixel(xx, bottom_y);
            }

            // Right line
            for (Uint16 yy = y; yy < bottom_y; yy++) {
                setPixel(right_x, yy);
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
            Uint16 *bufp;
            for (int i = x; i < right_x; i++) {
                for (int j = y; j < bottom_y; j++) {
                    setPixel(i, j);
                }
            }

            if (SDL_MUSTLOCK(screen)) {
                SDL_UnlockSurface(screen);
            }
        }

        void Canvas::dottedLine(Uint16 x0, Uint16 y0, Uint16 x1, Uint16 y1, Uint32 color, int dotGap) {
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
                Uint16 *bufp;
                if (!dontPixel) {
                    setPixel(x0, y0);
                }
                if (dotGap) {
                    dontPixel = (dontPixel + 1) % dotGap;
                }

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

        void Canvas::fillLine(Uint16 x0, Uint16 y0, Uint16 x1, Uint16 y1, Uint16 size, Uint32 color) {
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

            do {
                Uint16 *bufp;
                for (Uint16 xs = 0; xs < size; xs++) {
                    for (Uint16 ys = 0; ys < size; ys++) {
                        setPixel(x0 + xs, y0 + ys);
                    }
                }

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

        void Canvas::drawCircle(Uint16 x0, Uint16 y0, Uint16 radius, Uint32 color) {
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

        void Canvas::fillCircle(Uint16 x0, Uint16 y0, Uint16 radius, Uint32 color) {
            int f = 1 - radius;
            int ddF_x = 1;
            int ddF_y = -2 * radius;
            int x = 0;
            int y = radius;

            Uint16 *bufp;

            line(x0, y0 - radius, x0, y0 + radius, color);
            line(x0 - radius, y0, x0 + radius, y0, color);

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

                line(x0 - x, y0 + y, x0 + x, y0 + y, color);
                line(x0 - x, y0 - y, x0 + x, y0 - y, color);
                line(x0 - y, y0 + x, x0 + y, y0 + x, color);
                line(x0 - y, y0 - x, x0 + y, y0 - x, color);
            }

            if (SDL_MUSTLOCK(screen)) {
                SDL_UnlockSurface(screen);
            }
        }

#undef setPixel

#define dotProduct(x0,y0,x1,y1) ((x0) * (x1) + (y0) * (y1))

        bool Canvas::inTriangle(double x0, double y0, double x1, double y1, double x2, double y2, double px, double py) {
            double dot00 = dotProduct(x2 - x0, y2 - y0, x2 - x0, y2 - y0);
            double dot01 = dotProduct(x2 - x0, y2 - y0, x1 - x0, y1 - y0);
            double dot02 = dotProduct(x2 - x0, y2 - y0, px - x0, py - y0);
            double dot11 = dotProduct(x1 - x0, y1 - y0, x1 - x0, y1 - y0);
            double dot12 = dotProduct(x1 - x0, y1 - y0, px - x0, py - y0);

            // Compute barycentric coordinates
            double invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
            double u = (dot11 * dot02 - dot01 * dot12) * invDenom;
            double v = (dot00 * dot12 - dot01 * dot02) * invDenom;

            // Check if point is in triangle
            return (u >= 0) && (v >= 0) && (u + v < 1);
        }

#undef dotProduct

        float Canvas::sqrt7(float x) {
            unsigned int i = *(unsigned int*) &x;
            // adjust bias
            i += 127 << 23;
            // approximation of square root
            i >>= 1;
            return *(float*) &i;
        }

    }
}


