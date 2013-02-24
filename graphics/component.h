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
        
        class Component : private boost::noncopyable {
        public:
            virtual ~Component() {}

            virtual void draw(Canvas &canvas) = 0;
            
            virtual void move(Uint16 x, Uint16 y) = 0;
        };

        class CompositeComponent : public Component {
        public:
            typedef ::std::vector<boost::shared_ptr<Component> > ComponentsType;
        protected:
            ComponentsType children;

        public:

            CompositeComponent() : children() {
            }

            virtual ~CompositeComponent() {}

            void addChild(ComponentsType::value_type component) {
                children.push_back(component);
            }

            void removeChild(ComponentsType::iterator component) {
                children.erase(component);
            }

            void draw(Canvas &canvas);
            
            void move(Uint16 x, Uint16 y) {
                for (ComponentsType::iterator it = children.begin(); it != children.end(); it++) {
                    (*it)->move(x, y);
                }
            }
        };
    }
}
#endif	/* COMPONENT_H */

