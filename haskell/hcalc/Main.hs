import System.Environment
import System.Exit
import Data.Char

-- parser

data Expr = PLUS Expr Expr 
  | MINUS Expr Expr
  | TIMES Expr Expr
  | DIV Expr Expr
  | Lit Integer


newtype Parser a = Parser { parse :: String -> [(a,String)] }

runParser m s =
  case parse m s of
    [(x,[])] -> x
    [(_,xs)] -> die
    _        -> die


item :: Parser Char
item = Parser $ \s ->
  case s of
    []     -> []
    (c:cs) -> [(c, cs)]





-- commands

help = putStrLn "hcalc\nUsage: hcalc < file" >> exit


runHCalc xs = xs 



exit = exitWith ExitSuccess
die = exitWith (ExitFailure 1)


-- main

parseArgs ("--help":_) = help
parseArgs ("-h":_) = help
parseArgs _ = interact runHCalc

main = getArgs >>= parseArgs

