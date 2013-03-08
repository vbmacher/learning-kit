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
            Uint16 x;
            Uint16 y;
            Uint16 width;
            Uint16 height;
            bool goalKeeper;
            
            int hits;
        public:
            Wall(Uint16 x, Uint16 y, Uint16 width, Uint16 height, bool goalKeeper);
            ~Wall() {}

            void draw(Canvas &canvas) {
            }

            void move(Uint16 x, Uint16 y) {
            }
            
            bool isGoalKeeper() const {
                return goalKeeper;
            }
            
            int getHits() const {
                return hits;
            }
            
            bool isCollision(Uint16 colX, Uint16 colY);

            void actionIfCollision(Ball &ball);

        };

    }
}
#endif	/* WALL_H */

