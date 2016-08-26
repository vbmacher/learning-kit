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
  gen <- getStdGen
  return $ map (\x -> xs!!x) $ take n $ randomRs (0,length xs-1) gen






