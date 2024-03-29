-- "Cubra" (Snake puzzle) solving
--
-- http://webhome.cs.uvic.ca/~mweston/snakes.html
--
-- Finds all solutions

-- Example data:
cube = [3,1,1,2,1,2,1,1,2,2,1,1,1,2,2,2,2]
cube1 = [3,2,1,2,1,2,1,1,1,2,2,2,1,2,2,2]
cube2 = [3,1,1,2,1,1,2,1,1,1,1,1,1,1,2,1,1,1,2,2]

-- Program:
data Direction = X | Y | Z | NX | NY | NZ deriving (Eq,Show,Enum)

vec X = (1,0,0)
vec Y = (0,1,0)
vec Z = (0,0,1)
vec NX = (-1,0,0)
vec NY = (0,-1,0)
vec NZ = (0,0,-1)

rots X = [Z,NZ,Y,NY]
rots Y = [X,NX,Z,NZ]
rots Z = [X,NX,Y,NY]
rots NX = [Z,NZ,Y,NY]
rots NY = [X,NX,Z,NZ]
rots NZ = [X,NX,Y,NY]

go :: (Num a) => Direction -> (a,a,a) -> (a,a,a)
go dir (x,y,z) = let (i,j,k) = vec dir in (i+x, j+y, k+z)

inbounds (i,j,k) = let xs = [i,j,k] in and [x >= 1 && x <=3 | x <- xs]
isuniq xs p = not (p `elem` xs)

get (Just x) = x

headOr [] x = x
headOr xs x = head xs

try _ 0 xs = Just xs
try dir cnt [] = try dir (cnt - 1) [(1,1,1)]
try dir cnt xs
  | valid && (recurs /= Nothing) = Just (get recurs) 
  | otherwise                    = Nothing
  where 
    valid  = inbounds pos && isuniq xs pos 
    recurs = try dir (cnt - 1) (pos:xs)
    pos    = go dir $ head xs

build :: (Num a, Eq a, Ord a) => [a] -> [(a,a,a)] -> [Direction] -> [[Direction]] -> ([Direction],[[Direction]])
build [] cs rs rss = (rs,rss)
build (x:xs) cs rs rss = foldl recurs ([],rss) $ rots prevDir
  where prevDir = headOr rs X
        tryDir dir = try dir x cs
        recurs ([],orss) dir
          | tryD == Nothing = ([],orss)
          | otherwise       = build xs (get tryD) (dir:rs) (orss)
          where tryD = tryDir dir
        recurs (ors,orss) dir
          | tryD == Nothing = ([],ors:orss)
          | otherwise       = build xs (get tryD) (dir:rs) ((ors:orss))
          where tryD = tryDir dir

        
solution xs = snd $ build xs [] [] []




