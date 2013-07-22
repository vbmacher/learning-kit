import Prelude(hiding last)

----- ex 0

last :: [a] -> a
last [x] = x
last (_ : xs) = last xs

