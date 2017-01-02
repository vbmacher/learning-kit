module Board exposing (..)

import Debug
import Time exposing (Time)
import Dict exposing (Dict, fromList, get, member)
import List exposing (map, concat, repeat, range, filter, length, map2)

type Action = Tick Time | Invert (Int,Int) | Start | Stop | Noop
type alias Board = Dict (Int,Int) Bool
type alias Model = { board: Board
                   , started: Bool
                   , history: List Int
                   }

createBoard : Int -> Int -> Board
createBoard w h = let
    positions = concat (map (\x -> map (\y -> (x,y)) (range 0 h)) (range 0 w))
    states    = repeat ((w+1) * (h+1)) False
    zip       = map2 (,)
  in zip positions states |> Dict.fromList

invertItem : Board -> (Int, Int) -> Board
invertItem board pos = case get pos board of
  Just True   -> Dict.update pos (\_ -> Just False) board
  Just False  -> Dict.update pos (\_ -> Just True) board
  _           -> board

gameStep : Board -> Board
gameStep board = let
    surroundings (x, y) = let
        m = x % 2
      in [ (x - 1,   y + m)    , (x    ,   y + 1), (x + 1,   y + m)
         , (x + 1,   y - 1 + m), (x    ,   y - 1), (x - 1,   y - 1 + m)
         ]

    getFromMaybe s = case s of
      Just n -> n
      Nothing -> False

    neighbours (x,y) = surroundings (x,y)
                     |> map (\s -> get s board)
                     |> filter getFromMaybe
                     |> length

    survived n = n >= 3 && n <= 5
    born n = n == 2

    liveness board = \pos state -> if state
                                   then survived (neighbours pos)
                                   else born (neighbours pos)

  in Dict.map (liveness board) board

lives : Board -> Int
lives board = let
    stateToInt s = if s then 1 else 0
  in List.sum (List.map stateToInt (Dict.values board))

update : Action -> Model -> (Model, Cmd Action)
update msg model = let
    noAction = (model, Cmd.none)
  in case msg of
    Invert pos -> ({model | board=invertItem model.board pos}, Cmd.none)
    Tick _     -> if model.started
                  then ({model | board=gameStep model.board
                               , history=lives (model.board)::model.history
                        }, Cmd.none)
                  else noAction
    Start      -> ({model | started = True}, Cmd.none)
    Stop       -> ({model | started = False}, Cmd.none)
    Noop       -> noAction
