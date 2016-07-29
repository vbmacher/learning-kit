-- Problem 19
--
-- (**) Rotate a list N places to the left.
--
-- Hint: Use the predefined functions length and (++).
--
-- Examples:
--
-- * (rotate '(a b c d e f g h) 3)
-- (D E F G H A B C)
--
-- * (rotate '(a b c d e f g h) -2)
-- (G H A B C D E F)
-- Examples in Haskell:
--
-- *Main> rotate ['a','b','c','d','e','f','g','h'] 3
-- "defghabc"
--  
--  *Main> rotate ['a','b','c','d','e','f','g','h'] (-2)
--  "ghabcdef"

rotate [] _ = []
rotate ys@(x:xs) n
  | n < 0     = rotate ys (length ys + n)
  | n > 0     = rotate (xs ++ [x]) (n-1)
  | otherwise = ys

rotate' [] _ = []
rotate' ys@(x:xs) n
  | n' < 0    = rotate' ys (l + n')
  | n' > 0    = rotate' (xs ++ [x]) (n' - 1)
  | otherwise = ys
  where l = length ys
        n' = n `mod` l



