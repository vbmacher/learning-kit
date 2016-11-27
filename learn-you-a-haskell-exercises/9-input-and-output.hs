{-
 - Lets implement the UNIX echo command
 - The program arguments are simply printed to the standard output.
 - If the first argument is -n, this argument is not printed, and no trailing newline is printed
 -}
import System.Environment
import System.Random

main = do
  args <- getArgs
  let first = head args
  case first of
    "-n" -> mapM putStr $ tail args
    _    -> mapM putStrLn args
 
  

{- Write a lottery number picker
 - This function should take a StdGen instance, and produce a list of six unique numbers between 1 and 49, in numerical order
 -}
lottery :: StdGen -> [Int]
lottery gen = take 6 $ randomRs (1,49) gen
