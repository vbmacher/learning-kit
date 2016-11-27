module Lab5 where

import Control.Monad

data Concurrent a = Concurrent ((a -> Action) -> Action)

data Action 
    = Atom (IO Action)
    | Fork Action Action
    | Stop

instance Show Action where
    show (Atom x) = "atom"
    show (Fork x y) = "fork " ++ show x ++ " " ++ show y
    show Stop = "stop"

-- ===================================
-- Ex. 0
-- ===================================

-- To express the connection between an expression of type Concurrent
--  a and one of type Action, we define a function 
--  action :: Concurrent a -> Action that transforms a 
--  ((a -> Action) -> Action) into an Action that uses Stop :: Action
--  to create the continuation to the Concurrent a passed as the first
--  argument to action.

action :: Concurrent a -> Action
action (Concurrent f) = f (\x -> Stop)
  
-- ===================================
-- Ex. 1
-- ===================================

-- To make the constructors of the data type Action easily accessible,
-- we can define helper functions that hide the boilerplate required to use them.
--
-- The first helper function that we will define is the function
-- stop :: Concurrent a, which discards any continuation, thus ending a computation.
--
-- Thus we need to return a function of type ((a -> Action) -> Action) wrapped
-- in the Concurrent data type. This function takes a continuation, which gets
-- discarded, and then it returns a Stop action.

stop :: Concurrent a
stop = Concurrent (\_ -> Stop)


-- ===================================
-- Ex. 2
-- ===================================

-- Now we can define the helper function atom :: IO a -> Concurrent a, which
-- turns an arbitrary computation in the IO Monad into an atomic action
-- represented using the Atom constructor. 
--
-- The easiest way to implement this function is to first implement
-- atom :: IO a -> ((a -> Action) -> Action) by taking an value
-- x :: IO a and returning a value of type ((a -> Action) -> Action) which
-- looks like \c :: (a -> Action) -> ... value of type Action ...
--
-- You already know, from the previous homework and labs, how to combine a
-- value of type IO a and a function of type a -> IO b into a value of type
-- IO b using (>>=), in this case b is instantiated to Action. You also know
-- how to convert a value of type Action into a value of type IO Action using
-- return. Finally, the obvious choice to turn a value of type IO Action into
-- an Action is by using the Atom constructor. With all these pieces on the table,
-- there is really only one way to wire them together to implement your function.
-- Now the only step that is left is to wrap and unwrap the Concurrent data type,
-- to implement atom :: IO a -> Concurrent a.

atom :: IO a -> Concurrent a
atom c = Concurrent (\x -> Atom ((c >>= \d -> return (x d))))


-- ===================================
-- Ex. 3
-- ===================================

-- In order to access Fork, we need to define two operations. The first,
-- called fork :: Concurrent a -> Concurrent (), forks its argument by turning
-- it into an action and continues by passing () as the input to the continuation:

fork :: Concurrent a -> Concurrent ()
fork c = Concurrent $ \b -> Fork (action c) (b ())

-- The second, par :: Concurrent a -> Concurrent a -> Concurrent a, combines two
-- computations into one by forking them both and passing the given continuation
-- to both parts.
-- ===> something like parallel computation?

par :: Concurrent a -> Concurrent a -> Concurrent a
par (Concurrent f) (Concurrent g) = Concurrent (\a -> Fork (f a) (g a)) 


-- ===================================
-- Ex. 4
-- ===================================

instance Monad Concurrent where

-- Don't panic! Let the types guide you and everything will be alright.
-- There is really just one way to wire up all the pieces you have on the
-- table to create a result of the required type. Don't try to understand
-- what the code does operationally, trust the types.

-- The easiest way to do this is by first developing on a piece of scratch
-- paper (*), a function
--
-- (>>=) :: ((a -> Action) -> Action) 
--    -> (a -> ((b -> Action) -> Action))
--    -> ((b -> Action) -> Action)
--
-- ignoring the Concurrent wrapper. Now you can let the types lead you to
-- the only reasonable implementation. Once you've found this, you can add
-- the boilerplate pattern matching and applications of Concurrent that
-- Haskell unfortunately requires. Your implementation without Concurrent
-- will look as follows:

-- Remember when you return a value of a function type, such as
-- ((b -> Action) -> Action), the value you create looks like \c -> ...
-- expression of type Action ...

-- Similarly, when you need to pass a value of a function type, for example
-- a -> ((b -> Action) -> Action), the value you pass can be an expression
-- of the form:

-- \a -> ... expression of type ((b -> Action) -> Action) ... 

-- In the end, the solution only needs two lambda expressions and a couple
-- of function applications.

-- While doing all this, you don't need to look at the structure of Action
-- at all. In fact, this would work for any type instead of Action.

-- (*) You can use Hugs as your scratch paper by using a different name than
-- (>>=), for example bind. This is what we mean by "Think like a Fundamentalist".
-- Implementing (>>=) with all the nasty wrapping is "Code like a Hacker".
-- Thinking like a fundamentalist is like Dolby noise reduction for the Hacker.

    -- Concurrent ((a -> Action) -> Action)
    --  -> (a -> (Concurrent ((b -> Action) -> Action)))
    --  -> Concurrent ((b -> Action) -> Action)
    (Concurrent f) >>= g = Concurrent $ \b -> f $ \a -> runcont (g a) b
       where runcont (Concurrent m) n = m n

    return x = Concurrent (\c -> c x)


-- ===================================
-- Ex. 5
-- ===================================

roundRobin :: [Action] -> IO ()
roundRobin [] = return ()
roundRobin ((Atom a):xs) = do b <- a
                              (roundRobin (xs ++ [b]))
roundRobin ((Fork a b):xs) = roundRobin (xs ++ [a,b])
roundRobin (Stop:xs) = roundRobin xs

-- ===================================
-- Tests
-- ===================================

ex0 :: Concurrent ()
ex0 = par (loop (genRandom 1337)) (loop (genRandom 2600) >> atom (putStrLn ""))

ex1 :: Concurrent ()
ex1 = do atom (putStr "Haskell")
         fork (loop $ genRandom 7331) 
         loop $ genRandom 42
         atom (putStrLn "")


-- ===================================
-- Helper Functions
-- ===================================

run :: Concurrent a -> IO ()
run x = roundRobin [action x]

genRandom :: Int -> [Int]
genRandom 1337 = [1, 96, 36, 11, 42, 47, 9, 1, 62, 73]
genRandom 7331 = [17, 73, 92, 36, 22, 72, 19, 35, 6, 74]
genRandom 2600 = [83, 98, 35, 84, 44, 61, 54, 35, 83, 9]
genRandom 42   = [71, 71, 17, 14, 16, 91, 18, 71, 58, 75]

loop :: [Int] -> Concurrent ()
loop xs = mapM_ (atom . putStr . show) xs

