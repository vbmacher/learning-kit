-- Brainfuck interpreter

import Data.Char
import System.IO
-- import Data.Vector.Unboxed as VU

data Instr = Instr Char Int deriving Show
type Program = [Instr]


-- parser

some :: Char -> Int -> String -> (Instr, String)
some c n [] = (Instr c n, [])
some c n ys@(x:xs)
  | x == c    = some c (n+1) xs
  | otherwise = (Instr c n, ys)



parse :: String -> Program
parse [] = []
parse ('[':xs) = (Instr '[' 0):(parse xs)
parse (']':xs) = (Instr ']' 0):(parse xs)
parse ys@(x:xs) = case x of
  '<' -> rep '<'
  '>' -> rep '>'
  '+' -> rep '+'
  '-' -> rep '-'
  '.' -> rep '.'
  ',' -> rep ','
  _   -> parse xs

  where rep c = let (i,cs) = some c 0 ys in i:(parse cs)



findLoops :: Program -> Program
findLoops xs = snd $ foldr f ([],[]) xs
  where f (Instr '[' 0) ((c:d:cs), p) = ((d+c+1):cs, (Instr '[' c):p)
        f (Instr '[' 0) ((  c:cs), p) = (        cs, (Instr '[' c):p)
        f (Instr ']' 0) (      cs, p) = (      1:cs, (Instr ']' 0):p)
        f             i (      [], p) = (        [],             i:p)
        f             i (    c:cs, p) = (  (c+1):cs,             i:p)



-- simulator

type T a = ([a], [a])

type IP     = T Instr
type Memory = T Int
type Stack  = [IP]
type State  = (IP, Stack, Memory)


initT xs = ([], xs)
initial ps ms = (initT ps, [], initT ms)


save :: T a -> (a -> a) -> T a
save (ps, x:ts) f = (ps, (f x):ts)

roll :: Int -> T a -> T a
roll 0 xs = xs
roll n xs@(ps, ts)
  | n < 0     = roll (n+1) (tail ps,  (head ps):ts)
  | n > 0     = roll (n-1) ((head ts):ps, tail ts)
  | otherwise = xs


next :: T a -> T a
next xs = roll 1 xs


printR :: Int -> Int -> IO ()
printR 0 x = return ()
printR c x = putChar (chr x) >> hFlush stdout >> printR (c-1) x


loadR :: Int -> Int -> IO Int
loadR 0 x = return x
loadR c _ = getChar >>= \char -> loadR (c - 1) (ord char) >>= return


eval :: State -> IO State
eval (ip@(_,(Instr c n):is), xs, mem@(_,m:ms)) = do
--  putStrLn $ (take (2 * length xs) (repeat ' ')) ++ [c] ++ "(" ++ show n ++ "), mem[p]=" ++ show m ++ ", loops=" ++ show (length xs)


  case c of
    '<' -> return (next ip, xs, roll (-n) mem)
    '>' -> return (next ip, xs, roll n mem)
    '-' -> return (next ip, xs, save mem (\b-> (b - n) `mod` 256))
    '+' -> return (next ip, xs, save mem (\b-> (b + n) `mod` 256))
    '.' -> do
             printR n m
             return (next ip, xs, mem)
    ',' -> do
             char <- loadR n 0
             return (next ip, xs, save mem (const char))
    '[' -> if m == 0
           then return (roll (n + 1) ip, xs, mem)
           else return (next ip, ip:xs, mem)
    ']' -> if m /= 0
           then return (head xs, tail xs, mem)
           else return (next ip, tail xs, mem)


runProgram :: State -> IO ()
runProgram ((_,[]), _, _) = return ()
runProgram state = do 
  newState <- eval state
  runProgram newState


main = do
  rawtext <- getContents
  program <- return $ parse rawtext
  ps <- return $ findLoops program
  runProgram $ initial ps (take 30000 (repeat 0))
