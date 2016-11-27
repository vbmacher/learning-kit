evens :: [Integer] -> [Integer]
evens xs = [ x | x <- xs, even x]

squares :: Integer -> Integer -> [Integer]
squares m n = [x*x | x <- [(n+1)..(n+m)]]


sumSquares x = sum . uncurry squares $ (x,x)

coords :: Integer -> Integer -> [(Integer, Integer)]
coords m n = [(x,y) | x <- [0..m], y <- [0..n]]

