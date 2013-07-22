/*
 * File:   Players.h
 * Author: vbmacher
 *
 * Created on Pondelok, 2013, február 25, 22:52
 */

#ifndef PLAYERS_H
#define	PLAYERS_H

#include <boost/shared_ptr.hpp>

#include "component.h"

namespace github {
    namespace pong {

        class Player;

        class Players : public CompositeComponent {
            int activeIndex;
        public:
            Players(Uint16 collisionTolerance);
            ~Players();

            void add(boost::shared_ptr<Player> player) {
                push_back(boost::static_pointer_cast<Component, Player>(player));
            }

            void remove(boost::shared_ptr<Player> player) {
                erase(boost::static_pointer_cast<Component, Player>(player));
            }

            void move(Uint16 x, Uint16 y);

            void next() {
                if (!empty()) {
                  activeIndex = (activeIndex + 1) % size();
                }
            }

            bool hasActivePlayer() const {
                return (!empty() && activeIndex >= 0);
            }
        private:
            ComponentsType::iterator insert (iterator position, const value_type& val);
            void insert (iterator position, size_type n, const value_type& val);

            template <class InputIterator>
            void insert (iterator position, InputIterator first, InputIterator last);
            void erase(ComponentType player);
            void push_back (const ComponentsType::value_type& val) {
                CompositeComponent::push_back(val);
            }

            template <class InputIterator>
            void assign (InputIterator first, InputIterator last);
            void assign (size_type n, const ComponentsType::value_type& val);
            void swap (ComponentsType& x);
        };
    }
}
#endif	/* PLAYERS_H */

