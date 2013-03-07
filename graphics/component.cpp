#include "component.h"

namespace github {
    namespace pong {

        void CompositeComponent::draw(Canvas &canvas) {
            for (ComponentsType::iterator it = children.begin(); it != children.end(); it++) {
                (*it)->draw(canvas);
            }
        }

        void CompositeComponent::move(Uint16 x, Uint16 y) {
            for (ComponentsIterator it = children.begin(); it != children.end(); it++) {
                (*it)->move(x, y);
            }
        }

        bool CompositeComponent::actionIfCollision(Uint16 colX, Uint16 colY, Uint16 radius) {
            bool collision = false;
            for (ComponentsIterator it = children.begin(); it != children.end(); it++) {
                collision |= (*it)->actionIfCollision(colX, colY, radius);
            }
            return collision;
        }
        

    }
}
