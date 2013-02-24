#include "component.h"

namespace github {
    namespace pong {

        void CompositeComponent::draw(Canvas &canvas) {
            for (ComponentsType::iterator it = children.begin(); it != children.end(); it++) {
                (*it)->draw(canvas);
            }
        }

    }
}
