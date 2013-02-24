/* 
 * File:   Game.cpp
 * Author: vbmacher
 * 
 * Created on Nedeľa, 2013, február 24, 11:47
 */

#include <iostream>

#include "game.h"

namespace github {
    namespace pong {

        Game::Game(boost::shared_ptr<Canvas> canvas) : canvas(canvas), table(new Table()), running(false) {
            component.addChild(table);
            ball.reset(new Ball(canvas->getWidth()/2, canvas->getHeight()/2));
        }

        void Game::start() {
            if (running) {
                stop();
            }
            running = true;
            eventDispatcher.reset(new boost::thread(boost::bind(&Game::dispatchEvents, this)));
        }

        void Game::stop() {
            running = false;
            if (eventDispatcher.get()) {
                eventDispatcher->join();
                eventDispatcher.reset();
            }
        }
        

        void Game::dispatchEvents() {
            while (running) {
                if (canvas.get()) {
                    canvas->clearScreen();
                    component.draw(*canvas);
                    ball->draw(*canvas);
                    
                    CompositeComponent::ComponentsType::value_type active = table->getActive();
                    
                    if (active.get()) {
                        Player& activePlayer = dynamic_cast<Player&>(*active);
                        if (activePlayer.collision(ball->getX(), ball->getY())) {
                            std::cout << "A: " << activePlayer.getName() << " COL " << std::endl;
                            ball->updateVector(0, activePlayer.getMovingVectorY());
                        }
                    }
                    ball->moveAhead();
                    
                    canvas->updateScreen();
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
            table->next();
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