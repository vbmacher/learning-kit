module Kata.InvisibleCubes where

notVisibleCubes :: Integer -> Integer
notVisibleCubes n
  | n < 3     = 0
  | otherwise = let i = n - 2 in i ^ 3

