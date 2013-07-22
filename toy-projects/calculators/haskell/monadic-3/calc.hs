import Control.Monad
import Control.Applicative
import Data.Char
import System.IO

-- Parser is a structure which "contains" a parsing function
-- remember: function is a value, so parser holds the value - parsing function 
-- parsing function returns AST - possibly multiple ASTs
-- which direction or how it will be parsing?, well, monad will tell.
newtype Parser a = Parser { parse :: String -> [(a, String)]  }


-- remember: functor is the ability to apply a function inside a structure.
-- what is parser? a structure where is a function.
-- types tell us that functor in this case means to change already parsed AST to another AST 
-- but we don't have first AST until we parse it from original string first
-- so fmap is - create a parser which uses the input string for parsing AST using given parser,
-- and then applying given function to its result.
instance Functor Parser where

  -- fmap :: (a -> b) -> Parser a -> Parser b
  fmap f p = Parser $ \s -> [(f x, xs) | (x,xs) <- parse p s] 


-- applicative is weird. it's similar to functor, but the function (a->b)
-- is INSIDE the same structure as the input. We know already that inside the
-- Parser there is a function already, String -> [(a,String)]. But what Applicative
-- requires is "a" being a function, resulting in String -> [((a->b), String)], as
-- for first argument. The second argument is the "normal" parser.
--
-- So the feature Applicative brings is that it specifies the way of converting
-- one type of AST to another, but in a way that the "conversion function" will
-- be obtained only after parsing the input by the first parser. The result of the
-- applicative is in fact the second parser, modified in a way that its resulting AST
-- is converted into another one, by applying the conversion function.
--
-- This "applicative mapping" is a binary function <*>. It is often described as "sequencing
-- and combining results".
--
-- There exists also another one in this category, called "pure", which
-- take normal function (a->b) and puts it into the structure. The whole applicative
-- has basically this relation to normal functor: fmap f x = pure f <*> x 
--
-- Maybe the most important thing what applicative brings is is how the
-- function is called. If the structure can contain multiple items, the function
-- must be applied to each item. In case of parser, after parsing the input it
-- returns, by definition, one or more ASTs. We know that the first parser returns
-- a list of "conversion functions" after parsing its input. But the result is
-- [((a->b), String)] - so list of conversion functions, together with the "rest"
-- of the input. Each conversion function might consume various size of the original
-- input, therefore the "rests" of input might be different.
--
-- The result of the whole operation is a new parser, which applies returned
-- functions - all of them - to all ASTs returned by the second parser, when applying
-- the unparsed inputs to it, taken from the conversion functions. This "sequencing"
-- of unparsed inputs is one part of Applicative idea, and combining results in terms
-- of having a cartesian product by applying all conversion functions from the first
-- parser to all ASTs returned by the second parser. 
--
instance Applicative Parser where

  -- pure :: (a->b) -> Parser (a->b)
  pure a = Parser $ \s -> [(a,s)]

  -- (<*>) :: Parser (a->b) -> Parser a -> Parser b
  (<*>) p q = Parser $ \s -> [(f x, xs) | (f,ys) <- parse p s, (x, xs) <- parse q ys]


-- Applicatives were introduced later than Monads to Haskell, having some similar
-- properties, but simplified and accomodated, in that time mainly for usefulness in
-- parsing.
-- Monads is just another structure which contains some context we want to work with,
-- but it is not accessible directly. Main operation is sequencing operations, by
-- applying of some "combination" function. This function has in fact the same meaning
-- as our "conversion function", but it is required that the function will return
-- Monad - the structure with the value - and not just pure value.
--
-- So, in fact, what the main Monad operation does is that it takes a monad, unboxes
-- the value in the monad. Then, applies given "combination function" to this value,
-- and it is expected that the function will pack / box the resulting value again
-- into the monad, which is the result.
--
-- At first, the first parser is applied to the input, resulting in some ASTs
-- with unparsed "rests" of the original input. We then apply the combination function,
-- which takes the AST and based on that it returns new parser, which results in another
-- AST. So basically the "conversion" goes in another place - not by direct converting
-- one type of AST to another, but based on parsed AST specifying what should be parsed
-- next.
--
-- Again, this happens as a cartesian product - for all ASTs returned by the first
-- parser is applied the combination function, and its result is parsed using the unparsed
-- "rests" of the input, returned by the first parser.
--
-- to all the N values, we obtain N new parsers. 
--
-- Monad is a "programmable semicolons”
-- https://medium.com/@dailydrip/monads-haskell-and-elm-a-socratic-dialogue-c0a1b4f6acb2#.evgzyu6l5
instance Monad Parser where

  -- return :: a -> Parser a
  return = pure

  -- (>>=) :: Parser a -> (a -> Parser b) -> Parser b
  (>>=) p f = Parser $ \s -> [ (y,ys) | (x, xs) <- parse p s, (y,ys) <- parse (f x) xs]


