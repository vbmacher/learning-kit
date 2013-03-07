/* 
 * File:   Players.h
 * Author: vbmacher
 *
 * Created on Pondelok, 2013, február 25, 22:52
 */

#ifndef PLAYERS_H
#define	PLAYERS_H

#include "component.h"
#include "player.h"

namespace github {
    namespace pong {

        class Players : public CompositeComponent {
            int activeIndex;
        public:
            Players();
            ~Players();
            
            void removeChild(ComponentsIterator component);
            
            void move(Uint16 x, Uint16 y);
            
            void next() {
                if (!children.empty()) {
                  activeIndex = (activeIndex + 1) % children.size();
                }
            }
            
            CompositeComponent::ComponentType getActive() const;

        };
    }
}
#endif	/* PLAYERS_H */

