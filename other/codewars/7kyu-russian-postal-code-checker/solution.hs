module Codewars.Kata.ZipValidate where

import Data.Char

firstValid x = all (/= x) ['0','5','7','8','9']
restValid xs = all isDigit xs

zipValidate :: String -> Bool
zipValidate [] = False
zipValidate (x:xs) = (firstValid x) && (length xs == 5) && restValid xs

