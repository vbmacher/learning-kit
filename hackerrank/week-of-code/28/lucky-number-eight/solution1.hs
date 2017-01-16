import Control.Monad
import Data.List
import Data.Bits

-- We will use "binary" mapping which tells us the
-- combinations.
positions :: String -> [[Int]]
positions xs = [pos (i::Int) | i <- [0..(2^l)-1]]
  where l = length xs
        pos n = [i | i <- [0..l], n .&. 2^i == 2^i]

combs :: String -> [String]
combs xs = tail $ map f $ positions xs
  where f ps = foldr (\p ys -> (xs !! p):ys) [] ps

-- Recursive variants are NOT OK!
--
-- For example, this:
--    combs [] = []
--    combs (x:xs) = ([x]:xss) ++ (map (x:) xss)
--      where xss = combs xs
--
-- Or this nice one also:
--    combs = init . filterM (const [True, False])

lastThrees :: [String] -> [String]
lastThrees = map (reverse. take 3)

toInts :: [String] -> [Int]
toInts = map read

div8 :: [Int] -> [Int]
div8 = filter (\x -> x `mod` 8 == 0)

count :: [Int] -> Int
count = foldl' f 0
  where f a _ = (a + 1) `mod` 1000000007


main = do
  strDigits <- getLine
  strNumber <- getLine
  let digits = read strDigits
  let divsBy8 = (div8 . toInts . lastThrees . combs) (reverse $ take digits strNumber)
  putStrLn $ show $ count divsBy8
