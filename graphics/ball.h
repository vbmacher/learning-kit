/* 
 * File:   Ball.h
 * Author: vbmacher
 *
 * Created on Nedeľa, 2013, február 24, 18:13
 */

#ifndef BALL_H
#define	BALL_H

#include <math.h>

#include "component.h"

namespace github {

    namespace pong {
        
        class Canvas;

        class Ball : public Component {
            static const double M_2PI = 2 * M_PI;
            double x;
            double y;
            
            double tan_angle;
            
            Uint16 middleX;
            Uint16 middleY;
            
            double angle;
            double velocity;
            
            enum CollisionDirection {
                LEFT, RIGHT, TOP, BOTTOM
            };
            
            CollisionDirection collisionDirection;
        public:
            static const Uint16 RADIUS = 5;
            
            Ball(Uint16 x, Uint16 y, Uint16 maxX, Uint16 maxY);
            virtual ~Ball();
            
            Uint16 getX() const { return x; }

            Uint16 getY() const { return y; }
            
            void draw(Canvas &canvas);
            
            void move(Uint16 x, Uint16 y);
            
            const Component* collision(Uint16 colX, Uint16 colY, Uint16 radius) {
                return NULL;
            }
            
            void moveAhead();
            
            void changeAngle();
        private:
            void updateCollisionDirection();
        };
    }
}

#endif	/* BALL_H */

