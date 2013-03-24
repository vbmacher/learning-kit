/*
 * File:   Ball.h
 * Author: vbmacher
 *
 * Created on Nedeľa, 2013, február 24, 18:13
 */

#ifndef BALL_H
#define	BALL_H

#include <math.h>
#include <boost/shared_ptr.hpp>

#include "component.h"
#include "synchronization.h"

namespace github {

    namespace pong {

        class Canvas;

        class Ball : public Component {
            static const double M_2PI = 2 * M_PI;
            Locked<Sint16> x;
            Locked<Sint16> y;

            Uint16 middleX;
            Uint16 middleY;

            Uint16 maxX;
            Uint16 maxY;

            Locked<double> angle;
            double velocityX;
            double velocityY;
        public:
            static const Uint16 RADIUS;

            Ball(Uint16 x, Uint16 y, Uint16 maxX, Uint16 maxY);
            virtual ~Ball();

            Sint16 getX() const {
                return x;
            }

            Sint16 getY() const {
                return y;
            }

            void draw(Canvas &canvas);

            void move(Uint16 x, Uint16 y);

            void actionIfCollision(Ball &ball) {
            }

            void moveAhead();

            void changeAngle(double angleInfluence, double velocityInfluence);
        private:
            enum CollisionDirection {
                LEFT, RIGHT, TOP, BOTTOM
            };

            CollisionDirection getCollisionDirection();

            Sint16 correctX();
            Sint16 correctY();
        };
    }
}

#endif	/* BALL_H */

