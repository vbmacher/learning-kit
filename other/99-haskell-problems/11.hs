-- Problem 11
-- (*) Modified run-length encoding.
--
-- Modify the result of problem 10 in such a way that if an element has no duplicates
-- it is simply copied into the result list. Only elements with duplicates are transferred
-- as (N E) lists.
--
-- Example:
--
-- * (encode-modified '(a a a a b c c a a d e e e e))
-- ((4 A) B (2 C) (2 A) D (4 E))
-- Example in Haskell:

-- encodeModified "aaaabccaadeeee"
-- [Multiple 4 'a',Single 'b',Multiple 2 'c',
-- Multiple 2 'a',Single 'd',Multiple 4 'e']

import Data.List(group)

data Encoded a = Single a | Multiple Int a deriving(Show)


encode :: Eq a => [a] -> [(Int,a)]
encode = map (\x -> (length x, head x)) . group 


encodeModified :: Eq a => [a] -> [Encoded a]
encodeModified = map transform . encode
  where transform (1,a) = Single a
        transform (c,a) = Multiple c a


encodeModified' xs = [mx | x <- group xs, let mx = if (length x) == 1 then Single (head x) else Multiple (length x) (head x)]
