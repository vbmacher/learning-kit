/* 
 * File:   Players.cpp
 * Author: vbmacher
 * 
 * Created on Pondelok, 2013, február 25, 22:52
 */

#include "players.h"

namespace github {
    namespace pong {

        Players::Players() : activeIndex(-1) {
        }

        Players::~Players() {
        }
        
        void Players::removeChild(ComponentsType::iterator component) {
            CompositeComponent::removeChild(component);
            long unsigned int size = children.size();
            if (size <= activeIndex) {
                activeIndex = size - 1;
            }
        }
        
        void Players::move(Uint16 x, Uint16 y) {
            if (children.empty() || (activeIndex < 0)) {
                return;
            }
            children[activeIndex]->move(x, y);
        }
    }
}