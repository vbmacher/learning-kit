module Histogram exposing (..)

import List exposing (map, map2, range)
import Collage exposing (Path, path)

histoWidth = 200
histoHeight = 200

histogram : List Int -> Path
histogram xs = let
    max = List.foldl (\x a -> if x > a then x else a) 0 xs
    len = List.length xs

    xc i = (histoWidth * toFloat i) / toFloat len - histoWidth / 2
    yc a = (histoHeight * toFloat a) / toFloat max - histoHeight / 2

    series = map2 (,) (range 0 len) xs
  in path (map (\(i,a) -> (xc i, yc a)) series)
