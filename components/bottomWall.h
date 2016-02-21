/*
 * File:   bottomWall.h
 * Author: vbmacher
 *
 * Created on Sobota, 2013, marec 23, 10:32
 */

#ifndef BOTTOMWALL_H
#define	BOTTOMWALL_H

#include "wall.h"

namespace github {
    namespace pong {

        class BottomWall : public Wall {
            Uint16 y;
        public:
            BottomWall(Uint16 y, Uint16 collisionTolerance) : Wall(collisionTolerance), y(y) {}

        private:
            bool isCollision(Uint16 colX, Uint16 colY) const;

            bool isGoalKeeper() const {
                return false;
            }

        };

    }
}

#endif	/* BOTTOMWALL_H */

