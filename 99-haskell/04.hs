-- Problem 4
--
-- Find the number of elements of a list.
--
-- Example in Haskell:
--
-- Prelude> myLength [123, 456, 789]
-- 3
-- Prelude> myLength "Hello, world!"
-- 13

myLength = foldr (\x acc -> acc+1) 0

myLength' = sum . map (const 1)

myLength'' = foldl (\acc x -> acc+1) 0

myLength''' [] = 0
myLength''' (x:xs) = 1 + myLength''' xs

myLength'''' = fst . last . zip [1..]

