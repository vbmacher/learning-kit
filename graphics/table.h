/* 
 * File:   Table.h
 * Author: vbmacher
 *
 * Created on Nedeľa, 2013, február 24, 14:16
 */

#ifndef TABLE_H
#define	TABLE_H

#include <boost/shared_ptr.hpp>

#include "component.h"

namespace github {
    namespace pong {
        
        class Canvas;
        
        class Ball;

        class Table : public Component {
        public:
            Table();
            ~Table();
            
            void draw(Canvas &canvas);
            
            void move(Uint16 x, Uint16 y) {}

            void actionIfCollision(Ball &ball) {
            }

        };

    }
}
#endif	/* TABLE_H */

