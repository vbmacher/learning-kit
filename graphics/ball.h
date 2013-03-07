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

namespace github {

    namespace pong {
        
        class Canvas;

        class Ball : public Component {
            static const double M_2PI = 2 * M_PI;
            double x;
            double y;
            
            Uint16 middleX;
            Uint16 middleY;

            Uint16 maxX;
            Uint16 maxY;
            
            double angle;
            double velocity;
        public:
            static const Uint16 RADIUS = 5;
            
            Ball(Uint16 x, Uint16 y, Uint16 maxX, Uint16 maxY);
            virtual ~Ball();
            
            Uint16 getX() const { return x; }

            Uint16 getY() const { return y; }
            
            void draw(Canvas &canvas);
            
            void move(Uint16 x, Uint16 y);
            
            bool actionIfCollision(Uint16 colX, Uint16 colY, Uint16 radius) {
                return false;
            }
            
            void moveAhead();
            
            void changeAngle();
        private:
            enum CollisionDirection {
                LEFT, RIGHT, TOP, BOTTOM
            };
            
            CollisionDirection getCollisionDirection();
        };
    }
}

#endif	/* BALL_H */

