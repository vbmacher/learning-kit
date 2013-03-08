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
        
        const double Ball::RADIUS = 5;

        Ball::Ball(Uint16 x, Uint16 y, Uint16 maxX, Uint16 maxY) : x(x), y(y), maxX(maxX), maxY(maxY),
                velocityX(1.4), velocityY(1.4),
                middleX(maxX/2), middleY(maxY/2), angle(rand() % (int)(M_2PI + 1.0)) {
            assert(maxX > 0);
        }

        Ball::~Ball() {
        }

        void Ball::draw(Canvas &canvas) {
            canvas.drawCircle(x, y, RADIUS);
        }
        
        void Ball::correctXY() {
            if (x <= RADIUS) {
                x = RADIUS + 1;
                changeAngle(0,0);
            } else if (x >= (double)(maxX - RADIUS)) {
                x = maxX - 1 - RADIUS;
                changeAngle(0,0);
            }
            if (y <= RADIUS) {
                y = RADIUS + 1;
                changeAngle(0,0);
            } else if (y >= (double)(maxY - RADIUS)) {
                y = maxY - 1 - RADIUS;
                changeAngle(0,0);
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
        
#define dotProduct(x0,y0,x1,y1) ((x0) * (x1) + (y0) * (y1))
        static bool inTriangle(double x0, double y0, double x1, double y1, double x2, double y2, double px, double py) {
            double dot00 = dotProduct(x2-x0, y2-y0, x2-x0, y2-y0);
            double dot01 = dotProduct(x2-x0, y2-y0, x1-x0, y1-y0);
            double dot02 = dotProduct(x2-x0, y2-y0, px-x0, py-y0);
            double dot11 = dotProduct(x1-x0, y1-y0, x1-x0, y1-y0);
            double dot12 = dotProduct(x1-x0, y1-y0, px-x0, py-y0);

            // Compute barycentric coordinates
            double invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
            double u = (dot11 * dot02 - dot01 * dot12) * invDenom;
            double v = (dot00 * dot12 - dot01 * dot02) * invDenom;

            // Check if point is in triangle
            return (u >= 0) && (v >= 0) && (u + v < 1);
        }
#undef dotProduct
       
        Ball::CollisionDirection Ball::getCollisionDirection() {
            if (inTriangle(0, 0, maxX, 0, middleX, middleY, x, y)) {
                return TOP;
            } else if (inTriangle(0,0,middleX, middleY, 0, maxY, x, y)) {
                return LEFT;
            } else if (inTriangle(0, maxY, middleX, middleY, maxX, maxY, x, y)) {
                return BOTTOM;
            }
            return RIGHT;
        }

    }
}
