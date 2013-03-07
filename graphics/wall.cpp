/* 
 * File:   Wall.cpp
 * Author: vbmacher
 * 
 * Created on Pondelok, 2013, február 25, 21:25
 */

#include <boost/shared_ptr.hpp>

#include "wall.h"

namespace github {
    namespace pong {

        Wall::Wall(Uint16 x0, Uint16 y0, Uint16 x1, Uint16 y1, bool goalKeeper) : x0(x0), y0(y0), x1(x1), y1(y1)
        , goalKeeper(goalKeeper), hits(0) {
        }

        bool Wall::actionIfCollision(Uint16 colX, Uint16 colY, Uint16 radius) {
            if (colX < (x0 - radius) || colX > (x1 + radius)) {
                return false;
            }
            if (colY < (y0 - radius) || colY > (y1 + radius)) {
                return false;
            }
            hits++;
            return true;
        }
    }
}
