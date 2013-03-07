#include "component.h"
#include "ball.h"

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

        void CompositeComponent::actionIfCollision(Ball &ball) {
            for (ComponentsIterator it = children.begin(); it != children.end(); it++) {
                (*it)->actionIfCollision(ball);
            }
        }
        

    }
}
