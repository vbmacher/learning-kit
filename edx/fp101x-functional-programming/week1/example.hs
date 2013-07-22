
n = a `div` length xs
  where a = 10
        xs = [1,2,3,4,5]

------------

alast xs = drop (length xs - 1) xs  -- incorrect

blast xs = head (drop (length xs -1) xs) -- correct

clast xs = tail (reverse xs) -- incorrect

dlast xs = reverse (head xs) -- incorrect

elast xs = xs !! (length xs - 1) -- correct

flast xs = head (drop (length xs) xs) -- incorrect

glast xs = head (reverse xs) -- correct

hlast xs = reverse xs !! (length xs -1) -- incorrect

-----------

ainit xs = tail (reverse xs) -- incorrect

binit xs = reverse (head (reverse xs)) -- incorrect

cinit xs = reverse (tail xs) -- incorrect

dinit xs = take (length xs) xs -- incorrect

einit xs = reverse (tail (reverse xs)) -- correct

finit xs = take (length xs - 1) (tail xs) -- incorrect

ginit xs = drop (length xs -1) xs -- incorrect

---------------

-- correct
qsort [] = []
qsort (x: xs) = qsort larger ++ [x] ++ qsort smaller
  where smaller = [a | a <- xs, a <= x]
        larger = [b | b <- xs, b > x]


-- incorrect
bqsort [] = []
bqsort (x: xs) = reverse (bqsort smaller ++ [x] ++ bqsort larger)
  where smaller = [a | a <- xs, a <= x]
        larger = [b | b <- xs, b > x]

-- incorrect
cqsort [] = []
cqsort xs = cqsort larger ++ cqsort smaller ++ [x]
  where x = minimum xs
        smaller = [a | a <- xs, a <= x]
        larger = [b | b <- xs, b > x]

-- incorrect
dqsort [] = []
dqsort (x :xs)
  = reverse (dqsort smaller) ++ [x] ++ reverse (dqsort larger)
  where smaller = [a | a <- xs, a <= x]
        larger = [b | b <- xs, b > x]

-- correct
eqsort [] = []
eqsort (x: xs) = eqsort larger ++ [x] ++ eqsort smaller
  where larger = [a | a <- xs, a > x || a == x]
        smaller = [b | b <- xs, b < x]

-- incorrect
fqsort [] = []
fqsort (x : xs) = fqsort larger ++ [x] ++ fqsort smaller
   where smaller = [a | a <- xs, a < x]
         larger = [b| b <- xs, b > x]

-- correct
gqsort [] = []
gqsort (x: xs)
  = reverse 
      (reverse (gqsort smaller) ++ [x] ++ reverse (gqsort larger))
  where smaller = [a | a<- xs, a <= x]
        larger = [b | b <- xs, b > x]

hqsort [] = []
hqsort xs = x : hqsort larger ++ hqsort smaller
  where x = maximum xs
        smaller = [a | a <- xs, a < x]
        larger = [b | b <- xs, b >= x]










































