/*
 * File:   Game.cpp
 * Author: vbmacher
 *
 * Created on Nedeľa, 2013, február 24, 11:47
 */

#include <iostream>
#include <boost/make_shared.hpp>
#include <boost/shared_ptr.hpp>

#include "../graphics/canvas.h"

#include "game.h"
#include "table.h"
#include "leftWall.h"
#include "rightWall.h"
#include "upperWall.h"
#include "bottomWall.h"
#include "ball.h"
#include "players.h"
#include "leftPlayer.h"
#include "rightPlayer.h"

namespace github {
    namespace pong {

        Game::Game(boost::shared_ptr<Canvas> canvas) : canvas(canvas), component(Ball::RADIUS),
                players(new Players(Ball::RADIUS)), running(false) {
            Uint16 tolerance = Ball::RADIUS;
            int width = canvas->getWidth();
            int height = canvas->getHeight();

            component.push_back(boost::make_shared<Table>());
            component.push_back(players);
            ball = boost::make_shared<Ball>(width/2, height/2, width, height);

            leftWall = boost::make_shared<LeftWall>(tolerance, tolerance);
            rightWall = boost::make_shared<RightWall>(width-tolerance-1, tolerance);

            component.push_back(boost::make_shared<UpperWall>(tolerance, tolerance));
            component.push_back(boost::make_shared<BottomWall>(height-tolerance-1, tolerance));
            component.push_back(boost::static_pointer_cast<Component,LeftWall>(leftWall));
            component.push_back(boost::static_pointer_cast<Component,RightWall>(rightWall));
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

        bool Game::addPlayer(const std::string& name, Playground playground) {
            if (running) {
                return false;
            }
            Uint16 yMax = canvas->getHeight() - 10;
            Uint16 xMax = canvas->getWidth() - 22;

            Uint16 yMin = Ball::RADIUS;
            Uint16 xMin = Ball::RADIUS + 3;

            if (playground == PLAYGROUND_LEFT) {
                players->add(boost::make_shared<LeftPlayer>(name, xMin, yMin, yMin, yMax, Ball::RADIUS));
            } else if (playground == PLAYGROUND_RIGHT) {
                players->add(boost::make_shared<RightPlayer>(name, xMax, yMin, yMin, yMax, Ball::RADIUS));
            }
            return true;
        }

        void Game::removePlayer(Playground playground) {
            // TODO
        }

        void Game::nextPlayer() {
            players->next();
        }

        void Game::showIntroScreen() {
            canvas->clearScreen();
            canvas->fillRect(20, 60, 30, 450);
            canvas->fillRect(20, 60, 150, 70);
            canvas->fillRect(140, 60, 150, 200);
            canvas->fillRect(20, 190, 150, 200);

            canvas->fillRect(200, 60, 210, 450);
            canvas->fillRect(200, 60, 340, 70);
            canvas->fillRect(340, 60, 350, 450);
            canvas->fillRect(200, 440, 340, 450);

            canvas->fillRect(400, 60, 410, 450);
            canvas->fillLine(400, 60, 540, 440, 10);
            canvas->fillRect(540, 60, 550, 450);

            canvas->fillRect(600, 60, 610, 450);
            canvas->fillRect(600, 60, 740, 70);
            canvas->fillRect(600, 440, 740, 450);
            canvas->fillRect(740, 310, 750, 450);
            canvas->fillRect(630, 310, 740, 320);
            canvas->fillRect(740, 60, 750, 100);
        }

        void Game::dispatchEvents() {
            while (running) {
                if (!players->hasActivePlayer()) {
                    showIntroScreen();
                    canvas->updateScreen();
                    continue;
                }
                canvas->clearScreen();

                component.draw(*canvas);
                ball->draw(*canvas);
                canvas->updateScreen();

                component.actionIfCollision(*ball);
                ball->moveAhead();

                if (leftWall->isGoal()) {
                    running = false;
                    std::cout << "Winner is on the RIGHT side" << std::endl;
                } else if (rightWall->isGoal()) {
                    running = false;
                    std::cout << "Winner is on the LEFT side" << std::endl;
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
