/* 
 * File:   Ball.cpp
 * Author: vbmacher
 * 
 * Created on Nedeľa, 2013, február 24, 18:13
 */

#include "ball.h"
#include "canvas.h"

namespace github {

    namespace pong {

        Ball::Ball(Uint16 x, Uint16 y) : x(x), y(y), velocity(1) {
            vector[0] = 1;
            vector[1] = 0;
        }

        Ball::~Ball() {
        }

        void Ball::draw(Canvas &canvas) {
            canvas.fillCircle(x,y,5);
        }
            
        void Ball::move(Uint16 x, Uint16 y) {
        }
        
    }
}
