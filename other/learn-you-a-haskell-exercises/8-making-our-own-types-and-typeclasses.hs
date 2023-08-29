{-
 - We are going to create some types for a deck of cards
 - The cards need to have an ordering, based on the standard ranking http://en.wikipedia.org/wiki/Standard_52-card_deck#Rank_and_color
 - We are assuming Aces are high.
 - Therefore, the following statements should be true:
 -    (Card Ace Spades) > (Card King Spades)
 -    (Card Two Clubs) < (Card Three Clubs)
 -
 - We are going to provide our own implementation of the Show typeclass for the Card type.
 - When displaying the Card instance in GHCI, or calling show (Card digit suit), the String which should be displayed is "The <Digit> of <Suit>"
 -
 - Uncomment the following declarations to complete the implementation, and provide an implementation for instance Show Card
 -}

data Suit = Clubs | Diamonds | Hearts | Spades deriving (Show,Eq)
data Digit = Two | Three | Four | Five | Six | Seven | Eight | Nine | Ten
             | Jack | Queen | King | Ace deriving (Ord, Eq, Bounded, Enum, Show)
data Card = Card Digit Suit deriving (Ord,Eq)

instance Ord Suit where
  compare a b = EQ

instance Show Card where
  show (Card d s) = "The " ++ show d ++ " of " ++ show s

-- We should be able to provide a function which returns the higher ranked card:
betterCard :: Card -> Card -> Card
betterCard x y = if x > y then x else y

-- Here is a new Typeclass, which represents some kind of playing hand in a game.
-- It returns True for a "winning hand", depending on the rules for the type of class we are playing with
class Hand a where
    play :: [a] -> Bool

-- Implement Hand for Card, where play returns true if the list contains the Ace of Spades
instance Hand Card where
    play c = (Card Ace Spades) `elem` c

-- Create a new Coin type
data Coin = Head | Back deriving (Eq)

-- Implement Hand for Coin, where play returns true if there are ten heads in a row in the list
instance Hand Coin where
	play = (==10) . length . takeWhile (==Head) . take 10

-- Have a play with implementing Hand for some other types, for instance Int and Bool
