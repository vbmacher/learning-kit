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

        Wall::Wall(Uint16 collisionTolerance) : Component(collisionTolerance), goal(false) {
        }

        void Wall::actionIfCollision(Ball &ball) {
            if (isCollision(ball.getX(), ball.getY())) {
                ball.changeAngle(0,0);
                goal = true;
            }
        }
    }
}
