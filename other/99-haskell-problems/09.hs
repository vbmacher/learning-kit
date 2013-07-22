-- Problem 9
--
-- (**) Pack consecutive duplicates of list elements into sublists. If a list contains
-- repeated elements they should be placed in separate sublists.
--
-- Example:
--
-- * (pack '(a a a a b c c a a d e e e e))
-- ((A A A A) (B) (C C) (A A) (D) (E E E E))
-- Example in Haskell:
--
-- *Main> pack ['a', 'a', 'a', 'a', 'b', 'c', 'c', 'a', 
--              'a', 'd', 'e', 'e', 'e', 'e']
--              ["aaaa","b","cc","aa","d","eeee"]

pack [] = []
pack (x:xs) = (x:(takeWhile (==x) xs)) : (pack $ dropWhile (==x) xs)

pack' xs = joint $ foldl merge ([],[]) xs
  where
    merge ([],[]) x = ([x],[])
    merge (ys, zs) x
      | x == head ys  = (x:ys, zs)
      | otherwise     = ([x], zs ++ [ys])
    joint (ys,zs) = zs ++ [ys]

pack'' xs = joint $ foldr merge ([],[]) xs
  where
    merge x ([], []) = ([x], [])
    merge x (ys, zs)
      | x == head ys = (x:ys, zs)
      | otherwise    = ([x], ys:zs)
    joint (ys, zs) = ys:zs

pack''' [] = []
pack''' xs = ys:pack''' zs 
  where (ys,zs) = span (==head xs) xs





