/* 
 * File:   Wall.cpp
 * Author: vbmacher
 * 
 * Created on Pondelok, 2013, február 25, 21:25
 */

#include <boost/shared_ptr.hpp>

#include "wall.h"
#include "ball.h"

namespace github {
    namespace pong {

        Wall::Wall(Uint16 x0, Uint16 y0, Uint16 x1, Uint16 y1, bool goalKeeper) : x0(x0), y0(y0), x1(x1), y1(y1)
        , goalKeeper(goalKeeper), hits(0) {
        }

        void Wall::actionIfCollision(Ball &ball) {
            Uint16 colX = ball.getX();
            Uint16 colY = ball.getY();
            
            if (colX < (x0 - Ball::RADIUS) || colX > (x1 + Ball::RADIUS)) {
                return;
            }
            if (colY < (y0 - Ball::RADIUS) || colY > (y1 + Ball::RADIUS)) {
                return;
            }
            hits++;
            ball.changeAngle();
        }
    }
}
