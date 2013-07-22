import math.abs
object example {
  val tolerance = 0.00000001

  def fixedpoint(f: Double => Double)(firstGuess: Double): Double = {

    def isCloseEnough(x: Double, y: Double): Boolean = {
      abs((x - y) / x) / x < tolerance
    }

    def iter(guess: Double): Double = {
      val next = f(guess)
      if (isCloseEnough(guess, next)) {
        guess
      } else {
        iter(next)
      }
    }

    iter(firstGuess)
  }

  def averageDamp(f: Double => Double, prev: Double): Double =
    (f(prev) + prev) / 2


  def sqrt(x: Double) = fixedpoint(y => averageDamp(y => x / y, y))(1.0)

  sqrt(2)
}