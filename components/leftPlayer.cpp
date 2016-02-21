/*
 * File:   leftPlayer.cpp
 * Author: vbmacher
 *
 * Created on Sobota, 2013, marec 23, 10:13
 */

#include "leftPlayer.h"

namespace github {
    namespace pong {

        LeftPlayer::LeftPlayer(const std::string& name, Uint16 x, Uint16 y, Uint16 yMin, Uint16 yMax, Uint16 collisionTolerance)
                : Player(name, x, y, yMin, yMax, collisionTolerance)
        {}

        bool LeftPlayer::isCollision(Uint16 colX, Uint16 colY) {
            if (colX >= (x + WIDTH + collisionTolerance)) {
                return false;
            }
            double myY = y;
            if (colY <= (myY - collisionTolerance) || colY >= (myY + HEIGHT + collisionTolerance)) {
                return false;
            }
            return true;
        }

    }
}
