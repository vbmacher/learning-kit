module Styling exposing (..)

import Html.Attributes exposing (style)

(=>) : a -> b -> (a,b)
(=>) = (,)

containerStyle = style
               [ "display" => "flex"
               , "flex-direction" => "column"
               , "padding" => "50px"
               ]

rowStyle = style
         [ "display" => "flex"
         , "flex-direction" => "row"
         ]

chartStyle = style
           [ "border" => "1px solid grey"
           ]

marginTop x = style [ "margin-top" => String.append (toString x) "px" ]
