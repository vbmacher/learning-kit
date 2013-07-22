import Prelude hiding ((^),and,concat,replicate,(!!),elem)

-- correct
-- m ^ 0 = 1
-- m ^ n = m * m ^ (n - 1)

-- m ^ 0 = 1
-- m ^ n = m * m * m ^ (n - 2)

m ^ 0 = 1
m ^ n = (m * m) ^ (n -1 )

--------------------

--and [] = True
--and (b : bs)
--  | b = and bs
--  | otherwise = False

-- and [] = True
-- and (b:bs)
--   | b == False = False
--   | otherwise = and bs

and [] = True
and (b:bs) = and bs && b

-----------------------------

--concat :: [[a]] -> [a]
concat [] = []
concat (xs : xss) = xs ++ concat xss

---------------------

replicate :: Int -> a -> [a]
replicate 0 _ = []
replicate n x = x : replicate (n - 1) x

----------------------

(!!) :: [a] -> Int -> a
(x : _) !! 0 = x
(_ : xs) !! n = xs !! (n - 1)

---------------------

elem :: Eq a => a -> [a] -> Bool
elem _ [] = False
elem a (x : xs)
  | a == x = True
  | otherwise = elem a xs

------------------------

merge [] ys = ys
merge xs [] = xs
merge (x : xs) (y : ys)
  | x <= y = x : merge xs (y:ys)
  | otherwise = y : merge (x:xs) ys

--------------------

halve :: [a] -> ([a], [a])
halve xs = splitAt (length xs `div` 2) xs

msort [] = []
msort [x] = [x]
msort xs = merge (msort ys) (msort zs)
  where (ys,zs) = halve xs





