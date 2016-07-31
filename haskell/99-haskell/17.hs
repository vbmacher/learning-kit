-- Problem 17
--
-- (*) Split a list into two parts; the length of the first part is given.
--
-- Do not use any predefined predicates.
--
-- Example:
--
-- * (split '(a b c d e f g h i k) 3)
-- ( (A B C) (D E F G H I K))
-- Example in Haskell:
--
-- *Main> split "abcdefghik" 3
-- ("abc", "defghik")
--

split xs n = (take n xs, drop n xs)


split' xs 0     = ([], xs)
split' (x:xs) n = add ([x], []) $ split' xs (n-1)
  where add (as,bs) (cs,ds) = (as ++ cs, bs ++ ds)

split'' :: [b] -> Int -> ([b],[b])
split'' = help ([],[])
  where help (xs,ys) is 0  = (xs, ys ++ is)
        help (xs, ys) [] _ = (xs, ys)
        help (xs, ys) (i:is) n = help (xs ++ [i], ys) is (n-1)


split''' xs n = foldl help ([],[]) xs
  where help (cs,ds) x
          | length cs == n  = (cs, ds ++ [x])
          | otherwise       = (cs ++ [x], ds)


split'''' xs n = foldr help ([],[]) xs
  where n' = length xs - n
        help x (cs,ds)
          | length ds == n' = (x:cs, ds)
          | otherwise       = (cs, x:ds)


split''''' xs n = foldl help ([],[]) $ zip xs [1..]
  where help (cs,ds) (x,c)
          | c > n      = (cs, ds ++ [x])
          | otherwise  = (cs ++ [x], ds)


split'''''' xs n = foldr help ([],[]) $ zip xs [1..]
  where help (x,c) (cs, ds)
          | c > n      = (cs, x:ds)
          | otherwise  = (x:cs, ds)


