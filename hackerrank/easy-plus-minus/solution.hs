import Control.Applicative
import Control.Monad
import System.IO
import Data.Function

intDiv = (/) `on` fromIntegral

fraction p xs = let c = length xs in (length $ filter p xs) `intDiv` c

printFraction f = putStrLn $ show f

main :: IO ()
main = do
    n_temp <- getLine
    arr_temp <- getLine
    let arr = map read $ words arr_temp :: [Int]
    
    printFraction $ fraction (>0) arr
    printFraction $ fraction (<0) arr
    printFraction $ fraction (==0) arr
    

