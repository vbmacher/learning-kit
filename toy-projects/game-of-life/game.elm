import Mouse
import Html
import Time exposing (every, second)

import Board exposing (Board, Model, Action(..), createBoard, update)
import View exposing (..)

init : Count2D -> Model
init items =
           { board = createBoard items.w items.h
           , started = False
           , history = []
           }

main = Html.program
      { init = (init boardItems, Cmd.none)
      , update = update
      , view = view
      , subscriptions =
        \_ -> Sub.batch
          [ every (second / 4) Tick
          , Mouse.clicks clicked
          ]
      }
