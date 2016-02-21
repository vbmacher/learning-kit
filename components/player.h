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
#include "synchronization.h"

namespace github {

    namespace pong {

        class Ball;

        class Player : public Component {
            std::string name;

            Uint32 lastDrawTime;
            Uint16 lastY;
            double velocityY;

        public:
            static const Uint16 WIDTH;
            static const Uint16 HEIGHT;

            Player(std::string name, Uint16 x, Uint16 y, Uint16 yMin, Uint16 yMax, Uint16 collisionTolerance);

            ~Player() {
            }

            void draw(Canvas &canvas);

            void move(Uint16 new_x, Uint16 new_y);

            std::string const &getName() const {
                return name;
            }

            void actionIfCollision(Ball &ball);
        protected:
            Locked<Uint16> x;
            Locked<Uint16> y;

            Uint16 yMin;
            Uint16 yMax;

            bool checkY(Uint16 newY) {
                return (newY + HEIGHT) <= yMax && (newY >= yMin);
            }
            void computeVelocity();

            virtual bool isCollision(Uint16 colX, Uint16 colY) = 0;
        };

    }
}
#endif	/* PLAYER_H */

