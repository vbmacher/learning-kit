-- Problem 13
--
-- (**) Run-length encoding of a list (direct solution).
--
-- Implement the so-called run-length encoding data compression method directly.
-- I.e. don't explicitly create the sublists containing the duplicates, as in problem 9,
-- but only count them. As in problem P11, simplify the result list by replacing the
-- singleton lists (1 X) by X.
--
-- Example:
--
-- * (encode-direct '(a a a a b c c a a d e e e e))
-- ((4 A) B (2 C) (2 A) D (4 E))
-- Example in Haskell:
--
-- P13> encodeDirect "aaaabccaadeeee"
-- [Multiple 4 'a',Single 'b',Multiple 2 'c',
--  Multiple 2 'a',Single 'd',Multiple 4 'e']


data Encoded a = Single a | Multiple Int a deriving (Show)

encodeDirect [] = []
encodeDirect (x:xs) = encode x xs 0
  where encode x [] 0     = [Single x]
        encode x [] n     = [Multiple (n+1) x]
        encode x (y:ys) n
          | x == y        = encode x ys (n+1)
          | n == 0        = (Single x) : (encode y ys 0)
          | otherwise     = (Multiple (n+1) x) : (encode y ys 0)


encodeDirect' [] = []
encodeDirect' (x:xs) = (decide (1 + (length . takeWhile (==x)) xs) x) : (encodeDirect' $ dropWhile (==x) xs)
  where decide 1 x = Single x
        decide n x = Multiple n x

encodeDirect'' [] = []
encodeDirect'' (x:xs) = (decide (1 + length ys)) : (encodeDirect'' zs)
  where decide 1 = Single x
        decide n = Multiple n x
        (ys, zs) = span (==x) xs

encodeDirect''' :: Eq a => [a] -> [Encoded a]
encodeDirect''' = map helper . foldr acc []
  where acc x [] = [(x, 1)]
        acc x (y@(a, c):xs)
            | a == x     = (a, c+1):xs
            | otherwise  = (x, 1):y:xs 
        helper (a,1) = Single a
        helper (a,n) = Multiple n a

