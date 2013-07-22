import Control.Applicative
import Control.Monad
import System.IO
import Data.Function

intDiv = (/) `on` fromIntegral

main :: IO ()
main = do
    n_temp <- getLine
    let n = read n_temp :: Int
    arr_temp <- getLine
    let arr = map read $ words arr_temp :: [Int]
    let count = length arr
    putStrLn $ show $ (length $ filter (>0) arr) `intDiv` count
    putStrLn $ show $ (length $ filter (<0) arr) `intDiv` count
    putStrLn $ show $ (length $ filter (==0) arr) `intDiv` count
    

