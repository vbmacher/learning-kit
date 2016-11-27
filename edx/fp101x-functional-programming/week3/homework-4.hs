-- sum100 = sum[[x * x] | x <- [1..100]]

-- correct
--sum100 = sum [x^2 | x <- [1..100]]

-- sum100 = sum [const 2 x | x <- [1 ..100]]

-- sum100 = foldl (+) (1) [x ^ 2 | x <- [1..2]]


-----------
import Prelude hiding (replicate)

replicate n a = [a | _ <- [1..n]]

----------

pyths :: Int -> [(Int, Int, Int)]
pyths n = [ (x,y,z) | x <- [1..n], y <- [1..n], z <- [1..n], x^2 + y^2 == z^2]

---------

factors n = [x | x <- [1..n], n `mod` x == 0]
perfects n = [x | x <- [1..n], sum (init (factors x)) == x]

----

find :: (Eq a) => a -> [(a,b)] -> [b]
find k t = [v | (k', v) <- t, k == k']

positions :: (Eq a) => a -> [a] -> [Int]
positions x xs = find x (zip xs [0..length xs - 1])

----

scalarproduct :: [Int] -> [Int] -> Int
-- scalarproduct xs ys = sum [x * y | (x,y) <- xs `zip` ys]
scalarproduct xs ys = sum (zipWith (*) xs ys)

-----------------------------

xs = 1 : [x + 1 | x <- xs]

-------------------

riffle :: [a] -> [a] -> [a]
riffle xs ys = concat [[x,y] | (x,y) <- zip xs ys]

-----------------

divides :: Int -> Int -> Bool
divides n x = n `mod` x == 0

divisors :: Int -> [Int]
divisors x = [d | d <- [1..x], x `divides` d]














