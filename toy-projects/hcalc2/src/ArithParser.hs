module ArithParser where

import Control.Applicative
import Data.Char

import Parser
import EBNF

data Expr = Add Expr Expr
          | Sub Expr Expr
          | Mul Expr Expr
          | Div Expr Expr
          | N Int

-- Now here we are. Numbers. But how?
-- We might just say - number is some characters such that they
-- are digits.
number :: Parser Int
number = do
  many $ satisfy isSpace
  digits <- some $ satisfy isDigit
  return $ read digits

-- Let's start with the repeating parts
ops :: [(Char, Expr -> Expr -> Expr, Parser Expr)] -> Parser (Expr -> Expr)
ops [] = empty
ops ((operator, exprCons, secondArgument):xs) = (do
  get operator
  b <- secondArgument
  return $ flip exprCons b) <|> ops xs

-- expr -> factor { (+|-) factor }
expr :: Parser Expr
expr = do
  f <- factor
  chainl f (ops [('+',Add,factor), ('-',Sub,factor)])

-- factor -> term { (*|/) term }
factor :: Parser Expr
factor = do
  t <- term
  chainl t (ops [('*',Mul,term), ('/',Div, term)])

-- term -> N | "(" expr ")"
term :: Parser Expr
term =   (do
            n <- number
            return (N n))
     <|> (do
            get '('
            e <- expr
            get ')'
            return e)
