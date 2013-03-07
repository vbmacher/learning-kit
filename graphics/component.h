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

            Component() {
            }

        public:
            virtual ~Component() {
            }

            virtual void draw(Canvas &canvas) = 0;

            virtual void move(Uint16 x, Uint16 y) = 0;

            virtual void actionIfCollision(Ball &ball) = 0;
        };

        class CompositeComponent : public Component {
        public:
            typedef typename boost::shared_ptr<Component> ComponentType;
        private:
            typedef ::std::vector<ComponentType> ComponentsType;
        public:
            typedef typename ComponentsType::iterator ComponentsIterator;
        protected:
            ComponentsType children;

        public:

            CompositeComponent() : children() {
            }

            virtual ~CompositeComponent() {
            }

            void addChild(ComponentType component) {
                children.push_back(component);
            }

            void removeChild(ComponentsIterator component) {
                children.erase(component);
            }

            void draw(Canvas &canvas);

            void move(Uint16 x, Uint16 y);

            void actionIfCollision(Ball &ball);

        };
    }
}
#endif	/* COMPONENT_H */

