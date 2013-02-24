/* 
 * File:   Ball.h
 * Author: vbmacher
 *
 * Created on Nedeľa, 2013, február 24, 18:13
 */

#ifndef BALL_H
#define	BALL_H

#include "component.h"

namespace github {

    namespace pong {
        
        class Canvas;

        class Ball : public Component {
            Uint16 x;
            Uint16 y;
            
            Uint16 vector[2];
            Uint16 velocity;
        public:
            Ball(Uint16 x, Uint16 y);
            virtual ~Ball();
            
            Uint16 getX() const { return x; }

            Uint16 getY() const { return y; }
            
            void draw(Canvas &canvas);
            
            void move(Uint16 x, Uint16 y);
            
            void moveAhead() {
                x = x + vector[0] * velocity;
                y = y + vector[1] * velocity;
            }
            
            void updateVector(Uint16 normalX, Uint16 normalY) {
                //Uint16 dot = vector[0] * normalX + vector[1] * normalY;
                //vector[0] = vector[0] - 2 * dot * normalX;
                //vector[1] = vector[1] - 2 * dot * normalY;
                vector[0] = -vector[0];
            }

        };
    }
}

#endif	/* BALL_H */

