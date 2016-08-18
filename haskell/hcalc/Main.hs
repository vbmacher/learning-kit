import System.Environment
import System.Exit
import Data.Char
import Control.Applicative


newtype Parser a = Parser { parse :: String -> [(a,String)] }

instance Functor Parser where
 
  -- fmap :: (a -> b) -> Parser a -> Parser b 
  fmap f (Parser p) = Parser $ \s -> [(f a, xs) | (a,xs) <- p s] 

instance Applicative Parser where
  pure f = Parser $ \s -> [(f,s)]

  (Parser p) <*> (Parser q) = Parser $ \s -> [(f a, xs) | (f, ys) <- p s,  (a,xs) <- q s]


runParser :: Parser (IO a) -> String -> IO a
runParser p s =
  case parse p s of
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

runHCalc :: String -> String
runHCalc xs = xs



exit = exitWith ExitSuccess
die = exitWith (ExitFailure 1)


-- main

parseArgs ("--help":_) = help
parseArgs ("-h":_) = help
parseArgs _ = interact runHCalc

main = getArgs >>= parseArgs

