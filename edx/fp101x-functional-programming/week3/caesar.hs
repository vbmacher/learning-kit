import Data.Char

let2int :: Int -> Char -> Int
let2int b c = ord c - b

int2let :: Int -> Int -> Char
int2let b n = chr (b + n)

shift :: Int -> Char -> Char
shift n c
  | isLower c = convert 'a' c
  | isUpper c = convert 'A' c
  | otherwise = c
  where convert b c = int2let (ord b) (((let2int (ord b) c) + n) `mod` 26)

encode :: Int -> String -> String
encode n xs = [shift n x | x <- xs]

