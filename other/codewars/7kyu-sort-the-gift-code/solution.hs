module SortGiftCode where

insertSorted n a (ls,rs) = if a <= n then (a:ls,rs) else (ls,a:rs)

sortGiftCode :: String -> String
sortGiftCode [] = []
sortGiftCode (x:xs) =
  let (ls,rs) = foldr (insertSorted x) ([],[]) xs 
  in sortGiftCode ls ++ [x] ++ sortGiftCode rs

