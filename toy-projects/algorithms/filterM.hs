import Control.Monad


filterX :: (Monad m) => (a -> m Bool) -> [a] -> m [a]
filterX f [] = return []
filterX f (x:xs) = do
  let rest = filterX f xs in do
  is <- f x
  if is then liftM (x:) rest else rest


