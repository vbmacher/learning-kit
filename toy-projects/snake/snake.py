from random import randint
from asciimatics.screen import Screen
import time

def copy(snake, direction, w, h):
    snake.insert(0, ((snake[0][0] + direction[0]) % w, (snake[0][1] + direction[1]) % h))

def move(snake, direction, w, h):
    copy(snake, direction, w, h)
    snake.pop()


def food(snake, w, h):
    (xs,ys) = list(zip(*snake))

    (x, y) = (randint(0, w-1), randint(0, h-1))
    while x in xs: x = randint(0, w-1)
    while y in ys: y = randint(0, h-1)

    return (x, y)


def new_direction(screen, direction):
    screen.wait_for_input(0.1)
    ev = screen.get_key()

    if ev in (ord('Q'), ord('q')):                        return (0,0)
    elif ev == screen.KEY_UP and direction != (0, 1):     return (0, -1)
    elif ev == screen.KEY_DOWN and direction != (0, -1):  return (0, 1)
    elif ev == screen.KEY_RIGHT and direction != (-1, 0): return (1, 0)
    elif ev == screen.KEY_LEFT and direction != (1, 0):   return (-1, 0)
    else: return direction


def draw(screen, snake, c):
    for (x, y) in snake:
        screen.print_at(c, x, y)

def found_food(snake, fx, fy):
    return snake[0][0] == fx and snake[0][1] == fy

def crashes(snake):
    head, *tail = snake
    return head in tail



def game(screen):

    w = screen.width-1
    h = screen.height-1

    # initial position
    snake = [(randint(0, w-1), randint(0, h-1))]
    direction = (1,0)
    (fx, fy) = food(snake, w, h)
    started = time.time()

    while True:
        if crashes(snake):
            print("CRASH")
            time.sleep(1)
            return

        draw(screen, snake, '*')
        screen.print_at('X', fx, fy, colour=randint(1,7))
        screen.refresh()
       
        direction = new_direction(screen, direction)
        if direction == (0,0):
            return

        if found_food(snake, fx, fy):
          copy(snake, direction, w, h) # snake is prolonged
          (fx, fy) = food(snake, w, h)
        else:
          draw(screen, snake, ' ') # snake is not prolonged
          move(snake, direction, w, h)

        elapsed = int(time.time() - started)
        screen.print_at("score: {}, time: {}".format(len(snake)-1, elapsed), 0, h-1)
        screen.refresh()


Screen.wrapper(game)