-- Alternative is a Monoid which have associative operation (<|>), the "alternative",
-- and neutral value, called "empty".
--
-- The alternative is in fact about a possibility to try another parser when the first one
-- does not get any results. We can chain the operator, since it is associative, it means
-- the following is the same:
--
--   (p <|> q) <|> r = p <|> (q <|> r)
--
-- but we cannot change the order.  
instance Alternative Parser where

  -- (<|>) :: Parser a -> Parser a -> Parser a
  (<|>) p q = Parser $ \s -> case parse p s of
    [] -> parse q s
    xs -> xs

  -- empty :: Parser a
  empty = Parser $ \s -> []


-- Ok, so let's begin to create some real parser combinators.
-- What should we begin with? Well, the most easiest thing is to parse a plain char.
-- So let "char" be our char parser combinator.
char :: Parser Char
char = Parser $ \s -> case s of
  (x:xs) -> [(x, xs)]
  _      -> []


-- Now, when we will do parsing specific characters, e.g. numbers, we should
-- be able to check if the parsed character satisfies some condition - e.g. that
-- it is a digit, etc. 
satisfy :: (Char -> Bool) -> Parser Char
satisfy f = do
  c <- char
  if (f c)
  then return c
  else empty

-- To simplify parsing specific characters, let us write
-- a parser which parses just those.
get :: Char -> Parser Char
get c = satisfy (==c)


-- Now here we are. Numbers. But how?
-- We might just say - number is some characters such that they
-- are digits.
number :: Parser Int
number = do
  digits <- some $ satisfy isDigit
  return $ read digits 



data Expr = Add Expr Expr
          | Sub Expr Expr
          | Mul Expr Expr
          | Div Expr Expr
          | N Int


-- chainl parses some grammar part, which has repeating
-- elements, specifically this:
--
--   F { X }
--
-- You might notice there two parts:
--
-- - F      : this is required part
-- - { X }  : this is optional part; means repeating X 0 and more times
--
-- In our terms, X can be any already existing parser combinator.
--
-- Why we need F? This is important question.
-- The answer lies in answering another question - how would you
-- implement the parsing of "repeating" part? This leads us even deeper,
-- to answering already answered question - what is parsing?
-- It is a translation of input to AST - a data structure.
-- So when we would like to parse repeating part, we need put this
-- repeats into the data structure in some recursive manner. Yes,
-- recursive, because we will repeat the *same* thing - so data structure
-- needs to be also "the same", nesed inside itself.
--
-- But we still want to be general - we do not really care what this
-- data structure will be - only thing what matters is that it has to be
-- binary. Why? Well, this is the answer for the very first question -
-- why we need F?
--
-- To answer both questions, let's imagine what possibilities we have
-- when the grammar is applied:
--
-- 1. Repeating part is not present; then we should return parsed F.
-- 2. Repeating part is present; then we need to combine F with all
--    the repeating parts, using given data structure, which we need
--    to treat as the binary function.
--
-- So, then, what we need:
--
-- 1. already parsed F
-- 2. the binary function resulting in parsed data structure. It will be used
--    only if the repeating part is present. 
-- 3. parser of the optional repeating part
--
-- For example, the following can happpen:
--
--      F1                      => F1
--     (F1 op F2)               => Add F1 F2
--    ((F1 op F2) op F3)        => Add (Add F1 F2) F3
--   (((F1 op F2) op F3) op F4) => Add (Add (Add F1 F2) F3) F4
--
chainl :: a -> (a -> a -> a) -> Parser a -> Parser a
chainl first op optional = continue first
  where continue a = (do
          b <- optional
          continue (op a b)) <|> return a
    

-- And now we can implement the grammar itself.


-- Let's start with the repeating parts
op c = do
  get c
  n <- factor
  return n 


-- expr -> factor { (+|-) factor }
expr :: Parser Expr
expr = do
  f <- factor
  chainl f Add (op '+') <|> chainl f Sub (op '-')

-- factor -> term { (*|/) term }
factor :: Parser Expr
factor = do
  t <- term
  chainl t Mul (op '*') <|> chainl t Div (op '/')

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


