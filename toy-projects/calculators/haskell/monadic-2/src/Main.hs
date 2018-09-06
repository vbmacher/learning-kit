module Main where

import System.IO

import Parser
import ArithParser

-- And now the las thing - evaluation of the parsed data
eval :: Expr -> Int
eval (Add a b) = (eval a) + (eval b)
eval (Sub a b) = (eval a) - (eval b)
eval (Mul a b) = (eval a) * (eval b)
eval (Div a b) = (eval a) `div` (eval b)
eval (N n)     = n


-- This is interesting function. It does two things:
--   1. Parse expression
--   2. Repack it into IO monad
runParser :: Parser Expr -> String -> IO Expr
runParser p input = case parse p input of
  [(e,[])] -> return e
  []       -> do
                putStrLn $ "Did not understand '" ++ input ++ "'"
                return (N 0)
  [(e,xs)] -> do
                putStrLn $ "Partially understood. Unparsed: '" ++ xs ++ "'"
                return e


cmdline :: IO ()
cmdline = do
  putStr "> "
  hFlush stdout
  input <- getLine
  parsed <- runParser expr input

  let e = eval parsed
  putStrLn $ "\n" ++ (show e)
  cmdline

main :: IO ()
main = do
  putStrLn "Hello, hello, eval man!"
  cmdline

