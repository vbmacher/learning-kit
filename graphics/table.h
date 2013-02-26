/* 
 * File:   Table.h
 * Author: vbmacher
 *
 * Created on Nedeľa, 2013, február 24, 14:16
 */

#ifndef TABLE_H
#define	TABLE_H

#include "component.h"

namespace github {
    namespace pong {
        
        class Canvas;

        class Table : public Component {
        public:
            Table();
            ~Table();
            
            void draw(Canvas &canvas);
            
            void move(Uint16 x, Uint16 y) {}
            
            const Component* collision(Uint16 colX, Uint16 colY, Uint16 radius) {
                return NULL;
            }
            
        };

    }
}
#endif	/* TABLE_H */

