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
#include "../graphics/canvas.h"

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
            canvas.fillCircle(correctX(), correctY(), RADIUS);
        }

        Sint16 Ball::correctX() {
            Sint16 _x = x;
            if (_x <= RADIUS) {
                _x = RADIUS + 1;
            } else if (_x >= (maxX - RADIUS)) {
                _x = maxX - 1 - RADIUS;
            }
            x = _x;
            return _x;
        }

        Sint16 Ball::correctY() {
            Sint16 _y = y;
            if (_y <= RADIUS) {
                _y = RADIUS + 1;
            } else if (_y >= (maxY - RADIUS)) {
                _y = maxY - 1 - RADIUS;
            }
            y = _y;
            return _y;
        }


        void Ball::move(Uint16 new_x, Uint16 new_y) {
            x = new_x;
            y = new_y;
        }

        void Ball::moveAhead() {
            double _angle = angle;
            Sint16 _x = (double)x + cos(_angle) * velocityX;
            Sint16 _y = (double)y + sin(_angle) * velocityY;
            x = _x;
            y = _y;
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
            Sint16 _x = x;
            Sint16 _y = y;
            if (Canvas::inTriangle(0, 0, maxX, 0, middleX, middleY, _x, _y)) {
                return TOP;
            } else if (Canvas::inTriangle(0,0,middleX, middleY, 0, maxY, _x, _y)) {
                return LEFT;
            } else if (Canvas::inTriangle(0, maxY, middleX, middleY, maxX, maxY, _x, _y)) {
                return BOTTOM;
            }
            return RIGHT;
        }

    }
}
