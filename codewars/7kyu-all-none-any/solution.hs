module Codewars.Kata.AllNoneAny where

import Prelude hiding (all, any)

all, none, any :: (a -> Bool) -> [a] -> Bool

all f = not . any (\x -> not (f x))
none f = not . any f

any f [] = False
any f (x:xs)
  | f x == True   = True
  | otherwise     = any f xs

