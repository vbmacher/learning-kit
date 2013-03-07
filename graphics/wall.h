/* 
 * File:   Wall.h
 * Author: vbmacher
 *
 * Created on Pondelok, 2013, február 25, 21:25
 */

#ifndef WALL_H
#define	WALL_H

#include <boost/shared_ptr.hpp>

#include "component.h"
#include "canvas.h"

namespace github {
    namespace pong {
        
        class Ball;

        class Wall : public Component {
            Uint16 x0;
            Uint16 y0;
            Uint16 x1;
            Uint16 y1;
            bool goalKeeper;
            
            int hits;
        public:
            Wall(Uint16 x0, Uint16 y0, Uint16 x1, Uint16 y1, bool goalKeeper);
            ~Wall() {}

            void draw(Canvas &canvas) {
                if (!goalKeeper) {
                    for (int i = x0; i <= x1; i+=2) {
                        for (int j = y0; j <= y1; j++) {
                            canvas.dottedLine(i,j,i+2,j+2);
                        }
                    }
                }
            }

            void move(Uint16 x, Uint16 y) {
            }
            
            bool isGoalKeeper() const {
                return goalKeeper;
            }
            
            int getHits() const {
                return hits;
            }

            void actionIfCollision(Ball &ball);

        };

    }
}
#endif	/* WALL_H */

