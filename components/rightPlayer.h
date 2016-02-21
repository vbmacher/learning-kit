/*
 * File:   rightPlayer.h
 * Author: vbmacher
 *
 * Created on Sobota, 2013, marec 23, 10:17
 */

#ifndef RIGHTPLAYER_H
#define	RIGHTPLAYER_H

#include "player.h"

namespace github {
    namespace pong {

        class RightPlayer : public Player {
        public:
            RightPlayer(const std::string& name, Uint16 x, Uint16 y, Uint16 yMin, Uint16 yMax, Uint16 collisionTolerance);

            bool isCollision(Uint16 colX, Uint16 colY);

        private:

        };

    }
}


#endif	/* RIGHTPLAYER_H */

