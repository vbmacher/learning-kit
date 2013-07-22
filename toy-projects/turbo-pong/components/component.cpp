#include "component.h"

namespace github {
    namespace pong {
        
        class Ball;

        void CompositeComponent::draw(Canvas &canvas) {
            for (ComponentsType::iterator it = begin(); it != end(); ++it) {
                (*it)->draw(canvas);
            }
        }

        void CompositeComponent::move(Uint16 x, Uint16 y) {
            for (ComponentsType::iterator it = begin(); it != end(); ++it) {
                (*it)->move(x, y);
            }
        }

        void CompositeComponent::actionIfCollision(Ball &ball) {
            for (ComponentsType::iterator it = begin(); it != end(); ++it) {
                (*it)->actionIfCollision(ball);
            }
        }
        

    }
}
