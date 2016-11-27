import Prelude hiding ((||))

-- False || False = False
-- _ || _ = True


-- False || b = False
-- True || _ = True


-- b || c
--   | b == c = True
--   | otherwise = True


-- b || c
--   | b == c = b
--   | otherwise = True


-- b || False = b
-- _ || True = True



-- b || c
--   | b == c = c
--   | otherwise = True



-- b || True = b
-- _ || True = True



