/* 
 * File:   Player.cpp
 * Author: vbmacher
 * 
 * Created on Nedeľa, 2013, február 24, 16:05
 */

#include <math.h>
#include <iostream>

#include "player.h"
#include "canvas.h"
#include "ball.h"

namespace github {

    namespace pong {

        const Uint16 Player::WIDTH = 10;
        const Uint16 Player::HEIGHT = 100;
        
        Player::Player(std::string name, Uint16 x, Uint16 y, Uint16 yMin, Uint16 yMax) 
                : name(name), x(x), y(y), minY(yMin), maxY(yMax), velocityY(0) {
        }

        void Player::draw(Canvas &canvas) {
            canvas.fillRect(x, y, x + WIDTH, y + HEIGHT);
        }

        void Player::move(Uint16 new_x, Uint16 new_y) {
            if (checkY(new_y)) {
                velocityY = 15.0 * ((double)new_y - (double)y) / (double)(maxY - HEIGHT);
                y = new_y;
            }
        }
        
        bool Player::isCollision(Uint16 colX, Uint16 colY) {
            if (colX <= ((double)x - Ball::RADIUS) || colX >= (x + WIDTH + Ball::RADIUS)) {
                return false;
            }
            if (colY <= ((double)y - Ball::RADIUS) || colY >= (y + HEIGHT + Ball::RADIUS)) {
                return false;
            }
            return true;
        }

        void Player::actionIfCollision(Ball &ball) {
            if (isCollision(ball.getX(), ball.getY())) {
                double half = (double)y + HEIGHT/2;
                double influence = (30 * (ball.getY() - half) / (HEIGHT/2)) * M_PI / 180;
                ball.changeAngle(influence, velocityY);
            }

            // just to be sure that the ball is out of the borders
            do {
                ball.moveAhead();
            } while (isCollision(ball.getX(), ball.getY()));
        }

    }
}