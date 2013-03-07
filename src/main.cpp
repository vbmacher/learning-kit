#include <stdexcept>
#include <iostream>

#include <boost/shared_ptr.hpp>
#include <boost/make_shared.hpp>

#include <SDL.h>

#include "../graphics/canvas.h"
#include "../graphics/player.h"
#include "game.h"

using namespace std;
using namespace github::pong;

int main(int argc, char *argv[]) {
    try {
        boost::shared_ptr<Canvas> canvas(new Canvas(640, 480));
        Game game(canvas);
        
        Uint16 yMax = canvas->getHeight()- 10;
        
        game.getPlayers()->addChild(boost::shared_ptr<Player>(new Player("PlayerLeft", 8,8, 8, yMax)));
        game.getPlayers()->addChild(boost::shared_ptr<Player>(new Player("PlayerRight", canvas->getWidth()-22,8,8,yMax)));
        
        game.start();
        
        bool running = true;
        while (running) {
            SDL_Event event;
            SDL_WaitEvent(&event);

            switch (event.type) {
                case SDL_MOUSEBUTTONUP:
                    game.onMouseClick();
                    break;
                case SDL_MOUSEMOTION:
                    game.onMouseMove(event.motion.x, event.motion.y);
                    break;
                case SDL_KEYDOWN:
                    if (event.key.keysym.sym == SDLK_ESCAPE) {
                        cout << "Quitting the application..." << std::endl;
                        running = false;
                        break;
                    }
                    break;
                case SDL_QUIT:
                    break;
            }
        }

        game.stop();
    } catch (std::runtime_error &e) {
        cerr << "Error during loading Canvas: " << e.what() << std::endl;
    }
}
