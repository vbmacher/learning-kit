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
            movingVectorY = 1;
        }

        void Player::draw(Canvas &canvas) {
            canvas.fillRect(x, y, x + WIDTH, y + HEIGHT);
        }

        void Player::move(Uint16 new_x, Uint16 new_y) {
            if (checkY(new_y)) {
                if (y < new_y) {
                    movingVectorY = 1;
                } else {
                    movingVectorY = 0;
                }
                y = new_y;
            }
        }

        bool Player::collision(Uint16 colX, Uint16 colY) {
            Uint16 circleDistanceX = Canvas::abs((Sint16)(colX - x));
            Uint16 circleDistanceY = abs((Sint16)(colY - y));

            if (circleDistanceX > (WIDTH / 2 + 5)) {
                return false;
            }
            if (circleDistanceY > (HEIGHT / 2 + 5)) {
                return false;
            }

            if (circleDistanceX <= (WIDTH / 2)) {
                return true;
            }
            if (circleDistanceY <= (HEIGHT / 2)) {
                return true;
            }

            Uint16 cornerDistance_sq = (circleDistanceX - WIDTH / 2)^2 +
                    (circleDistanceY - HEIGHT / 2)^2;

            return (cornerDistance_sq <= (5^2));
        }

    }
}