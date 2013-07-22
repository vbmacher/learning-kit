/*
 * File:   leftPlayer.h
 * Author: vbmacher
 *
 * Created on Sobota, 2013, marec 23, 10:13
 */

#ifndef LEFTPLAYER_H
#define	LEFTPLAYER_H

#include "player.h"

namespace github {
    namespace pong {

        class LeftPlayer : public Player {
        public:
            LeftPlayer(const std::string& name, Uint16 x, Uint16 y, Uint16 yMin, Uint16 yMax, Uint16 collisionTolerance);
        private:

            bool isCollision(Uint16 colX, Uint16 colY);

        };

    }
}

#endif	/* LEFTPLAYER_H */

