import Control.Monad

combs :: String -> [String]
combs = init . filterM (const [True, False])

toInts :: [String] -> [Int]
toInts = map read . map (reverse . take 3 . reverse)

div8 :: [Int] -> Int
div8 = length . filter (\x -> x `mod` 8 == 0)

themod :: Int -> Int
themod x = x `mod` 1000000007


main = do
  digits <- getLine
  number <- getLine
  let howmany = read digits
  let count = (div8 . toInts . combs) (take howmany number)
  putStrLn $ show $ themod count
