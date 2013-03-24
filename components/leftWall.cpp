/* 
 * File:   leftWall.cpp
 * Author: vbmacher
 * 
 * Created on Sobota, 2013, marec 23, 10:50
 */

#include "leftWall.h"
#include "ball.h"

namespace github {
    namespace pong {

        bool LeftWall::isCollision(Uint16 colX, Uint16 colY) const {
            if (colX > (x + Ball::RADIUS)) {
                return false;
            }
            return true;
        }

    }
}
