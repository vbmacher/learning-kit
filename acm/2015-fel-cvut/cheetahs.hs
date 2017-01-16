--
-- input:
-- N
-- tk vk
-- ...
--
-- output:
-- R

import Data.List.Split
import Data.List

type Input = [[Int]]

timeAndSpeed :: String -> [Int]
timeAndSpeed xs = [read t, read v] 
  where (t:v:_) = splitOn " " xs


inputs :: [String] -> [Input]
inputs [] = []
inputs (x:xs) = speeds:(inputs $ drop n xs)
  where n = read x :: Int
        raw = take n xs
        speeds = map timeAndSpeed raw

-- potrebujem vzdialenosti od startu
-- po vybehu vsetkych
-- to znamena: (maxTime - minTime) * speed
minmax :: Input -> Int
minmax input = amax - amin
  where (amin, amax) = foldr mm (maxBound, 0) input
        mm (a:_) (cmin, cmax) = (min a cmin, max a cmax)

dist :: (Int, Input) -> [Int]
dist (mm, input) = map multSpeed input
  where multSpeed (_:b:_) = b * mm





cheetahs xs = (show . map dist) (zip mm inp)
  where mm = map minmax inp
        inp = (inputs . lines) xs

-- kto je najdalej vzadu
-- kto je najpomalsi vpredu





main = interact cheetahs


