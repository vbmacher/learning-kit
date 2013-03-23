/*
 * File:   Player.cpp
 * Author: vbmacher
 *
 * Created on Nedeľa, 2013, február 24, 16:05
 */

#include <math.h>
#include <ctime>
#include <SDL/SDL.h>

#include "player.h"
#include "canvas.h"
#include "ball.h"

#ifdef _DEBUG
#include <iostream>
#endif

namespace github {

    namespace pong {

        const Uint16 Player::WIDTH = 10;
        const Uint16 Player::HEIGHT = 100;

        Player::Player(std::string name, Uint16 x, Uint16 y, Uint16 yMin, Uint16 yMax, Uint16 collisionTolerance)
                : Component(collisionTolerance), name(name), x(x), y(y), minY(yMin), maxY(yMax), velocityY(0),
                lastDrawTime(0), lastY(0) {
        }

        void Player::draw(Canvas &canvas) {
            canvas.fillRect(x, y, x + WIDTH, y + HEIGHT);

            // update velocity
            computeVelocity();
        }

        void Player::move(Uint16 new_x, Uint16 new_y) {
            if (checkY(new_y)) {
                y = new_y;
            }
        }

        void Player::computeVelocity() {
            Uint32 time = SDL_GetTicks();
            Uint16 myY = y;
            Sint16 diffY = myY - lastY;
            lastY = myY;

            velocityY = (diffY / (time - lastDrawTime + 1)) % 5;

            lastDrawTime = time;
        }

        void Player::actionIfCollision(Ball &ball) {
            if (isCollision(ball.getX(), ball.getY())) {
                double half = (double)y + HEIGHT/2;
                double influence = (30 * (ball.getY() - half) / (HEIGHT/2)) * M_PI / 180;

#ifdef _DEBUG
                std::cout << "[" << name << "] velocityY = " << velocityY << std::endl;
#endif
                ball.changeAngle(influence, velocityY);
            }
        }

    }
}