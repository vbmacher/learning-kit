module View exposing (Count2D, view, clicked, boardItems)

import Dict exposing (map)
import Html exposing (div, button)
import Html.Events exposing (onClick)
import Mouse
import Collage exposing (..)
import Element exposing (toHtml)
import Color exposing (rgb)

import Board exposing (Action(..), Model)
import Styling
import Histogram exposing (histogram, histoHeight, histoWidth)

type alias Count2D = { w: Int, h: Int}
type alias Size = { w: Float, h: Float}
type alias Coord = { x: Float, y: Float }

gap : Float
gap = 20.0

boardItems : Count2D
boardItems = { w = 20, h = 10}

canvasSize : Size
canvasSize =
           { w = 2 * gap * toFloat boardItems.w
           , h = 2 * gap * toFloat boardItems.h }

view : Model -> Html.Html Action
view model = div [ Styling.rowStyle ]
            [ div [ Styling.containerStyle ]
                  [ drawBoard model.board
                  , div [ Styling.marginTop 10 ]
                        [ button [ onClick Start ] [ Html.text "Start" ]
                        , button [ onClick Stop ] [ Html.text "Stop" ]
                        ]
                  ]
            , div [ Styling.chartStyle ] [ drawHistogram model.history ]
            ]

drawBoard board = let
    iWidth = round canvasSize.w
    iHeight = round canvasSize.h

    hexagonGrid = (board |> map hexagon |> Dict.values)
  in collage iWidth iHeight hexagonGrid |> toHtml

drawHistogram history = let
    historyChart = [ traced defaultLine (histogram history) ]
  in collage histoWidth histoHeight historyChart |> toHtml

correctY x = if x % 2 == 0 then 0 else (7.0/8.0 * gap)

toCoord : (Int, Int) -> (Float, Float)
toCoord (x,y) =
              let
                 m = correctY x
                 (x_, y_) = (toFloat x, toFloat y)
              in (x_ * 1.5 * gap - canvasSize.w / 2.0
                 ,y_ * 1.75 * gap + m - canvasSize.h / 2.0 + gap)

toPos : Mouse.Position -> Maybe (Int,Int)
toPos mouse = let
    padding = -50
    x = round ((toFloat (padding + mouse.x)) / 1.5 / gap)
    m = correctY x
    y = round ((canvasSize.h - toFloat (mouse.y + padding) - m - gap) / 1.75 / gap)
  in if x >= 0 && y >= 0 then Just (x, y) else Nothing


hexstyle (x,y) alive = if alive
                       then filled (rgb 0 0 0)
                       else outlined { defaultLine | width = 3, color = rgb 200 200 200}

hexagon : (Int, Int) -> Bool -> Form
hexagon (x,y) alive = let
    coords = toCoord (x,y)
  in ngon 6 gap
     |> hexstyle (x,y) alive
     |> move coords

clicked : Mouse.Position -> Action
clicked mouse = case toPos mouse of
  Nothing  -> Noop
  Just pos -> Invert pos
