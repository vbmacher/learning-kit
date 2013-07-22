fib :: [Integer] -> Integer
fib (t1:t2:n:_)
  | n == 1    = t1
  | n == 2    = t2
  | otherwise = f2 + f1 * f1
  where f1 = fib [t1,t2,n-1]
        f2 = fib [t1,t2,n-2]



main = interact $ show . fib . map read . words
