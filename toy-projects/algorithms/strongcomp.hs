-- Strong components of a graph
--
-- Borrowed from book "Funkcionalne programovanie" from Jan Kollar, elfa 2009

---- doesn't work


import Data.List

data Vertex = A | B | C | D | E | F | G | H | I | J deriving (Eq, Show)-- Int

type Vertices = [Vertex]

type Graph = [(Vertex, Vertices)]



graph' :: Graph
graph' = [(A,[B,D]),(B,[C]),(C,[A,E]),(D,[]),(E,[])]

graph :: Graph
graph = [(A, [B,E,F]),
         (B,[C]), (C,[D]),(D,[D,B,C]),
         (E,[F]),
         (F,[G]), (G,[E,F,H]), (E,[G,J]),
         (H,[H,I]),
         (I,[J]),
         (J,[])]

graph'' :: Graph
graph'' = [(A, [B,C]), (B,[A,C]), (C,[A,B,G]),
           (D,[G,H]), (E,[E,G]), (F,[F,A]),
           (G,[D]), (H,[H])]

-- include a list to lists where it belongs
include xs [] = [xs]
include xs (cs:css)
  | intersect xs cs /= [] = union xs cs : css
  | otherwise = cs : (include xs css)


-- merge lists together which have something in common
unify cs [] = cs
unify cs (xs:xss) = unify (include xs cs) xss

-- find simple cycles
cpaths g path (u,us)
  | u `elem` path = [u : takeWhile (/=u) path]
  | otherwise     = foldr (++) [] [cpaths g (u:path) (v,vs) | (v,vs) <- g, u `elem` vs]


-- find visited 
visited :: Graph -> Vertices -> (Vertex, Vertices) -> Vertices
visited g path (u,us)
  | u `elem` path = []
  | otherwise     = foldr union [u] [visited g (u:path) (v,vs) | (v,vs) <- g, v `elem` us]


comp [] = []
comp g@(n:ns) = unify [] (cpaths g [] n) ++ comp g'
  where vi = visited g [] n 
        g' = [(u,us) | (u,us) <- g, not (u `elem` vi)]


strongComp :: Graph -> [Vertices]
strongComp g = unify (comp g) [[v] | (v,vs) <- g]




