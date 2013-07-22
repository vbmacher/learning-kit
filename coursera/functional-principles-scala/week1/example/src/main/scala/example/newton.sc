object session {
  def abs(x:Double) = if (x < 0) -x else x

  def sqrt(x: Double) = {
    def sqrtIter(guess: Double): Double =
      if (isGoodEnough(guess)) guess
      else sqrtIter(improve(guess))

    def isGoodEnough(guess: Double) =
      abs(guess * guess - x) / x < 0.001

    def improve(guess: Double) = (guess + x / guess) / 2

    sqrtIter(1.0)
  }

  sqrt(10000000)
  val a = sqrt(10000020)

  (a * a - 10000000)
  (a * a - 10000000) / 10000000
  0.1/5

  sqrt(0.001)
  sqrt(1e-6)
  sqrt(1e60)


}