/*
 * File:   Player.cpp
 * Author: vbmacher
 *
 * Created on Nedeľa, 2013, február 24, 16:05
 */

#include <math.h>
#include <ctime>
#include <SDL/SDL.h>

#include "../graphics/canvas.h"

#include "player.h"
#include "ball.h"

#ifdef _DEBUG
#include <iostream>
#endif

namespace github {

    namespace pong {

        const Uint16 Player::WIDTH = 10;
        const Uint16 Player::HEIGHT = 100;

        Player::Player(std::string name, Uint16 x, Uint16 y, Uint16 yMin, Uint16 yMax, Uint16 collisionTolerance)
                : Component(collisionTolerance), name(name), x(x), y(y), yMin(yMin), yMax(yMax), velocityY(0),
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
            Uint16 _y = y;
            double diffY = (_y - lastY) / 50.0;
            lastY = _y;

            velocityY = diffY / (time - lastDrawTime + 1);

#ifdef _DEBUG
                std::cout << "[" << name << "] velocityY = " << velocityY << "; diffY = " << diffY << std::endl;
#endif

            lastDrawTime = time;
        }

        void Player::actionIfCollision(Ball &ball) {
            Uint16 ballY = ball.getY();
            if (isCollision(ball.getX(), ballY)) {
                Uint16 _y = y;
                double halfY = _y + HEIGHT/2;
                double influence = (30 * (ballY - halfY) / (HEIGHT/2)) * M_PI / 180;

                ball.changeAngle(influence, velocityY);
            }
        }

    }
}