-- Compute Levenshtein algorithm for determining string similarity

hrever = head . reverse


leven xs ys
  | min lxs lys == 0 = max lxs lys
  | otherwise = minimum [
                  (leven ixs ys) + 1,
                  (leven xs iys) + 1,
                  (leven ixs iys) + f
                ]
  where lxs = length xs
        lys = length ys
        ixs = init xs
        iys = init ys
        f = if (hrever xs == hrever ys) then 0 else 1


-- Find the most similar word from database
-- xs - "the" word
-- db - database of words
similar xs db = foldr f ([],maxBound::Int) $  zip db $ map (leven xs) db
  where f (rs,c) (ds,l) = if c < l then (rs,c) else (ds,l)





