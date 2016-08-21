-- Problem 22
-- Create a list containing all integers within a given range.
--
-- Example:
--
-- * (range 4 9)
-- (4 5 6 7 8 9)
-- Example in Haskell:
--
-- Prelude> range 4 9
-- [4,5,6,7,8,9]

range n m = [n..m]

range' n m = take (m-n+1) $ scanl (+) n $ repeat 1

range'' n m = take (m-n+1) $  [n..]

range''' n m
  | n == m = [m]
  | n < m  = (range n (m-1)) ++ [m]






