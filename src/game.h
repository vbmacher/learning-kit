/* 
 * File:   game.h
 * Author: vbmacher
 *
 * Created on Nedeľa, 2013, február 24, 11:47
 */

#ifndef GAME_H
#define	GAME_H

#include <iostream>

#include <boost/shared_ptr.hpp>
#include <boost/noncopyable.hpp>
#include <boost/thread.hpp>

#include "../graphics/component.h"
#include "../graphics/ball.h"
#include "../graphics/player.h"
#include "../graphics/players.h"
#include "../graphics/eventHandler.h"

#include "synchronization.h"

namespace github {
    namespace pong {
        
        class Canvas;

        class Game : private boost::noncopyable, public EventHandler {
        public:
           // typedef std::vector<boost::shared_ptr<Player> > PlayersType;
        private:
            const static int TICK_INTERVAL = 30;
            
            boost::shared_ptr<Canvas> canvas;
            CompositeComponent component;
            
            boost::shared_ptr<Players> players;
            boost::shared_ptr<Ball> ball;
            
            boost::shared_ptr<boost::thread> eventDispatcher;
            Locked<bool> running;
            friend class boost::thread;
        public:
            Game(boost::shared_ptr<Canvas> canvas);
                        
            ~Game() {
                if (eventDispatcher.get()) {
                    running = false;
                    eventDispatcher->join();
                }
            }
            
            void addPlayer(boost::shared_ptr<Player> player) {
                players->addChild(player);
            }
            
            void removePlayer(CompositeComponent::ComponentsType::iterator player) {
                players->removeChild(player);
            }
            
            void nextPlayer() {
                players->next();
            }
            
            void start();
            
            void stop();
            
            bool isRunning() {
                return running;
            }
            
            void onWindowResize(int new_width, int new_height);

            void onMouseMove(Uint16 x, Uint16 y);

            void onMouseClick();
            
        private:
            void dispatchEvents();

            Uint32 timeLeft(Uint32 tickInterval);

        };

    }
}
#endif	/* GAME_H */

