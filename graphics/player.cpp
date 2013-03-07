/* 
 * File:   Player.cpp
 * Author: vbmacher
 * 
 * Created on Nedeľa, 2013, február 24, 16:05
 */

#include <boost/enable_shared_from_this.hpp>

#include "player.h"
#include "canvas.h"
#include "ball.h"

namespace github {

    namespace pong {

        Player::Player(std::string name, Uint16 x, Uint16 y, Uint16 yMin, Uint16 yMax) 
                : name(name), x(x), y(y), minY(yMin), maxY(yMax) {
        }

        void Player::draw(Canvas &canvas) {
            canvas.fillRect(x, y, x + WIDTH, y + HEIGHT);
        }

        void Player::move(Uint16 new_x, Uint16 new_y) {
            if (checkY(new_y)) {
                y = new_y;
            }
        }

        void Player::actionIfCollision(Ball &ball) {
            Uint16 colX = ball.getX();
            Uint16 colY = ball.getY();
            if (colX <= (x - Ball::RADIUS) || colX >= (x + WIDTH + Ball::RADIUS)) {
                return;
            }
            if (colY <= (y - Ball::RADIUS) || colY >= (y + HEIGHT + Ball::RADIUS)) {
                return;
            }
            // We have collision
            ball.changeAngle();
        }

    }
}