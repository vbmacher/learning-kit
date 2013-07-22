-- Problem 23
-- Extract a given number of randomly selected elements from a list.
--
-- Example:
--
-- * (rnd-select '(a b c d e f g h) 3)
-- (E D A)
-- Example in Haskell:
--
-- Prelude System.Random>rnd_select "abcdefgh" 3 >>= putStrLn
-- eda


import System.Random

rnd_select xs n = do
  gen <- newStdGen
  return $ map (xs!!) $ take n $ randomRs (0,length xs-1) gen


rnd_select' xs 0 = return []
rnd_select' xs n = do
  newStdGen
  i <- randomRIO (0, length xs -1)
  rest <- rnd_select' xs (n-1)
  return $ (xs !! i) : rest


