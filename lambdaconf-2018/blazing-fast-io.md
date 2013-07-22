# Blazing fast IO
John A. De Goes

The cost of value effects
  - Future is very slow
  - cats, monix, scalaz : a lot faster (100x)

monad introduces:
  - 1 extra allocation, 1 megamorphic dispatches

Instead of monads -> Kleisli effects



## Generalizing Monads to Arrows

def println(line: String): Unit     -->     val println: FunctionIO[String, Unit]      instead of println(line: String): IO[Unit]

easy composition

arrows introduce:
  - 6 extra allocations, 3 extra megamorphic dispatches


## KleisliIo

- 0 extra allocations, 1 extra megamorphic dispatch

val println: KleisliIO[IOException, String, Unit]

println("Hello, world!") // IO[IOException, Unit]


