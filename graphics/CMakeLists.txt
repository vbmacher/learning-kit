ADD_LIBRARY(Graphics STATIC
    canvas.cpp component.cpp table.cpp player.cpp ball.cpp wall.cpp players.cpp
)

TARGET_LINK_LIBRARIES(Graphics m)
