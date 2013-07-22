-- Problem 16
--
-- (**) Drop every N'th element from a list.
--
-- Example:
--
-- * (drop '(a b c d e f g h i k) 3)
-- (A B D E G H K)
-- Example in Haskell:
--
-- *Main> dropEvery "abcdefghik" 3
-- "abdeghk"


dropEvery [] _ = []
dropEvery xs n = take (n-1) xs ++ dropEvery (drop n xs) n


dropEvery' xs n = [x | (x,y) <- zip xs [1..], y `mod` n /= 0]


dropEvery'' xs n = snd $ foldl onlyFirstN (0,[]) xs
  where
    onlyFirstN (c,xs) x
      | c == n-1   = (0,xs)
      | otherwise  = (c+1,xs ++ [x])

dropEvery''' xs n = snd $ foldr onlyFirstN (length xs `mod` n, []) xs
  where
    onlyFirstN x (c,xs)
      | c == 0   = (n-1,xs)
      | otherwise  = (c-1, x:xs)


dropEvery'''' xs n = foldl acc [] $ zip [1..] xs
  where acc rs pair
          | (fst pair) `mod` n /= 0 = rs ++ [snd pair]
          | otherwise               = rs


