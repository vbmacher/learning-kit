-- module Main (main) where
import Data.List
import Data.Char

data Nat = Zero
         | Succ Nat
         deriving Show

-- ex 0

-- correct 
-- natToInteger Zero = 0
-- natToInteger (Succ n) = natToInteger n + 1

-- correct
-- natToInteger (Succ n) = natToInteger n + 1
-- natToInteger Zero = 0

-- correct
-- natToInteger = head . m
--   where m Zero = [0]
--         m (Succ n) = [sum [x | x <- (1 : m n)]]

-- correct
-- natToInteger :: Nat -> Integer
-- natToInteger = \n -> genericLength [c | c <- show n, c == 'S']

-- incorrect
-- natToInteger :: Nat -> Integer
-- natToInteger = \n -> length [c | c <- show n, c == 'S']

----- ex 1

-- correct
-- integerToNat 0 = Zero
-- integerToNat n = Succ (integerToNat (n-1))

-- incorrect
-- integerToNat n
--   = product [(unsafeCoerce c) :: Integer | c <- show n]

-- integerToNat n = let m = integerToNat (n-1) in Succ m
-- integerToNat 0 = Zero

--------- ex 2

-- correct
-- add Zero n = n
-- add (Succ m) n = Succ (add n m)

-- correct
-- add (Succ m) n = Succ (add n m)
-- add Zero n = n

-- correct
-- add n Zero = n
-- add n (Succ m) = Succ (add m n)

-- correct
add n (Succ m) = Succ (add m n)
add n Zero = n

------------ ex 3

mult m Zero = Zero
mult m (Succ n) = add m (mult m n)

-------- ex 4

-- data Tree = Leaf Integer
--           | Node Tree Integer Tree

-- occurs :: Integer -> Tree -> Bool

-- correct
-- occurs m (Leaf n) = m == n
-- occurs m (Node l n r)
--   = case compare m n of
--         LT -> occurs m l
--         EQ -> True
--         GT -> occurs m r

-- incorrect
-- occurs m (Leaf n) = m == n
-- occurs m (Leaf l n r)
--   = case compare m n of
--         LT -> occurs m r
--         EQ -> True
--         GT -> occurs m l

-- correct
-- occurs m (Leaf n) = m == n
-- occurs m (Node l n r)
--   | m == n = True
--   | m < n = occurs m l
--   | otherwise = occurs m r

-------- ex 5

data Tree = Leaf Integer
          | Node Tree Tree
          deriving Show

leaves (Leaf _) = 1
leaves (Node l r) = leaves l + leaves r

balanced :: Tree -> Bool
balanced (Leaf _) = True
balanced (Node l r)
  = abs (leaves l - leaves r) <= 1 && balanced l && balanced r

---------- ex 6

balance :: [Integer] -> Tree

-- my implementation
-- halve xs = splitAt ((length xs) `div` 2) xs
-- balance [x] = Leaf x
-- balance xs = Node (balance (fst $ halve xs)) (balance (snd $ halve xs))

halve xs = splitAt (length xs `div` 2) xs
balance [x] = Leaf x
balance xs = Node (balance ys) (balance zs)
  where (ys, zs) = halve xs






