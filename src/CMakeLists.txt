include_directories(${Pong_SOURCE_DIR}/src)
include_directories(${Pong_SOURCE_DIR}/graphics)
link_directories(${Pong_BINARY_DIR}/src)

add_executable(Pong main.cpp game.cpp table.cpp player.cpp wall.cpp players.cpp leftPlayer.cpp rightPlayer.cpp ball.cpp
    upperWall.cpp bottomWall.cpp leftWall.cpp rightWall.cpp)

set(CORELIBS ${SDL_LIBRARY} Graphics)

target_link_libraries(Pong ${CORELIBS} ${Boost_LIBRARIES})