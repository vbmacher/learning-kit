/* 
 * File:   SDLEventHandler.h
 * Author: vbmacher
 *
 * Created on Nedeľa, 2013, február 24, 12:51
 */

#ifndef EVENTHANDLER_H
#define	EVENTHANDLER_H

#include <SDL.h>

namespace github {

    namespace pong {

        class EventHandler {
        public:
            virtual ~EventHandler() {}

            virtual void onWindowResize(int new_width, int new_height) = 0;

            virtual void onMouseMove(Uint16 x, Uint16 y) = 0;

            virtual void onMouseClick() = 0;

        };

    }
}
#endif	/* EVENTHANDLER_H */

