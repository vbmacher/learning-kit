/* 
 * File:   Players.cpp
 * Author: vbmacher
 * 
 * Created on Pondelok, 2013, február 25, 22:52
 */

#include "component.h"
#include "players.h"

namespace github {
    namespace pong {

        Players::Players() : activeIndex(-1) {
        }

        Players::~Players() {
        }
        
        void Players::removeChild(ComponentsIterator component) {
            CompositeComponent::removeChild(component);
            long unsigned int size = children.size();
            if (size <= activeIndex) {
                activeIndex = size - 1;
            }
        }

        CompositeComponent::ComponentType Players::getActive() const {
            if (!children.empty() && activeIndex >= 0) {
                return children[activeIndex];
            }
            return ComponentType();
        }

        void Players::move(Uint16 x, Uint16 y) {
            if (children.empty() || (activeIndex < 0)) {
                return;
            }
            children[activeIndex]->move(x, y);
        }
    }
}