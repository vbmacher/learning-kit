-- Problem 6
--
-- Find out whether a list is a palindrome. A palindrome can be read forward or
-- backward; e.g. (x a m a x).
--
-- Example in Haskell:
--
-- *Main> isPalindrome [1,2,3]
-- False
-- *Main> isPalindrome "madamimadam"
-- True
-- *Main> isPalindrome [1,2,4,8,16,8,4,2,1]
-- True
--

import Control.Monad

isPalindrome xs = and $ map (\ (a,b) -> a == b) $ zip (halve xs) (halve (reverse xs))
  where halve = take =<< (`div` 2) . length

isPalindrome' []     = True
isPalindrome' [x]    = True
isPalindrome' (x:xs) = (x == last xs) && (isPalindrome' (init xs))


isPalindrome'' xs = (fst two) == (take (length (fst two)) (reverse (snd two)))
  where
    half = (length xs) `div` 2
    two = splitAt half xs

isPalindrome''' xs = xs == reverse xs

isPalindrome'''' xs = foldl (\ acc (a,b) -> if acc == True then a == b else acc) True merge
  where merge = zip xs (reverse xs)

isPalindrome''''' :: (Eq a) => [a] -> Bool
isPalindrome''''' = liftM2 (==) id reverse

