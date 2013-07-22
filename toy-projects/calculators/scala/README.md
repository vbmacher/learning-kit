# scalC

Welcome to scalC! This project is the result of my try to create simple monadic calculator in Scala.
It is somehow based on my [hcalc2](https://github.com/vbmacher/learning-kit/tree/master/toy-projects/hcalc2)
project, which is a simple monadic calculator in Haskell.

The calculator is really simple, it accepts (and evaluates) the following grammar:

    start  -> factor { '+'|'-' factor }
    factor -> term   { '*'|'/' term   }
    term   -> ['-'] N | '(' start ')'

As you can see, the calculator supports left-associativity of all operators, and also operators '*' and '/' have
higher priority than '+' and '-'. So this grammar allows to compute correct arithmetic expressions. There are not
allowed spaces between symbols, and numbers can be only (possibly negative) integers.

## Why...

At first I wanted to know how the monadic parser code would look like in Scala. I did not use any library,
I have created my own parser combinators which are used in the grammar implementation.

Also, I am quite new to Scala, so my implementation does not have to be "the nicest" or "the most efficient".
It was part of my "learning process".

## Parser: Haskell vs. Scala

At first, it is needed to say, Haskell is not object-oriented language. It means that data structures in Haskell
just hold the data. All operations which work with such data structure are defined elsewhere.
In addition, those operations always must take the data structure as a parameter, and returning new data structure.

Scala can do OOP, so the difference is that we can define operations "inside" the data structure. Then, for some
n-ary operation, where n > 1, we can assume that the first parameter is the "object" itself on which the
operation is defined.

This difference is just a convenience, it just affects from where the first parameter of e.g. monadic "bind"/"flatMap"
operation is taken.
 
For example, in Haskell, we have:

    newtype Parser a = Parser { parse :: String -> [(a, String)]  }

    instance Monad Parser where
    
      -- return :: a -> Parser a
      return = pure
    
      -- (>>=) :: Parser a -> (a -> Parser b) -> Parser b
      (>>=) p f = Parser $ \s -> [ (y,ys) | (x, xs) <- parse p s, (y,ys) <- parse (f x) xs]

And in Scala we have:
    
    trait Parser[A] {

      def parse(s: String): List[(A, String)]
    
      // ">>=" in Scala 
      def flatMap[B](f: A => Parser[B]): Parser[B] = s => for {
        (a, rest) <- parse(s)
        bs <- f(a).parse(rest)
      } yield bs

    }

These implementations do exactly same thing. In the Haskell case, the "bind" takes a parser and a function. In Scala
case, it takes just the function, because parser is assumed to be the object itself. The purpose of `bind`/`flatMap`
is that we can "sequence" parsers easily - the result of the first one will be parameter to the given function, which
returns another parser. Since the parser returns list of results, we will have list of parsers which we must "flatten"
into single parser.

## Grammar definition: Haskell vs. Scala

Using the parser in the grammar definition is the reason why monadic parsers are favourite.
Do you remember the grammar from the top? The following way is partial Haskell "definition":

    -- Grammar
    --
    -- expr   -> factor { addOp factor }
    -- factor -> term { mulOp term }
    -- term   -> "(" expr ")" | number
    -- addOp  -> "+" | "-"
    -- mulOp  -> "*" | "/"

    data Expr a = Add (Expr a) (Expr a) | Sub (Expr a) (Expr a)
      | Mul (Expr a) (Expr a) | Div (Expr a) (Expr a)
      | Lit a deriving Show

    start = factor `chainl1` addOp
    
    infixOp :: Char -> (a -> a -> a) -> Parser (a -> a -> a)
    infixOp x f = do
      char x
      return f
    
    addOp :: Parser (Expr a -> Expr a -> Expr a)
    addOp = (infixOp '+' Add) <|> (infixOp '-' Sub)
    
And in Scala:

    trait Expr {
      // def eval: Int
    }
    
    case class Add(b: Expr)(a: Expr) extends Expr 
    case class Sub(b: Expr)(a: Expr) extends Expr
    case class Mul(b: Expr)(a: Expr) extends Expr
    case class Div(b: Expr)(a: Expr) extends Expr
    case class Lit(n: Int) extends Expr
    
    trait Grammar {
      import Parser._
    
      // start -> factor { '+'|'-' factor }
      def start(): Parser[Expr] = {
        factor().foldLeft(plusFactor().or(minusFactor()))
      }
    
      def plusFactor(): Parser[Expr => Expr] = for {
        _ <- symbol('+')
        e <- factor()
      } yield Add(e)
    
      def minusFactor(): Parser[Expr => Expr] = for {
        _ <- symbol('-')
        e <- factor()
      } yield Sub(e)
    
    }

I have omitted `chainl1`/`foldLeft` implementations here, which does the left-associative repetition of the
grammar subrules - it's the implementation of the EBNF's `{ }`. 

The "niceness" here is that every function is a full-featured parser which can be used
separately, and combined into "bigger" parsers. The "combination" is realized by monadic
`>>=`/`flatMap` operation. The operations don't have to be obvious for beginners - they are hidden under the syntactic
sugars of `do`/`for`.

