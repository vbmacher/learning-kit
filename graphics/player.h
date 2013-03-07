/* 
 * File:   Player.h
 * Author: vbmacher
 *
 * Created on Nedeľa, 2013, február 24, 16:05
 */

#ifndef PLAYER_H
#define	PLAYER_H

#include <string>
#include <SDL.h>
#include <boost/shared_ptr.hpp>

#include "component.h"

namespace github {

    namespace pong {
        
        class Ball;

        class Player : public Component {
            std::string name;
            Uint16 x;
            Uint16 y;
            
            Uint16 minY;
            Uint16 maxY;
            
        public:
            static const int WIDTH = 10;
            static const int HEIGHT = 60;
            
            Player(std::string name, Uint16 x, Uint16 y, Uint16 yMin, Uint16 yMax);

            ~Player() {
            }
            
            void draw(Canvas &canvas);

            void move(Uint16 new_x, Uint16 new_y);

            std::string const &getName() const {
                return name;
            }
            
            void actionIfCollision(Ball &ball);
        private:
            bool checkY(Uint16 newY) {
                return (newY + HEIGHT) <= maxY && (newY >= minY);
            }

        };

    }
}
#endif	/* PLAYER_H */

