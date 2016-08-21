import Control.Applicative
import Data.Monoid

-- We can use the following type to simulate our own list
data List a = Empty | Value a (List a) deriving (Show)

-- Make the list a Functor

instance Functor List where

  fmap f Empty = Empty
  fmap f (Value a b) = Value (f a) (fmap f b)


-- Write a function which appends one list on to another
combineLists:: List a -> List a -> List a
combineLists Empty b = b
combineLists (Value a c) b = Value a (combineLists c b)


-- Make our list a Monoid

instance Monoid (List a) where

  mempty = Empty
  mappend = combineLists


-- Make our list an Applicative

instance Applicative List where

  pure a = Value a Empty
  (Value f Empty) <*> l = fmap f l


-- Make sure that the List obeys the laws for Applicative and Monoid

-- Applicative laws:
-- pure id <*> (Value 5 Empty) == Value 5 Empty
-- (.) <$> u <*> v <*> w = u <*> (v <*> w)



-- Create some lists of numbers of different lengths such as:
twoValueList = Value 10 $ Value 20 Empty

-- Use <$> on the lists with a single-parameter function, such as:
plusTwo = (+2)

-- Use <$> and <*> on the lists with a binary function

-- Create some lists of binary functions

-- Use <*> on the binary functions list and the number lists
