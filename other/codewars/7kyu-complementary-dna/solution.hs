module Codewars.Kata.DNA where
import Codewars.Kata.DNA.Types

-- data Base = A | T | G | C
type DNA = [Base]

dnaStrand :: DNA -> DNA
dnaStrand = map complement
  where complement A = T
        complement T = A
        complement G = C
        complement C = G


