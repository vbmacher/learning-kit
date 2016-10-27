import Control.Monad.Writer

-- Run it like e.g.:
--
-- mapM_ putStrLn . snd . runWriter $ gcd' 8 3


gcd' :: Int -> Int -> Writer [String] Int
gcd' a b = do
  if b == 0 then do
    tell $ ["It is " ++ show a]
    return a
  else do
    let m = a `mod` b
    result <- gcd' b m
    tell $ [show a ++ " mod " ++ show b ++ " = " ++ show m]
    return result


newtype DiffList a = DiffList { get :: [a] -> [a] }

toDiffList xs = DiffList (xs++)
fromDiffList (DiffList f) = f []

instance Monoid (DiffList a) where

  mempty = DiffList $ \xs -> [] ++ xs

  (DiffList f) `mappend` (DiffList g) = DiffList $ \xs -> f (g xs)



gcd'' :: Int -> Int -> Writer (DiffList String) Int
gcd'' a b = do
  if b == 0 then do
    tell $ toDiffList ["It is " ++ show a]
    return a
  else do
    let m = a `mod` b
    result <- gcd'' b m
    tell $ toDiffList [show a ++ " mod " ++ show b ++ " = " ++ show m]
    return result



