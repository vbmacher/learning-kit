-- Problem 2
--
-- Find the last but one element of a list.
--
-- Example in Haskell:
--
-- Prelude> myButLast [1,2,3,4]
-- 3
-- Prelude> myButLast ['a'..'z']
-- 'y'

myButLast (x:_:[]) = x
myButLast (_:xs) = myButLast xs

myButLast' = foldr1 (flip const) . init

myButLast'' = last . init

myButLast''' = head . reverse . init

myButLast'''' = foldl1 (curry snd) . init

myButLast''''' = fst . foldl (\(a,b) x -> (b, x)) (err1, err2)
  where
    err1 = error "Empty list"
    err2 = error "Singleton"


