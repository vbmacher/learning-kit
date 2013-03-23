/*
 * File:   Component.h
 * Author: vbmacher
 *
 * Created on Nedeľa, 2013, február 24, 12:54
 */

#ifndef COMPONENT_H
#define	COMPONENT_H

#include <vector>

#include <boost/shared_ptr.hpp>
#include <boost/noncopyable.hpp>

#include <SDL.h>

namespace github {
    namespace pong {

        class Canvas;

        class Ball;

        class Component : private boost::noncopyable {
        protected:
            Uint16 collisionTolerance;

            Component(Uint16 collisionTolerance) : collisionTolerance(collisionTolerance) {
            }

        public:
            virtual ~Component() {
            }

            virtual void draw(Canvas &canvas) = 0;

            virtual void move(Uint16 x, Uint16 y) = 0;

            virtual void actionIfCollision(Ball &ball) = 0;
        };

        class CompositeComponent : public Component, public ::std::vector<boost::shared_ptr<Component> > {
        public:
            typedef boost::shared_ptr<Component> ComponentType;
            typedef ::std::vector<ComponentType> ComponentsType;

            CompositeComponent(Uint16 collisionTolerance) : Component(collisionTolerance) {}

            virtual ~CompositeComponent() {}

            void erase(ComponentType component) {
                for (ComponentsType::iterator it = begin(); it != end(); ++it) {
                    if ((*it) == component) {
                        ComponentsType::erase(it);
                        return;
                    }
                }
            }

            void draw(Canvas &canvas);

            void move(Uint16 x, Uint16 y);

            void actionIfCollision(Ball &ball);

        };
    }
}
#endif	/* COMPONENT_H */

