/* 
 * File:   Players.h
 * Author: vbmacher
 *
 * Created on Pondelok, 2013, február 25, 22:52
 */

#ifndef PLAYERS_H
#define	PLAYERS_H

#include "component.h"

namespace github {
    namespace pong {

        class Players : public CompositeComponent {
            int activeIndex;
        public:
            Players();
            ~Players();
            
            void removeChild(ComponentsType::iterator component);
            
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
#endif	/* PLAYERS_H */

