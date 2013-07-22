Story Teller
=============

It's a simple 2D game maker engine written in Java/Groovy.
An user specifies a game file (custom Groovy DSL) and draws
static images for the game. The game file contains definition
of game objects, rooms and game action code in very simple
form. And that's it - the game can now be played via this
engine.

The engine creates main window. The window size is taken
from the background image of the current room.
It draws the background, and then all objects inside the
room. After this all action code is initialized. All game
logic is provided by the engine and user just plays.
He can use objects directly or store them into an
inventory. Each room has a question with some possible
answers. Choosing any answer brings the player to another
room. Entering a (new) room has potential impact on the
game progress.

Game language and more examples will be continuously added.