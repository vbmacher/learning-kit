/*
 * File:   rightWall.h
 * Author: vbmacher
 *
 * Created on Sobota, 2013, marec 23, 10:50
 */

#ifndef RIGHTWALL_H
#define	RIGHTWALL_H

#include "wall.h"

namespace github {
    namespace pong {

        class RightWall : public Wall {
            Uint16 x;
        public:
            RightWall(Uint16 x, Uint16 collisionTolerance) : Wall(collisionTolerance), x(x) {}

        private:
            bool isCollision(Uint16 colX, Uint16 colY) const;

            bool isGoalKeeper() const {
                return true;
            }

        };

    }
}

#endif	/* RIGHTWALL_H */

