-- fizz buzz

fizzes = cycle ["","","fizz"]
buzzes = cycle ["","","","","buzz"]

fb (n,("","")) = show n
fb (_,(x,y)) = x ++ y

fizzbuzz n = take n (map fb (zip [1..] (zip fizzes buzzes)))



