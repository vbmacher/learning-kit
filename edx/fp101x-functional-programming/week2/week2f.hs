-- halve xs = (take n xs, drop n xs)
--  where n = length xs / 2

halve xs = splitAt (length xs `div` 2) xs

halve2 xs = (take (n `div` 2) xs, drop (n `div` 2) xs)
  where n = length xs

halve3 xs = (take n xs, drop (n + 1) xs)  -- incorrect
  where n = length xs `div` 2

halve4 xs = splitAt (div (length xs) 2) xs

-- halve5 xs = splitAt (length xs / 2) xs

halve6 xs = (take n xs, drop n xs)
  where n = length xs `div` 2

------------------------- EX 1 ------------------

safetail xs = if null xs then [] else tail xs

safetail2 [] = []
safetail2 (_:xs) = xs

safetail3 (_:xs) -- incorrect
  | null xs = []
  | otherwise = tail xs

safetail4 xs
  | null xs = []
  | otherwise = tail xs

safetail5 xs = tail xs -- incorrect
safetail5 [] = []

safetail6 [] = []
safetail6 xs = tail xs

safetail7 [x] = [x]     -- incorrect
safetail7 (_ : xs) = xs

safetail8
  = \ xs ->
      case xs of
          [] -> []
          (_ : xs) -> xs












