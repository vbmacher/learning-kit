ADD_LIBRARY(Components STATIC
    component.cpp ball.cpp bottomWall.cpp leftWall.cpp rightWall.cpp upperWall.cpp leftPlayer.cpp rightPlayer.cpp
    player.cpp players.cpp table.cpp wall.cpp
)

TARGET_LINK_LIBRARIES(Components m)
