/*
 * File:   bottomWall.cpp
 * Author: vbmacher
 *
 * Created on Sobota, 2013, marec 23, 10:32
 */

#include "bottomWall.h"

namespace github {
    namespace pong {

        bool BottomWall::isCollision(Uint16 colX, Uint16 colY) const {
            if (colY < (y - collisionTolerance)) {
                return false;
            }
            return true;
        }

    }
}
