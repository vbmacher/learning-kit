-- Compute Levenshtein algorithm for determining string similarity

hrever = head . reverse


-- cut from the right side
leven [] xs = length xs
leven xs [] = length xs
leven xs ys = minimum [1 + leven ixs ys, 1 + leven xs iys, f + leven ixs iys]
  where ixs = init xs
        iys = init ys
        f = if (hrever xs == hrever ys) then 0 else 1

-- cut from the left side
leven' [] ys = length ys
leven' xs [] = length xs
leven' xs ys = minimum [1 + leven' txs ys, 1 + leven' xs tys, f + leven' txs tys]
  where txs = tail xs
        tys = tail ys
        f = if head xs == head ys then 0 else 1


-- Find the most similar word from database
-- xs - "the" word
-- db - database of words
similar xs db = foldr f ([],maxBound::Int) $  zip db $ map (leven xs) db
  where f (rs,c) (ds,l) = if c < l then (rs,c) else (ds,l)




