import Data.Char
import System.IO

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

type Zipper a = ([a], [a])

type IP     = Zipper Instr
type Memory = Zipper Int
type Stack  = [IP]
type Input  = String
type State  = (IP, Stack, Memory, Input, Int)

killText = "\nPROCESS TIME OUT. KILLED!!!"

initZipper xs = ([], xs)
initial ps ms inp = (initZipper ps, [], initZipper ms, inp, 100000)


save :: Zipper a -> (a -> a) -> Zipper a
save (ps, x:ts) f = (ps, (f x):ts)

roll :: Int -> Zipper a -> Zipper a
roll 0 xs = xs
roll n xs@(ps, ts)
  | n < 0     = roll (n+1) (tail ps,  (head ps):ts)
  | n > 0     = roll (n-1) ((head ts):ps, tail ts)
  | otherwise = xs


next :: Zipper a -> Zipper a
next xs = roll 1 xs


printR :: Int -> Int -> IO ()
printR 0 x = return ()
printR c x = putChar (chr x) >> hFlush stdout >> printR (c-1) x


loadR :: Int -> Input -> (Int, Input)
loadR 1 (x:xs) = (ord x, xs)
loadR c (x:xs) = loadR (c - 1) xs


eval :: State -> IO State
eval (ip@(_,(Instr c n):is), xs, mem@(_,m:ms), inp, ops) = do
  let cnt = howMany n ops
  case c of
    '<' -> return (next ip, xs, roll (-cnt) mem, inp, ops - cnt)
    '>' -> return (next ip, xs, roll cnt mem, inp, ops - cnt)
    '-' -> return (next ip, xs, save mem (\b-> (b - cnt) `mod` 256), inp, ops - cnt)
    '+' -> return (next ip, xs, save mem (\b-> (b + cnt) `mod` 256), inp, ops - cnt)
    '.' -> do
             printR cnt m
             return (next ip, xs, mem, inp, ops - cnt)
    ',' -> do
             let (char, newInput) = loadR cnt inp
             return (next ip, xs, save mem (const char), newInput, ops - cnt)
    '[' -> if m == 0
           then return (roll (n + 1) ip, xs, mem, inp, ops - 2)
           else return (next ip, ip:xs, mem, inp, ops - 1)
    ']' -> if m /= 0
           then return (head xs, tail xs, mem, inp, ops - 1)
           else return (next ip, tail xs, mem, inp, ops - 1)

howMany n ops = if ops - n <= 0 then ops else n


runProgram :: State -> IO ()
runProgram ((_,[]), _, _, _, _) = return ()
runProgram (_, _, _, _, 0) = do
  putStrLn killText
runProgram state = do 
  newState <- eval state
  runProgram newState


main = do
  line <- getLine
  rawinput <- getLine
  let input = init rawinput
  rawtext <- getContents
  program <- return $ parse rawtext
  ps <- return $ findLoops program
  runProgram $ initial ps (take 6000 (repeat 0)) input
