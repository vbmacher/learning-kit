-- Problem 14
--
-- (*) Duplicate the elements of a list.
--
-- Example:
--
-- * (dupli '(a b c c d))
-- (A A B B C C C C D D)
-- Example in Haskell:
--
-- > dupli [1, 2, 3]
-- [1,1,2,2,3,3]
--

dupli = concat . map (\ a -> [a,a])


dupli' [] = []
dupli' (x:xs) = x:x:dupli' xs


dupli'' = foldl (\acc a -> acc ++ [a,a]) []


dupli''' = foldr (\a acc -> a:a:acc) []


dupli'''' xs = xs >>= (\a -> [a,a])


dupli''''' xs = concat [[x,x] | x <- xs]


dupli'''''' = concatMap $ replicate 2





