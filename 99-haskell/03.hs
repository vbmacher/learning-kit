-- Problem 3
--
-- Find the K'th element of a list. The first element in the list is number 1.
--
-- Example in Haskell:
--
-- Prelude> elementAt [1,2,3] 2
-- 2
-- Prelude> elementAt "haskell" 5
-- 'e'
--

elementAt xs n = xs !! (n-1)

elementAt' xs n = (result . foldl acc (err,0)) xs
  where
    err = error "Empty list"
    acc (a,c) x
      | c < n     = (x, c + 1)
      | otherwise = (a,c)
    result (a,c)
      | c < n     = error "Empty list"
      | otherwise = a 

elementAt'' (x:xs) n
  | n > 1     = elementAt'' xs (n-1)
  | n == 1    = x

elementAt''' xs n = (head . (drop (n-1))) xs

elementAt'''' = flip $ (last .) . take . (+1)

