{-
 -Once you've installed Haskell from http://www.haskell.org/platform/, load the interpreter with the command ghci.
 -
 -You can load (and reload) this file in the interpreter with the command: ":l 2-starting-out.hs"
 -
 -The first function has been completed as an example. All the other functions are undefined.
 -They can be implemented in one line using the material covered in http://learnyouahaskell.com/starting-out
 -
 -All indices are zero based.
 -}

-- Find the penultimate element in list l
penultimate l = last (init l)

penultimate' (x:y:[]) = x
penultimate' (x:xs) = penultimate' xs


-- Find the element at index k in list l
-- For example: "findK 2 [0,0,1,0,0,0]" returns 1
findK k = (head . drop k)

findK' k l = l !! k

findK'' 0 (x:xs) = x
findK'' k (x:xs) = findK'' (k-1) xs


-- Determine if list l is a palindrome
isPalindrome l = and $ zipWith (==) (reverse l) l


{-
 - Duplicate the elements in list xs, for example "duplicate [1,2,3]" would give the list [1,1,2,2,3,3]
 - Hint: The "concat [l]" function flattens a list of lists into a single list. 
 - (You can see the function definition by typing ":t concat" into the interpreter. Perhaps try this with other variables and functions)
 -
 - For example: concat [[1,2,3],[3,4,5]] returns [1,2,3,3,4,5]
 -}
duplicate xs = concat $ zipWith (\a b -> [a,b]) xs xs

{-
 - Imitate the functinality of zip
 - The function "min x y" returns the lower of values x and y
 - For example "ziplike [1,2,3] ['a', 'b', 'c', 'd']" returns [(1,'a'), (2, 'b'), (3, 'c')]
 -}
ziplike _ [] = []
ziplike [] _ = []
ziplike (x:xs) (y:ys) = (x,y) : ziplike xs ys

-- Split a list l at element k into a tuple: The first part up to and including k, the second part after k
-- For example "splitAtIndex 3 [1,1,1,2,2,2]" returns ([1,1,1],[2,2,2])
splitAtIndex k l = (take k l, drop k l)

splitAtIndex' k l = (reverse ys, zs)
  where (ys, zs) = splitAtI k [] l
        splitAtI 0 xs ys = (xs,ys)
        splitAtI n xs ys = splitAtI (n-1) (head ys : xs) (tail ys) 

-- Drop the element at index k in list l
-- For example "dropK 3 [0,0,0,1,0,0,0]" returns [0,0,0,0,0,0]
dropK k l = let (xs,ys) = splitAt k l in xs ++ tail ys

dropK' 0 l = tail l
dropK' k l = (head l) : dropK' (k-1) (tail l)

dropK'' k l = snd $ foldl (\(c,xs) x -> if c == k then (c+1,xs) else (c+1, xs ++ [x])) (0,[]) l

dropK''' k = reverse . snd . foldl process (0,[])
  where process (c, xs) x
          | c == k     = (c+1, xs)
          | otherwise  = (c+1, x:xs)


-- Extract elements between ith and kth element in list l. Including i, but not k
-- For example, "slice 3 6 [0,0,0,1,2,3,0,0,0]" returns [1,2,3]
slice i k = (drop i) . (take k)

slice' i k l = take (k-i) $ drop i l

slice'' 0 0 _ = []
slice'' 0 k l = (head l) : (slice'' 0 (k-1) (tail l)) 
slice'' i k l = slice'' (i-1) (k-1) $ tail l


-- Insert element x in list l at index k
-- For example, "insertElem 2 5 [0,0,0,0,0,0]" returns [0,0,0,0,0,2,0]
insertElem x k l = (take k l) ++ [x] ++ (drop k l)

insertElem' x 0 l = (x : l)
insertElem' x k (l:ls) = l : (insertElem x (k-1) ls)

insertElem'' x k = snd . (foldl process (0,[]))
  where process (c, xs) i
          | c == k      = (k+1, xs ++ [x] ++ [i])
          | otherwise   = (c+1, xs ++ [i])

insertElem''' x k l = snd $ foldr process (length l, []) l
  where process i (c, xs)
          | c == k      = (k-1, i:x:xs)
          | otherwise   = (c-1, i:xs)

insertElem'''' x k l = concat $ map (\(c,i) -> if i == k then [x,c] else [c]) $ zip l [0..]


-- Rotate list l n places left.
-- For example, "rotate 2 [1,2,3,4,5]" gives [3,4,5,1,2]
rotate _ [] = []
rotate 0 xs = xs
rotate n l = rotate (n-1) $ (tail l) ++ ([head l])

rotate' n l = (drop ind l) ++ (take ind l) 
  where ind = 1 + (length l) `mod` n

rotate'' n l = let (_, ls, fs) = foldl acc (n,[],[]) l in fs ++ ls
  where acc (c, ls, fs) x
          | c > 0     = (c-1, ls ++ [x], fs)
          | otherwise = (c, ls, fs ++ [x])

rotate''' n l = let (_,fs,ls) = foldr acc (length l - n,[],[]) l in fs ++ ls
  where acc x (c, fs, ls)
          | c > 0     = (c-1, (x:fs), ls)
          | otherwise = (c, fs, (x:ls))

