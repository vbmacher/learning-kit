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
#include <boost/thread/mutex.hpp>

#include "../graphics/eventHandler.h"
#include "../components/component.h"
#include "../components/synchronization.h"

namespace github {
    namespace pong {

        class Canvas;

        class Ball;

        class Players;

        class LeftWall;

        class RightWall;

        class Game : private boost::noncopyable, public EventHandler {
        public:
            enum Playground {
                PLAYGROUND_LEFT,
                PLAYGROUND_RIGHT
            };
        private:
            const static int TICK_INTERVAL = 30;

            boost::shared_ptr<Canvas> canvas;
            CompositeComponent component;

            boost::shared_ptr<Players> players;
            boost::shared_ptr<Ball> ball;
            boost::shared_ptr<LeftWall> leftWall;
            boost::shared_ptr<RightWall> rightWall;

            boost::shared_ptr<boost::thread> dispatcher;
            Locked<bool> running;
            friend class boost::thread;
        public:
            Game(boost::shared_ptr<Canvas> canvas);

            ~Game() {
                if (dispatcher.get()) {
                    running = false;
                    dispatcher->join();
                }
            }

            bool addPlayer(const std::string& name, Playground playground);

            void removePlayer(Playground playground);

            void nextPlayer();

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

            void showIntroScreen();

        };

    }
}
#endif	/* GAME_H */

