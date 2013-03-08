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

        Wall::Wall(Uint16 x, Uint16 y, Uint16 width, Uint16 height, bool goalKeeper) : x(x), y(y), width(width), height(height)
        , goalKeeper(goalKeeper), hits(0) {
        }

        bool Wall::isCollision(Uint16 colX, Uint16 colY) {
            if (colX < (x - Ball::RADIUS) || colX > ((x+width) + Ball::RADIUS)) {
                return false;
            }
            if (colY < (y - Ball::RADIUS) || colY > ((y+height) + Ball::RADIUS)) {
                return false;
            }
            return true;
        }
        
        void Wall::actionIfCollision(Ball &ball) {
            if (isCollision(ball.getX(), ball.getY())) {
                //     double influence = (45 * (colY - (y+HEIGHT/2)) / (HEIGHT/2)) * M_PI / 180;
                ball.changeAngle(0);
                hits++;
            }

            // just to be sure that the ball is out of the borders
            do {
                ball.moveAhead();
            } while (isCollision(ball.getX(), ball.getY()));
        }
    }
}
