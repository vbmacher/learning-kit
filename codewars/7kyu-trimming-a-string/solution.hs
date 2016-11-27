module Codewars.StringTrimmer where

trim :: String -> Int -> String
trim str n
  | lstr str <= n   = str
  | lstr str <= 3   = shortify n str
  | otherwise       = shortify (n-3) str
  where lstr str = length str
        shortify n = (++ "...") . take n 

