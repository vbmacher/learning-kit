second xs = head (tail xs)

swap (x,y) = (y,x)

pair x y  = (x, y)

double x = x * 2

palindrome xs = reverse xs == xs

twice f x = f (f x)

f xs = take 3 (reverse xs)

----- EX 4 -----

mult = \ x -> (\ y -> (\ z -> x * y * z))

remove n xs = take n xs ++ drop (n+1) xs

funct :: Int -> [a] -> [a]
funct x xs = take (x + 1) xs ++ drop x xs


--- LAB ----

e0 = [False, True, False, True]
e1 = [[1,2],[3,4]]
e2 = [[[1,2,3]], [[3,4,5]]]
e3 x = x * 2
e4 (x, y) = x
e5 (x, y, z) = z
e6 x y = x * y
e7 (x, y) = (y, x)
e8 x y = (y,x)
e9 [x, y] = (x, True)
e10 (x, y) = [x, y]
e11 = ('\a', False)
e12 = [('a', 1)]
e13 x y = x + y * y
e14 = ("Haskell", [3.1, 3.14, 3.141, 3.1415])
e15 xs ys = (head xs, head ys)












