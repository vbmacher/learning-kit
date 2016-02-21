/*
 * File:   leftWall.h
 * Author: vbmacher
 *
 * Created on Sobota, 2013, marec 23, 10:50
 */

#ifndef LEFTWALL_H
#define	LEFTWALL_H

#include "wall.h"

namespace github {
    namespace pong {

        class LeftWall : public Wall {
            Uint16 x;
        public:
            LeftWall(Uint16 x, Uint16 collisionTolerance) : Wall(collisionTolerance), x(x) {}

        private:
            bool isCollision(Uint16 colX, Uint16 colY) const;

            bool isGoalKeeper() const {
                return true;
            }

        };

    }
}

#endif	/* LEFTWALL_H */

