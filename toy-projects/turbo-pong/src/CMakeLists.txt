include_directories(${Pong_SOURCE_DIR}/src)
include_directories(${Pong_SOURCE_DIR}/graphics)
include_directories(${Pong_SOURCE_DIR}/components)
link_directories(${Pong_BINARY_DIR}/src)

add_executable(Pong main.cpp game.cpp)

set(PONGLIBS Graphics Components)

target_link_libraries(Pong ${PONGLIBS} ${SDL_LIBRARY} ${SDL_ttf_LIBRARY} ${Boost_LIBRARIES})