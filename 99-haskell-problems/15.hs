-- Problem 15
--
-- (**) Replicate the elements of a list a given number of times.
--
-- Example:
--
-- * (repli '(a b c) 3)
-- (A A A B B B C C C)
-- Example in Haskell:
--
-- > repli "abc" 3
-- "aaabbbccc"
--

repli xs c = concatMap (replicate c) xs


repli' xs c = concat [replicate c x | x <- xs]


repli'' = flip replii
  where replii c = concatMap $ replicate c


repli''' xs c = foldl (\acc a -> acc ++ replicate c a) [] xs


repli'''' xs c = foldr (\a acc -> (replicate c a) ++ acc) [] xs


repli''''' = flip $ concatMap . replicate


repli'''''' xs c = xs >>= replicate c


repli''''''' [] _     = []
repli''''''' (x:xs) c = foldr (const (x:)) (repli''''''' xs c) [1..c]










