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

        const Uint16 Ball::RADIUS = 5;

        Ball::Ball(Uint16 x, Uint16 y, Uint16 maxX, Uint16 maxY)
                : Component(0), x(x), y(y), maxX(maxX), maxY(maxY), velocityX(10), velocityY(10), middleX(maxX/2),
                  middleY(maxY/2), angle(rand() % (int)(M_2PI + 1.0)) {
            assert(maxX > 0);
        }

        Ball::~Ball() {
        }

        void Ball::draw(Canvas &canvas) {
            canvas.fillCircle(x, y, RADIUS);
        }

        void Ball::correctXY() {
            if ((Uint16)x <= RADIUS) {
                x = RADIUS + 1;
            } else if (x >= (double)(maxX - RADIUS)) {
                x = maxX - 1 - RADIUS;
            }
            if ((Uint16)y <= RADIUS) {
                y = RADIUS + 1;
            } else if (y >= (double)(maxY - RADIUS)) {
                y = maxY - 1 - RADIUS;
            }
        }


        void Ball::move(Uint16 new_x, Uint16 new_y) {
            x = new_x;
            y = new_y;
            correctXY();
        }

        void Ball::moveAhead() {
            x += cos(angle) * (double)velocityX;
            y += sin(angle) * (double)velocityY;
            correctXY();
        }

        void Ball::changeAngle(double angleInfluence, double velocityInfluence) {
            switch (getCollisionDirection()) {
                case TOP:
                    angle = M_2PI - (angle - angleInfluence);
                    break;
                case BOTTOM:
                    angle = M_2PI - (angle + angleInfluence);
                    break;
                case LEFT:
                    angle = M_PI - (angle - angleInfluence);
                    break;
                case RIGHT:
                    angle = M_PI - (angle + angleInfluence);
                    break;
            }
            velocityY += velocityInfluence;
        }

        Ball::CollisionDirection Ball::getCollisionDirection() {
            if (Canvas::inTriangle(0, 0, maxX, 0, middleX, middleY, x, y)) {
                return TOP;
            } else if (Canvas::inTriangle(0,0,middleX, middleY, 0, maxY, x, y)) {
                return LEFT;
            } else if (Canvas::inTriangle(0, maxY, middleX, middleY, maxX, maxY, x, y)) {
                return BOTTOM;
            }
            return RIGHT;
        }

    }
}
