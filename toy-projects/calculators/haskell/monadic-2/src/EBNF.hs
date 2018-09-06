module EBNF where

import Control.Applicative
import Parser

-- chainl parses some grammar part, which has repeating
-- elements, specifically this:
--
--   F { X }
--
-- You might notice there two parts:
--
-- - F      : this is required part
-- - { X }  : this is optional part; means repeating X 0 and more times
--
-- In our terms, X can be any already existing parser combinator.
--
-- Why we need F? This is important question.
-- The answer lies in answering another question - how would you
-- implement the parsing of "repeating" part? This leads us even deeper,
-- to answering already answered question - what is parsing?
-- It is a translation of input to AST - a data structure.
-- So when we would like to parse repeating part, we need put this
-- repeats into the data structure in some recursive manner. Yes,
-- recursive, because we will repeat the *same* thing - so data structure
-- needs to be also "the same", nesed inside itself.
--
-- But we still want to be general - we do not really care what this
-- data structure will be - only thing what matters is that it has to be
-- binary. Why? Well, this is the answer for the very first question -
-- why we need F?
--
-- To answer both questions, let's imagine what possibilities we have
-- when the grammar is applied:
--
-- 1. Repeating part is not present; then we should return parsed F.
-- 2. Repeating part is present; then we need to combine F with all
--    the repeating parts, using given data structure, which we need
--    to treat as the binary function.
--
-- So, then, what we need:
--
-- 1. already parsed F
-- 2. the binary function resulting in parsed data structure. It will be used
--    only if the repeating part is present.
-- 3. parser of the optional repeating part
--
-- For example, the following can happpen:
--
--      F1                      => F1
--     (F1 op F2)               => Add F1 F2
--    ((F1 op F2) op F3)        => Add (Add F1 F2) F3
--   (((F1 op F2) op F3) op F4) => Add (Add (Add F1 F2) F3) F4
--
chainl :: a -> Parser (a -> a) -> Parser a
chainl first optional = (do
    second <- optional
    chainl (second first) optional
  ) <|> return first
