-- Problem 12
-- (**) Decode a run-length encoded list.
--
-- Given a run-length code list generated as specified in problem 11. Construct its uncompressed version.
--
-- Example in Haskell:
--
-- decodeModified [Multiple 4 'a',Single 'b',Multiple 2 'c', Multiple 2 'a',Single 'd',Multiple 4 'e']
-- "aaaabccaadeeee"


data Encoded a = Single a | Multiple Int a deriving (Show)

decodeModified = concat . map transform
  where transform (Multiple n b) = replicate n b
        transform (Single b) = [b]


decodeModified' [] = []
decodeModified' ((Single a):xs) = a : (decodeModified' xs)
decodeModified' ((Multiple n a):xs) = (replicate n a) ++ (decodeModified' xs)

decodeModified'' = concatMap (uncurry replicate . toTuple) 
  where toTuple (Multiple n a) = (n,a)
        toTuple (Single a) = (1,a)



