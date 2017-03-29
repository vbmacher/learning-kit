module WindInfo where

runway:: [Char] -> Int
runway xs = 10 * read (take 2 xs) :: Int

cosinus:: Int -> Int -> Double
cosinus a ws = (fromIntegral(ws)::Double) * cos (pi/180 * (fromIntegral(a)::Double))

sinus:: Int -> Int -> Double
sinus a ws = (fromIntegral(ws)::Double) * sin (pi/180 * (fromIntegral(a)::Double))

headtail a ws =
  let value = round $ cosinus a ws
  in
    if value >= 0 then "Headwind " ++ show value ++ " knots."
    else "Tailwind " ++ show (-value) ++ " knots."

cross a ws =
  let value = round $ sinus a ws
  in
    if value < 0 then " Crosswind " ++ show (-value) ++ " knots from your left."
    else " Crosswind " ++ show value ++ " knots from your right."

windInfo :: [Char] -> Int -> Int -> [Char]
windInfo input aw ws = 
  let heading = runway input
  in
      headtail (aw - heading) ws ++ cross (aw - heading) ws

