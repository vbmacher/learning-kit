import Data.Char

allin _ [] = "pangram"
allin [] _ = "not pangram"
allin (x:xs) ys = allin xs (filter (/= x) ys)

pangram s = allin (map toLower s) ['a'..'z']

main = interact pangram
