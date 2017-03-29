-- 10 Problem 20
-- (*) Remove the K'th element from a list.
--
-- Example in Prolog:
--
-- ?- remove_at(X,[a,b,c,d],2,R).
-- X = b
-- R = [a,c,d]
-- Example in Lisp:
--
-- * (remove-at '(a b c d) 2)
-- (A C D)
-- (Note that this only returns the residue list, while the Prolog version also returns the deleted element.)
--
-- Example in Haskell:
--
-- *Main> removeAt 2 "abcd"
-- ('b',"acd")

removeAt n xs = (head $drop (n-1) xs, take (n-1) xs ++ drop n xs)

removeAt' n xs = foldl f (' ',[]) $ zip [1..] xs
  where f (ch,rest) (n',c)
          | n' == n = (c,rest)
          | otherwise = (ch, rest ++ [c])


removeAt'' n xs = foldr f (' ', []) $ zip [1..] xs
  where f (n',c) (ch,rest)
          | n' == n = (c, rest)
          | otherwise = (ch, c:rest)


removeAt''' n xs = rem 1 xs [] Nothing
  where rem n' (x:xs) ys r
          | n' == n   = (Just x, ys ++ xs)
          | otherwise = rem (n'+1) xs (ys ++ [x]) r


removeAt'''' n xs = (find, [x | (n',x) <- zip [1..] xs, n' /= n])
  where find = head $ drop (n-1) xs





