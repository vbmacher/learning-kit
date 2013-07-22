
putStr' [] = return ()
putStr' (x:xs) = (putChar x) >> putStr' xs

------ EX 2 ---------------

-- correct
-- putStrLn' [] = putChar '\n'
-- putStrLn' xs = putStr' xs >> putStrLn' ""

-- correct
putStrLn' [] = putChar '\n'
putStrLn' xs = putStr' xs >> putChar '\n'

-- correct
-- putStrLn' [] = putChar '\n'
-- putStrLn' xs = putStr' xs >>= \ x -> putChar '\n'

-- correct
-- putStrLn' [] = putChar '\n'
-- putStrLn' xs = putStr' xs >> putStr' "\n"

---- EX 3 ------------------

getLine' = get []

get:: String -> IO String
get xs
  = do x <- getChar
       case x of
            '\n' -> return xs
            _ -> get (xs ++ [x])


----- EX 4 -------------

interact' f
  = do input <- getLine'
       putStrLn' (f input)

----- EX 5 ----------

-- correct
-- sequence_' [] = return ()
-- sequence_' (m : ms) = m >> sequence_' ms

-- correct
-- sequence_' [] = return ()
-- sequence_' (m:ms) = (foldl (>>) m ms) >> return ()

-- incorrect
-- sequence_' ms = foldl (>>) (return ()) ms

-- correct
-- sequence_' [] = return ()
-- sequence_' (m:ms) = m >>= \ _ -> sequence_' ms

sequence_' ms = foldr (>>) (return ()) ms

----- EX 6 ----------------

-- correct
-- sequence' [] = return []
-- sequence' (m:ms)
--   = m >>=
--       \ a ->
--         do as <- sequence' ms
--            return (a : as)

-- incorrect
-- sequence' ms = foldr func (return ()) ms
--   where
--          func :: (Monad m) => m a -> m [a] -> m [a]
--          func m acc
--            = do x <- m
--                 xs <- acc
--                 return (x : xs)

-- incorrect
-- sequence' ms = foldr func (return []) ms
--   where
--         func :: (Monad m) => m a -> m [a] -> m [a]
--         func m acc = m : acc

-- incorrect
-- sequence' [] = return []
-- sequence' (m:ms) = return (a : as)
--   where
--     a <- m
--     as <- sequence' ms

-- correct
-- sequence' ms = foldr func (return []) ms
--   where
--         func :: (Monad m) => m a -> m [a] -> m [a]
--         func m acc
--           = do x <- m
--                xs <- acc
--                return (x: xs)

-- incorrect
-- sequence' [] = return []
-- sequence' (m:ms) = m >>= \a ->
--     as <- sequence' ms
--     return (a: as)

sequence' [] = return []
sequence' (m:ms)
  = do a <- m
       as <- sequence' ms
       return (a: as)

------ EX 7 ------------

-- correct (my own implementation)
-- mapM' :: Monad m => (a -> m b) -> [a] -> m [b]
-- mapM' f [] = return []
-- mapM' f (a:as) = do x <- f a
--                     xs <- mapM' f as
--                     return (x:xs)

-- putAndGetChar :: String -> IO Char
-- putAndGetChar xs = do putStr xs
--                       x <- getChar
--                       return x

-- correct
-- mapM' f as = sequence' (map f as)

-- correct
-- mapM' f [] = return []
-- mapM' f (a:as)
--   = f a >>= \b -> mapM' f as >>= \bs -> return (b:bs)

-- incorrect
-- mapM' f as = sequence_' (map f as)

-- incorrect
-- mapM' f [] = return []
-- mapM' f (a: as)
--   = f a >> \b -> mapM' f as >> \bs -> return (b:bs)

-- incorrect
-- mapM' f [] = return []
-- mapM' f (a:as) =
--     do
--         f a -> b
--         mapM' f as -> bs
--         return (b:bs)

-- correct
-- mapM' f [] = return []
-- mapM' f (a:as)
--   = do b <- f a
--        bs <- mapM' f as
--        return (b:bs)

-- correct
-- mapM' f [] = return []
-- mapM' f (a:as)
--   = f a >>=
--       \ b ->
--         do bs <- mapM' f as
--            return (b:bs)

-- incorrect
-- mapM' f [] = return []
-- mapM' f (a:as)
--   = f a >>=
--       \ b ->
--         do bs <- mapM' f as
--            return (bs ++ [b])

------ EX 8 --------------------

-- correct (my implementation)
-- filterM' :: Monad m => (a -> m Bool) -> [a] -> m [a]
-- filterM' _ [] = return []
-- filterM' f (x:xs) = do b <- f x
--                        bs <- filterM' f xs
--                        if b then return (x:bs) else return bs

evenS :: (Monad m) => Char -> m Bool 
evenS a = return (even ((read [a]) :: Int))

-- incorrect
-- filterM' _ [] = return []
-- filterM' p (x:xs)
--   = do flag <- p x
--        ys <- filterM' p xs
--        return (x:ys)

filterM' _ [] = return []
filterM' p (x:xs)
  = do flag <- p x
       ys <- filterM' p xs
       if flag then return (x:ys) else return ys

----- EX 9 ----------------

foldLeftM :: Monad m => (a -> b -> m a) -> a -> [b] -> m a
foldLeftM f acc [] = return acc
foldLeftM f acc (x:xs) = do n <- f acc x
                            ns <- foldLeftM f n xs
                            return ns


----------- EX 10 -----------

foldRightM :: Monad m => (a -> b -> m b) -> b -> [a] -> m b
foldRightM f acc [] = return acc
foldRightM f acc (x:xs) = do n <- foldRightM f acc xs
                             ns <- f x n
                             return ns

----------- EX 11 ------------

liftM :: Monad m => (a->b) -> m a -> m b

-- correct
-- liftM f m = do x <- m
--                return (f x)

-- incorrect
-- liftM f m = m >>= \a -> f a

-- correct
-- liftM f m = m >>= \ a -> return (f a)

-- incorrect
-- liftM f m = return (f m)

-- incorrect
-- liftM f m = m >>= \a -> m >>= \b -> return (f a)

-- incorrect
-- liftM f m = m >>= \a -> m >>= \b -> return (f b)

-- incorrect
-- liftM f m = mapM f [m]











