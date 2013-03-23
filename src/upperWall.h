/*
 * File:   upperWall.h
 * Author: vbmacher
 *
 * Created on Sobota, 2013, marec 23, 10:32
 */

#ifndef UPPERWALL_H
#define	UPPERWALL_H

#include "wall.h"

namespace github {
    namespace pong {

        class UpperWall : public Wall {
            Uint16 y;
        public:
            UpperWall(Uint16 y, Uint16 collisionTolerance) : Wall(collisionTolerance), y(y) {}

        private:
            bool isCollision(Uint16 colX, Uint16 colY) const;

            bool isGoalKeeper() const {
                return false;
            }

        };

    }
}

#endif	/* UPPERWALL_H */

