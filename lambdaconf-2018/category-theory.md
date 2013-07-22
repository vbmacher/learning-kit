# Category theory
Steven Syrek

- teaser: yoneda lemma

function intuituion:
  - relation axb
  - set of pairs (a,b)
  - table
  - translation from domain to codomain
  - mapping from one set to another
  - black box / abstraction
  - arrow a -> b  (graph)
  - image of "a" in "b"
  - structure-preserving map  - preserves "a" somehow in "b"

cardinatily of a function A -> B is (B ^ A)


## Types of functions
  - injective (one-to-one)
    - pre kazde a existuje spojenie s nejakym unikatnym b
    - preserves distinctness: it never maps distinct elements of its domain to the same element of its codomain
  - surjetive (onto)
    - pre kazde b existuje spojenie s a (nemusi byt unikatne - teda viacero "a" mozu pointovat na "b")
    - if each element of the codomain is mapped to by at least one element of the domain. (That is, the image and the codomain of the function are equal.)
  - bijective
    - injective + surjective
  - total
    - all input (domain) is covered
  - partial
    - not all input (domain) is covered

## Category

  - objects + arrows
  - something like "patterns"
  
  - endofunctor is a functor from one category to the same category
  
  
  Monad is just a monoid in a category of endofunctors!
  
## Natural Transformation

Naturality condition: componentY . F f = G f . componentX


component :: forall a. F a -> G a
fmap f . component == component . fmap f




