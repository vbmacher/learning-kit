--sito (x:xs) n = x : sito [x | x <- xs, x `mod` n /= 0] (n+1)
--eratosthenes = 1 : 2 : (sito [3..] 2)
--

sito (x:xs) n = x : sito [y | y <- xs, y `mod` n /= 0] (n+1)
eratostenes = 1 : 2: (sito [3..] 2)




