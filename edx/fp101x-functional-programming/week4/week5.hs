import Prelude hiding (iterate)

c f p xs = [f x | x <- xs, p x]

-- correct
-- all p xs = and (map p xs)

-- incorrect
-- all p xs = map p (and xs) 

-- correct
-- all p = and . map p

-- correct
-- all p = not . any (not . p)

-- incorrect
-- all p = map p . and


-- correct but not terminating on infinite lists
-- all p xs = foldl (&&) True (map p xs)

-- incorrect
-- all p xs = foldr (&&) False (map p xs)

-- all p = foldr (&&) True . map p


---- EX 2 --------

-- incorrect
-- any p = map p . or

-- correct
-- any p = or . map p

-- correct
-- any p xs = length (filter p xs) > 0

-- correct
-- any p = not . null . dropWhile (not . p)

-- incorrect
-- any p = null . filter p

-- correct
-- any p xs = not (all (\ x -> not (p x)) xs)

-- correct
-- any p xs = foldr (\ x acc -> (p x) || acc) False xs

-- incorrect
-- any p xs = foldr (||) True (map p xs)

--------- EX 3 ---------

-- takeWhile _ [] = []
-- takeWhile p (x:xs)
--   | p x = x : takeWhile p xs
--   | otherwise = []

------- EX 4 ------

-- dropWhile _ [] = []
-- dropWhile p (x:xs)
--   | p x = dropWhile p xs
--   | otherwise = x:xs

----- EX 5 -------

-- incorrect
-- map f = foldr (\ x xs -> f x ++ xs) [] 

-- incorrect
-- map f = foldl (\ xs x -> f x : xs) []

-- correct
-- map f = foldl (\ xs x -> xs ++ [f x]) []


------- EX 6 -----------

-- correct
-- filter p = foldr (\ x xs -> if p x then x : xs else xs) []

------ EX 7 ---------

-- dec2int :: [Integer] -> Integer
-- dec2int = foldl (\ acc x -> acc * 10 + x) 0

------- EX 8 --------

-- sumsqreven = compose [sum, map (^2), filter even]

-- compose :: [a -> a] -> (a->a)
-- compose = foldr (.) id

------- EX 9 ----------

-- curry f = \ x y -> f (x, y)

-------- EX 10 --------

-- uncurry f = \ (x, y) -> f x y

------- EX 11 ---------

unfold p h t x
  | p x = []
  | otherwise = h x : unfold p h t (t x)

-- type Bit = Int

-- chop8:: [Bit] -> [[Bit]]
-- chop8 = unfold null (take 8) (drop 8)

-------- EX 12 --------

-- map f = unfold null (f . head) tail 

--------- EX 13 --------

-- iterate f = unfold (const False) id f
 
-------- EX 14 --------












