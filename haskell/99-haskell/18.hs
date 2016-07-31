-- Problem 18
--
-- (**) Extract a slice from a list.
--
-- Given two indices, i and k, the slice is the list containing the elements between the i'th and k'th element of the original
-- list (both limits included). Start counting the elements with 1.
--
-- Example:
--
-- * (slice '(a b c d e f g h i k) 3 7)
-- (C D E F G)
-- Example in Haskell:
--
-- *Main> slice ['a','b','c','d','e','f','g','h','i','k'] 3 7
-- "cdefg"
--

slice xs i j = take (j-i+1) $ drop (i-1) xs


slice' xs i j = foldl accu [] $ zip xs [1..j]
  where accu xs (a,n)
          | n >= i      = xs ++ [a]
          | otherwise   = xs


slice'' xs i j = foldr accu [] $ zip xs [1..j]
  where accu (a,n) xs
          | n >= i     = a:xs
          | otherwise  = xs


slice''' xs i j = [a | (a,n) <- zip xs [1..j], n >= i]


slice'''' xs i j = map fst $ filter ((>=i) . snd) $ zip xs [1..j]


slice''''' xs i j = fst $ splitAt (j-i+1) $ snd $ splitAt (i-1) xs


slice'''''' xs i j = fst $ unzip $ filter ((>=i) . snd) $ zip xs [1..j]



slice''''''' xs 1 0 = []
slice''''''' (x:xs) 1 j = x : (slice''''''' xs 1 (j-1))
slice''''''' (x:xs) i j = slice''''''' xs (i-1) (j-1)


