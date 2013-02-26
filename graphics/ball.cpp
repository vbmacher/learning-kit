/* 
 * File:   Ball.cpp
 * Author: vbmacher
 * 
 * Created on Nedeľa, 2013, február 24, 18:13
 */

#include <iostream>
#include <math.h>
#include <assert.h>

#include "ball.h"
#include "canvas.h"

namespace github {

    namespace pong {

        Ball::Ball(Uint16 x, Uint16 y, Uint16 maxX, Uint16 maxY) : x(x), y(y), middleX(maxX/2), middleY(maxY/2),
                velocity(5), angle(rand() % (int)(M_2PI + 1.0)) {
            assert(maxX > 0);
            tan_angle = tan(maxY/maxX);
            updateCollisionDirection();
        }

        Ball::~Ball() {
        }

        void Ball::draw(Canvas &canvas) {
            canvas.drawCircle(x, y, RADIUS);
        }

        void Ball::move(Uint16 new_x, Uint16 new_y) {
            x = new_x;
            y = new_y;
        }

        void Ball::moveAhead() {
            x += cos(angle) * velocity;
            y += sin(angle) * velocity;
            updateCollisionDirection();
        }

        void Ball::changeAngle() {
            switch (collisionDirection) {
                case LEFT:
                case RIGHT:
                    angle = M_2PI - angle;
                    break;
                case TOP:
                case BOTTOM:
                    angle = M_PI - angle;
                    break;
            }
        }

        void Ball::updateCollisionDirection() {
            if (x <= middleX) {
                if (y <= middleY) {
                    if (tan(y / x) <= tan_angle) {
                        collisionDirection = TOP;
                    } else {
                        collisionDirection = LEFT;
                    }
                } else {
                    if (tan((middleY * 2 - y) / x) <= tan_angle) {
                        collisionDirection = BOTTOM;
                    } else {
                        collisionDirection = LEFT;
                    }
                }
            } else {
                if (y <= middleY) {
                    if (tan(y / (2 * middleX - x)) <= tan_angle) {
                        collisionDirection = TOP;
                    } else {
                        collisionDirection = RIGHT;
                    }
                } else {
                    if (tan((middleY * 2 - y) / (2 * middleX - x)) <= tan_angle) {
                        collisionDirection = BOTTOM;
                    } else {
                        collisionDirection = RIGHT;
                    }
                }
            }
        }

    }
}
