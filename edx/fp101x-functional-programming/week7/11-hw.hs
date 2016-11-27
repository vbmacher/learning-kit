-- ex 6
-- my implementation
-- fibs :: [Integer]
-- fibs = 1 : 1 : zipWith (+) fibs (tail fibs)

fibs = 0 : 1 : [x + y | (x,y) <- zip (tail fibs) fibs]

-- ex 7
fib n = fibs !! n

-- ex 8
largeFib :: Integer
largeFib = head (dropWhile (<= 1000) fibs)

-- ex 9
data Tree a = Leaf
            | Node (Tree a) a (Tree a)

repeatTree :: a -> Tree a
repeatTree x = Node t x t
  where t = repeatTree x





