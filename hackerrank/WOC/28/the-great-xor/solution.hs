import Control.Applicative
import Control.Monad
import System.IO


-- satisfy a xor x > x
-- while 0 < a < x


-- # how to get all smaller numbers than x?
-- - I find the righ-most 1
-- - change it to 0 and set to 1 all digits to the right
-- - I found such number; repeat until I reach 0 globally
--
-- # how to determine if a xor x > x ?
-- - if the left-most 1 in a is more to the left than the
--   left-most 1 in x, then the condition is true
-- - if they equal, the condition is false
-- - otherwise, keep searching to the right
--
-- # heuristics - next relevant "lower number" ?
-- - is there a better way? certainly!
-- - count all zeros in the number - with all combinations
--   of numbers to the right of the zero, and we have the
--   whole result!
--
-- E.g.:
--
-- 1010 (10)
--
-- we have 2 zeros, giving the following:
--   - 0xx - xx can be: 00,01,10,11
--   - 0   - just one option: 1
--
-- so the result is 5
--
-- # how to compute the combinations?
-- it's the same asking how many numbers are in x bits?
-- for 2 bits, we have 00,01,10,11 -> 4 numbers
-- for 3 bits, we have 000,001,010,011,100,101,110,111 -> 8 bits
--
-- so apparently it's 2^x
--
-- so the complete solution is:
-- 1. make a binary by long division
-- 2. go from the right to left, count digits until getting 0
-- 3. then add to the counter 2^(current number of digits)
--    and continue with point 2
-- 4. print the counter on screen

div2 :: String -> (Int, String)
div2 = foldl f (0,[])
  where f (rem, ys) x = let
            xi = read [x] :: Int
            num = show $ (xi `div` 2) + rem 
          in (5 * (xi `mod` 2), ys ++ num)

tobin :: String -> String -> String
tobin xs bs = case yys of
    [] -> bins
    _  -> tobin yys bins
  where (rem, ys) = div2 xs
        bins = if rem == 0 then ('0':bs) else ('1':bs)
        yys = dropWhile (=='0') ys

zeros :: String -> [Int] 
zeros = map fst . filter (\(_,n) -> n =='0') . zip [0..] . reverse

count :: [Int] -> Integer
count = foldr (\x a -> a + (2^x)) 0



main :: IO ()
main = do
    q_temp <- getLine
    let q = read q_temp :: Int
    forM_ [1..q] $ \a0  -> do
        x_temp <- getLine
        let c = (count . zeros . tobin x_temp) []
        putStrLn $ show c
