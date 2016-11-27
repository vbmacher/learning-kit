import Prelude hiding (last, foldr, init, drop, (++), foldl)

-- overlapping
last :: [a] -> a
last [x] = x
last (_ : xs) = last xs

-- non-overlapping
foldr :: (a->b->b) -> b -> [a] -> b
foldr f v (x : xs) = f x $ foldr f v xs
foldr _ v [] = v

-- overlapping
init [_] = []
init (x:xs) = x : init xs

-- overlapping
drop 0 xs = xs
drop n [] = []
drop n (_ : xs) = drop (n-1) xs

-- non-overlapping
(x:xs) ++ ys = x : (xs ++ ys)
[] ++ ys = ys

-- non-overlapping
foldl f v (x:xs) = foldl f (f v x) xs
foldl _ v [] = v



data Nat = Zero
         | Succ Nat


add :: Nat -> Nat -> Nat
add Zero m = m
add (Succ n) m = Succ (add n m)

-------- ex 1
-- prove that: add n (Succ m) = Succ (add n m)
-- Base case:
-- add Zero (Succ m) =
--   (applying add)
-- = Succ m
--   (unapplying add)
--   Succ (add Zero m)
--
-- Assumption: add n (Succ m) = Succ (add n m) 
--
-- Induction:
-- add (Succ n) (Succ m) =
--   (applying add)
-- = Succ (add n (Succ m))
--   (hypothesis)
-- = Succ (Succ (add n m))
--   (unapplying add)
-- = Succ (add (Succ n) m)

-------- ex 2
-- prove that: add n m = add m n
--   use:
--        1. add n (Succ m) = Succ (add n m)
--        2. add n Zero = n
-- Base case:
-- add Zero m =
--   (applying add)
-- = m
--   (unapplying add - property 2)
-- = add m Zero 
--
-- Hypothesis: add n m = add m n
--
-- Induction:
-- add (Succ n) m =
--   (applying add)
-- = Succ (add n m)
--   (hypothesis)
-- = Succ (add m n)
--   (property 1)
-- = add m (Succ n)

------- ex 3
--
-- prove that: all (== x) (replicate n x)
--
-- Base case (n = 0): 
-- all (==x) (replicate 0 x)
--   (applying replicate)
-- all (==x) []
--   (applying all)
-- True
--
-- Hypothesis:
--   all (==x) (replicate n x) == True
--
-- Induction:
-- all (==x) (replicate (n+1) x)
--   (applying replicate)
-- all (==x) (x:replicate n x)
--   (applying all)
-- (x==x) && all (==x) (replicate n x)
--   (hypothesis)
-- (x==x) && True
--   (==)
-- True && True
--   (&&)
-- True

------------- ex 4
--
-- prove that: xs ++ [] = xs
--
-- Base case:
-- [] ++ []
--   (applying ++)
-- = []
--
-- Hypothesis: xs ++ [] = xs
--
-- Induction:
-- (x:xs) ++ []
--   (applying ++)
-- = x : (xs ++ [])
--   (induction hypothesis)
-- = x : xs

-------------- ex 5
--
-- prove that: xs ++ (ys ++ zs) = (xs ++ ys) ++ zs (associativity)
-- use:
--      1. xs ++ [] = xs
--
-- Base case (xs == []):
-- [] ++ (ys ++ zs)
--   (applying outer ++)
-- = ys ++ zs
--   (unapplying ++)
-- = ([] ++ ys) ++ zs
--
-- Hypothesis:
--   xs ++ (ys ++ zs) = (xs ++ ys) ++ zs
--
-- Induction:
-- (x:xs) ++ (ys ++ zs)
--   (applying outer ++)
-- x : (xs ++ (ys ++ zs))
--   (hypothesis)
-- x : ((xs ++ ys) ++ zs)
--   (unapplying ++)
-- (x : (xs ++ ys)) ++ zs
--   (unapplying ++)
-- ((x:xs) ++ ys) ++ zs


--------- ex 6
--
-- prove that: map f (map g xs) = map (f . g) xs
--
-- Base case (xs = []):
-- map f (map g []) =
--   (applying map)
-- map f []
--   (applying map)
-- []
--   (unapplying map)
-- map (f . g) []
--
-- Hypothesis:
--   map f (map g xs) = map (f . g) xs
--
-- Induction:
-- map f (map g (x:xs))
--   (applying map)
-- map f (g x : map g xs)
--   (applying map)
-- f (g x) : map f (map g xs)
--   (hypothesis)
-- f (g x) : map (f . g) xs
--   (unapplying .)
-- (f . g) x : map (f . g) xs
--   (unapplying map)
-- map (f . g) (x:xs)

-------------- ex 7
--
-- prove that: length (xs ++ ys) = length xs + length ys
--
-- Base case (xs = [])
-- length ([] ++ ys)
--   (applying ++)
-- = length ys
--   (applying the identity element for +)
-- = 0 + length ys
--   (unapplying length)
-- = length [] + length ys
--
-- Hypothesis: length (xs ++ ys) = length xs + length ys
--
-- Induction:
-- length (x:xs ++ ys)
--   (applying ++)
-- = length (x : (xs ++ ys))
--   (applying length)
-- = 1 + length (xs ++ ys)
--   (hypothesis)
-- = 1 + (length xs + length ys)
--   (associativity of +)
-- = (1 + length xs) + length ys)
--   (unapplying length)
-- = length (x:xs) + length ys

------------ ex 9
--
-- prove that: length (take n (repeat x)) = n (induction on n) 
--
-- Base case (n = 0):
-- length (take 0 (repeat x))
--   (applying take)
-- = length []
--   (applying length)
-- = 0
--
-- Hypothesis: length (take n (repeat x)) = n
--
-- Induction step:
-- length (take (n+1) (repeat x))
--   (applying repeat)
-- = length (take (n+1) (x : repeat x))
--   (applying take)
-- = length (x : take n (repeat x))
--   (applying length)
-- = 1 + length (take n (repeat x))
--   (induction hypothesis)
-- = 1 + n
--   (commutativity of +)
-- = n + 1


