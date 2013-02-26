#include "component.h"

namespace github {
    namespace pong {

        void CompositeComponent::draw(Canvas &canvas) {
            for (ComponentsType::iterator it = children.begin(); it != children.end(); it++) {
                (*it)->draw(canvas);
            }
        }

        const Component* CompositeComponent::collision(Uint16 colX, Uint16 colY, Uint16 radius) {
            for (ComponentsType::iterator it = children.begin(); it != children.end(); it++) {
                const Component* collide = (*it)->collision(colX, colY, radius);
                if (collide != NULL) {
                    return collide;
                }
            }
            return NULL;
        }

    }
}
