/* 
 * File:   Wall.h
 * Author: vbmacher
 *
 * Created on Pondelok, 2013, február 25, 21:25
 */

#ifndef WALL_H
#define	WALL_H

#include "component.h"

namespace github {
    namespace pong {

        class Wall : public Component {
            Uint16 x0;
            Uint16 y0;
            Uint16 x1;
            Uint16 y1;
        public:
            Wall(Uint16 x0, Uint16 y0, Uint16 x1, Uint16 y1);
            ~Wall() {}

            void draw(Canvas &canvas) {
            }

            void move(Uint16 x, Uint16 y) {
            }

            const Component* collision(Uint16 colX, Uint16 colY, Uint16 radius);


        };

    }
}
#endif	/* WALL_H */

