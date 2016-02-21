/* 
 * File:   upperWall.cpp
 * Author: vbmacher
 * 
 * Created on Sobota, 2013, marec 23, 10:32
 */

#include "upperWall.h"
#include "ball.h"

namespace github {
    namespace pong {

        bool UpperWall::isCollision(Uint16 colX, Uint16 colY) const {
            if (colY > (y + Ball::RADIUS)) {
                return false;
            }
            return true;
        }

    }
}



