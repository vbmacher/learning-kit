-- Problem 21
--
-- Insert an element at a given position into a list.
--
-- Example:
--
-- * (insert-at 'alfa '(a b c d) 2)
-- (A ALFA B C D)
-- Example in Haskell:
--
-- P21> insertAt 'X' "abcd" 2
-- "aXbcd"

import Debug.Trace

insertAt x xs 1 = x:xs
insertAt x (y:ys) n = y : (insertAt x ys (n-1))


insertAt' x xs n = (take (n-1) xs) ++ [x] ++ (drop (n-1) xs)


insertAt'' x xs n = let (l,r) = splitAt (n-1) xs in l ++ [x] ++ r


insertAt''' x xs n = snd $ foldl f (1,[]) xs
  where f (c,ys) y
          | c == n    = trace ("c==n==" ++ show n ++ ", " ++ show ys) (c+1, ys ++ [x,y])
          | otherwise = trace ("c==" ++ show c ++ ", " ++ show ys) (c+1, ys ++ [y])


insertAt'''' x xs n = foldr f [] (zip [1..] xs)
  where f (c,y) ys
          | c == n    = trace ("c==n==" ++ show n ++ ", " ++ show ys) (x:y:ys)
          | otherwise = trace ("c==" ++ show c ++ ", " ++ show ys) (y:ys)
  
