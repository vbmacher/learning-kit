/*
 * File:   Wall.h
 * Author: vbmacher
 *
 * Created on Pondelok, 2013, február 25, 21:25
 */

#ifndef WALL_H
#define	WALL_H

#include <boost/shared_ptr.hpp>

#include "../graphics/canvas.h"

#include "component.h"

namespace github {
    namespace pong {

        class Ball;

        class Wall : public Component {
            bool goal;
        public:
            Wall(Uint16 collisionTolerance);
            virtual ~Wall() {}

            void draw(Canvas &canvas) {}

            void move(Uint16 x, Uint16 y) {}

            bool isGoal() const {
                return isGoalKeeper() ? goal : false;
            }

            void actionIfCollision(Ball &ball);
        protected:

            virtual bool isCollision(Uint16 colX, Uint16 colY) const = 0;

            virtual bool isGoalKeeper() const = 0;
        };

    }
}
#endif	/* WALL_H */

