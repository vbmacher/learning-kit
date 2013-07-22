import Control.Monad
import Control.Applicative
import Data.Char
import System.IO

import Data.Tree hiding (Tree )


newtype Parser a = Parser { parse :: String -> [(a, String)] }

instance Functor Parser where
  fmap f p = Parser $ \s -> [(f x,xs) | (x,xs) <- parse p s]

instance Applicative Parser where
  pure f = Parser $ \s -> [(f, s)]

  -- bez zvysku nesparsovaneho stringu by to ani neslo napisat
  -- (<*>) :: Parser (a->b) -> Parser a -> Parser b
  (<*>) p q = Parser $ \s -> [(f x, xs) | (f, ys) <- parse p s, (x, xs) <- parse q ys]

instance Alternative Parser where
  empty = Parser $ \s -> []

  (<|>) p q = Parser $ \s -> case parse p s of
    [] -> parse q s
    xs -> xs


psym :: (Char -> Bool) -> Parser Char
psym f = Parser $ \ds -> case ds of
  (x:xs) -> if f x then [(x,xs)] else []
  _      -> []

sym :: Char -> Parser Char
sym a = psym (== a)

string :: String -> Parser String
string = traverse sym

number :: Parser Int
number = read <$> some (psym isDigit)

-----------

data Token = TDig Int | TPlus | TMinus | TMul | TDiv | TLPar | TRPar deriving Show

ignore = sym ' '

tsym :: Char -> Token -> Parser Token
tsym a t = (many ignore) *> sym a *> pure t <* many ignore

tplus = tsym '+' TPlus
tminus = tsym '-' TMinus
tmul = tsym '*' TMul
tdiv = tsym '/' TDiv
tlpar = tsym '(' TLPar
trpar = tsym ')' TRPar
tnumber = TDig <$> number

token :: Parser Token
token = tplus <|> tminus <|> tmul <|> tdiv <|> tlpar <|> trpar <|> tnumber

tokens :: Parser [Token]
tokens = many token

----------

--    expr   -> factor { ("+" | "-") factor }
--    factor -> term   { ("*" | "/") term   }
--    term   -> "(" expr ")" | number
--    number -> digit { digit }
--    digit  -> "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"

data Arith =
  Ops Arith Arith
  | Add Arith
  | Sub Arith
  | Mul Arith
  | Div Arith
  | Num Int deriving Show

mkOps = foldl Ops

expr = mkOps <$> factor <*> many (addOrSub <*> factor)
  where addOrSub = (tplus *> pure Add) <|> (tminus *> pure Sub)

factor = mkOps <$> term <*> many (mulOrDiv <*> term)
  where mulOrDiv = (tmul *> pure Mul) <|> (tdiv *> pure Div)

term = (tlpar *> expr <* trpar) <|> (numFromDig <$> tnumber) 

numFromDig (TDig n) = Num n

-----------------------


toTree (Ops a b) = Node "ops" [toTree a, toTree b]
toTree (Add a) = Node "add" [toTree a]
toTree (Sub a) = Node "sub" [toTree a]
toTree (Mul a) = Node "mul" [toTree a]
toTree (Div a) = Node "div" [toTree a]
toTree (Num i) = Node (show i) []

-----------------------

eval :: Arith -> Int
eval (Ops a (Add b)) = (eval a) + (eval b)
eval (Ops a (Sub b)) = (eval a) - (eval b)
eval (Ops a (Mul b)) = (eval a) * (eval b)
eval (Ops a (Div b)) = (eval a) `div` (eval b)
eval (Num a) = a


runParser :: String -> IO Arith
runParser input = case (parse expr input) of
  [(ast, [])] -> return ast
  []          -> do fail $ "Invalid input '" ++ input ++ "'"
  [(ast, xs)] -> do fail $ "Invalid input '" ++ xs ++ "'"

cmdline :: IO ()
cmdline = do
  putStr "> "
  hFlush stdout
  input <- getLine
  ast <- runParser input

  let e = eval ast
  putStrLn $ "\n" ++ (show e)
  cmdline


main :: IO ()
main = do cmdline

