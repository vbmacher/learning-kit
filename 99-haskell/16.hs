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



