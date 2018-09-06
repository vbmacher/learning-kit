import System.Environment
import System.Exit
import Data.Char
import Control.Applicative

-- Types

newtype Parser a = Parser { parse :: String -> [(a,String)] }

instance Functor Parser where
  fmap f (Parser p) = Parser $ \s -> [(f a, xs) | (a,xs) <- p s] 

instance Applicative Parser where
  pure f = Parser $ \s -> [(f,s)]
  (Parser p) <*> (Parser q) = Parser $ \s -> [(f a, xs) | (f, ys) <- p s,  (a,xs) <- q ys]

instance Monad Parser where
  return a = Parser $ \s -> [(a,s)]
  (>>=) p f = Parser $ \s -> concatMap (\(a,s') -> parse (f a) s') $ parse p s

instance Alternative Parser where
  empty = Parser $ \s -> []
  (<|>) a b = Parser $ \s ->
    case parse a s of
      [] -> parse b s
      res -> res

-- Common functions

item :: Parser Char
item = Parser $ \s ->
  case s of
    []     -> []
    (c:cs) -> [(c, cs)]


satisfy :: (Char -> Bool) -> Parser Char 
satisfy p = item >>= \s ->
  if p s
  then return s
  else Parser $ \s -> []

oneOf p = satisfy (flip elem p)

chainl1 :: Parser a -> Parser (a -> a -> a) -> Parser a
p `chainl1` op = do {a <- p; rest a}
  where rest a = (do f <- op
                     b <- p
                     rest (f a b))
                 <|> return a


runParser :: Parser a -> String -> a
runParser p s =
  case parse p s of
    [(x,[])] -> x
    [(_,xs)] -> error $ "Unrecognized characters left: " ++ xs
    _        -> error $ "String is completely unknown to me"

-- Expression

data Expr a =
  Add (Expr a) (Expr a) |
  Mul (Expr a) (Expr a) |
  Sub (Expr a) (Expr a) |
  Div (Expr a) (Expr a) |
  Lit a deriving Show


digit :: Parser Char
digit = satisfy isDigit

char c = satisfy (c ==)

string [] = return []
string (c:cs) = do
  char c
  string cs
  return (c:cs)


number :: Parser Int
number = do
  minus <- string "-" <|> return []
  s <- many digit
  return $ read (minus ++ s)

spaces :: Parser String
spaces = many $ oneOf " \t\n\r"

token :: Parser a -> Parser a
token p = do { spaces; a <- p; spaces ; return a}

reserved c = token (string c)


-- Grammar
--
-- expr   -> factor { addOp factor }
-- factor -> term { mulOp term }
-- term   -> "(" expr ")" | number
-- addOp  -> "+" | "-"
-- mulOp  -> "*" | "/"

expr = factor `chainl1` addOp

factor = term `chainl1` mulOp

term = par <|> lit


infixOp :: String -> (a -> a -> a) -> Parser (a -> a -> a)
infixOp x f = reserved x >> return f

addOp :: Parser (Expr a -> Expr a -> Expr a)
addOp = (infixOp "+" Add) <|> (infixOp "-" Sub)

mulOp :: Parser (Expr a -> Expr a -> Expr a)
mulOp = (infixOp "*" Mul) <|> (infixOp "/" Div)


lit = do
  spaces
  a <- number
  spaces
  return $ Lit a

par = do
  reserved "("
  e <- expr
  reserved ")"
  return e


eval e = case e of
  Lit n   -> n
  Add a b -> eval a + eval b
  Mul a b -> eval a * eval b
  Sub a b -> eval a - eval b
  Div a b -> eval a `div` eval b


-- commands

help = putStrLn "hcalc\nUsage: hcalc < file" >> exit

runHCalc = show . eval . runParser expr


exit = exitWith ExitSuccess
die = exitWith (ExitFailure 1)


-- main

parseArgs ("--help":_) = help
parseArgs ("-h":_) = help
parseArgs _ = interact runHCalc

main = getArgs >>= parseArgs

