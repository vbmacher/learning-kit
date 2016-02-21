/*
 * File:   Players.cpp
 * Author: vbmacher
 *
 * Created on Pondelok, 2013, február 25, 22:52
 */
#include <boost/shared_ptr.hpp>

#include "component.h"
#include "players.h"
#include "player.h"

namespace github {
    namespace pong {

        Players::Players(Uint16 collisionTolerance) : CompositeComponent(collisionTolerance), activeIndex(-1) {
        }

        Players::~Players() {
        }

        void Players::erase(ComponentType component) {
            CompositeComponent::erase(component);
            long unsigned int size = CompositeComponent::size();
            if (size <= activeIndex) {
                activeIndex = size - 1;
            }
        }

        void Players::move(Uint16 x, Uint16 y) {
            if (empty() || (activeIndex < 0)) {
                return;
            }
            CompositeComponent::operator[](activeIndex)->move(x, y);
        }
    }
}