-- Fix bad-ending sentences in a line of text.
--
-- A good-ending sentence must end on the same line.
--
-- At first, input text is split to lines (by \n).
-- 
-- Then, the list of lines is transformed in a way
-- that one sentence = one line (rules are applied).
-- 
-- Finally, fixed sencentes are transformed back to text.
--
-- Program also keeps paragraphs found in the original text,
-- separating them with double \n in the final text.
--
-- Also, each unnecessary empty spaces around sentences
-- are removed.
--
--
-- Rules for detection when sentence ends:
-- - one of ".!?" chars means ending a sentence
-- - if line ends with \" and next line starts with upper-case letter,
--   then the line is end of the sentence.
-- - that's it.



import Data.Char
import Data.List

trim xs = dropWhile isSpace $ reverse $ dropWhile isSpace (reverse xs)

joinIf _ []   xs = xs
joinIf _ ys [] = [ys]
joinIf f ys p@(x:xs) = if f ys x then (ys:p) else (ys ++ (' ':x)):xs


isSentence xs ys = endsLikeSentence xs || (endsWithQuotes xs && startsWithUpper ys)
  where endsLikeSentence = flip elem ".?!" . last
        endsWithQuotes = flip elem "\"'" . last
        startsWithUpper = isUpper . head


makeParagraphs = intersperse ""

fix = makeParagraphs . foldr (joinIf isSentence) [] . map trim


main = do interact $ unlines . fix . lines
