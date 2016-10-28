alter xs = show $ foldr isAlter 0 $ zip (init xs) (tail xs)
  where isAlter (a,b) c = if a == b then c+1 else c


main = interact (unlines . map alter . tail . lines)
