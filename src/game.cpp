/* 
 * File:   Game.cpp
 * Author: vbmacher
 * 
 * Created on Nedeľa, 2013, február 24, 11:47
 */

#include <iostream>
#include <boost/make_shared.hpp>

#include "game.h"
#include "../graphics/canvas.h"
#include "../graphics/table.h"
#include "../graphics/wall.h"

namespace github {
    namespace pong {

        Game::Game(boost::shared_ptr<Canvas> canvas) : canvas(canvas), players(new Players()), running(false) {
            int width = canvas->getWidth();
            int height = canvas->getHeight();
            
            component.addChild(boost::make_shared<Table>());
            component.addChild(players);
            ball.reset(new Ball(width/2, height/2, width, height));
            component.addChild(boost::make_shared<Wall>(5,5,width-10, 5, false));
            component.addChild(boost::make_shared<Wall>(5,height-5,width-10, height-5, false));
            component.addChild(boost::make_shared<Wall>(0,0,0, height-1, true));
            component.addChild(boost::make_shared<Wall>(width-1,0,width-1, height-1, true));
        }

        void Game::start() {
            if (running) {
                stop();
            }
            running = true;
            dispatcher.reset(new boost::thread(boost::bind(&Game::dispatchEvents, this)));
        }

        void Game::stop() {
            running = false;
            if (dispatcher.get()) {
                if (dispatcher->joinable()) {
                    dispatcher->join();
                }
                dispatcher.reset();
            }
        }
        
        void Game::dispatchEvents() {
            while (running) {
                if (canvas) {
                    canvas->clearScreen();
                    component.draw(*canvas);
                    ball->draw(*canvas);
                    canvas->updateScreen();
                    
                    if (!players->getActive()) {
                        continue;
                    }

                    component.actionIfCollision(*ball);
                    ball->moveAhead();
                    
                    // TODO: check for game end
                }
                SDL_Delay(timeLeft(TICK_INTERVAL));
            }
        }
        
        void Game::onWindowResize(int new_width, int new_height) {
            
        }

        void Game::onMouseMove(Uint16 x, Uint16 y) {
            component.move(x,y);
        }

        void Game::onMouseClick() {
            players->next();
        }

        Uint32 Game::timeLeft(Uint32 tickInterval) {
            static Uint32 next_time = 0;
            Uint32 now;

            now = SDL_GetTicks();
            if (next_time <= now) {
                next_time = now + tickInterval;
                return (0);
            }
            return (next_time - now);
        }        

    }
}