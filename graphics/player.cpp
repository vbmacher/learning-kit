/* 
 * File:   Player.cpp
 * Author: vbmacher
 * 
 * Created on Nedeľa, 2013, február 24, 16:05
 */

#include "player.h"
#include "canvas.h"

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

        const Component* Player::collision(Uint16 colX, Uint16 colY, Uint16 radius) {
            if (colX <= (x - radius) || colX >= (x + WIDTH + radius)) {
                return NULL;
            }
            if (colY <= (y - radius) || colY >= (y + HEIGHT + radius)) {
                return NULL;
            }
            return this;
        }

    }
}