/* 
 * File:   Table.h
 * Author: vbmacher
 *
 * Created on Nedeľa, 2013, február 24, 14:16
 */

#ifndef TABLE_H
#define	TABLE_H

#include "component.h"
#include "canvas.h"

namespace github {
    namespace pong {
        
        class Canvas;

        class Table : public CompositeComponent {
            int activeIndex;
        public:
            Table();
            ~Table();
            
            void removeChild(ComponentsType::iterator component);
            
            void draw(Canvas &canvas);
            
            void move(Uint16 x, Uint16 y);
            
            void next() {
                if (!children.empty()) {
                  activeIndex = (activeIndex + 1) % children.size();
                }
            }
            
            ComponentsType::value_type getActive() {
                if (!children.empty() && activeIndex >= 0) {
                    return children[activeIndex];
                }
                return ComponentsType::value_type();
            }
            
        };

    }
}
#endif	/* TABLE_H */

