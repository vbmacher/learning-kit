module BedmintonView exposing (..)

import Html exposing (Html, button, div, text, body,main_,header)
import Html.Events exposing (onMouseUp, onClick)
import Html.Attributes exposing (style)

import BedmintonModel as BM

(=>) = (,)

build : List (List (String, String)) -> Html.Attribute msg
build styles = style <| List.concat styles

scoreStyle = style
  [ "display" => "flex"
    , "flex-direction" => "row"
    , "justify-content" => "center"
    , "width" => "100%"
    , "height" => "50vh"
    , "align-items" => "center"
    , "align-contents" => "center"
  ]

textStyle = style
  [ "font-size" => "50vmin"
    , "align-items" => "center"
    , "font-family" => "Helvetica,sans-serif"
    , "font-weight" => "bold"
    , "height" => "100%"
  ]

invertStyle model = style
  [ "background-color" => model.icolor
  , "width" => "100px"
  , "height" => "100%"
  , "color" => "white"
  , "align-self" => "flex-start"
  , "margin-right" => "auto"
  ]

growStyle = style
  [ "flex-grow" => "2"
  , "display" => "flex"
  , "flex-direction" => "row"
  , "width" => "100%"
  , "justify-content" => "center"
  , "height" => "100%"
  , "align-items" => "center"
  , "align-contents" => "center"
  ]

fst (a,b) = a
snd (a,b) = b

nfuncI f = \x -> (f (-1)) * x

scorer m = onClick (m 1)
inverter model = onClick (BM.NFunc (nfuncI model.nfunc))

state : BM.Model -> ((Int,Int) -> a) -> Html msg
state model f = text (model.state |> f |> toString)

view : BM.Model -> Html BM.Msg
view model = div [] [
    div [scoreStyle] [
      div [inverter model, invertStyle model] [ ],
      div [growStyle] [
        div [scorer BM.Left, textStyle] [ state model fst ],
        div [textStyle] [ text ":" ],
        div [scorer BM.Right, textStyle] [ state model snd ]
      ]
    ]
  ]
