-- Problem 1
--
-- Find the last element of a list.
--
-- Example in Haskell:
--
-- Prelude> myLast [1,2,3,4]
-- 4
-- Prelude> myLast ['x','y','z']
-- 'z'


myLast = head . reverse

myLast' [x] = x
myLast' (_:xs) = myLast' xs

myLast'' xs = xs !! (length xs - 1)

myLast''' = foldr1 (flip const)

myLast'''' = foldl1 (curry snd)


