package calculator

object Polynomial {
  def computeDelta(a: Signal[Double], b: Signal[Double],
      c: Signal[Double]): Signal[Double] = {
    Signal {
      math.pow(b(), 2) - 4 * a() * c()
    }
  }

  def computeSolutions(a: Signal[Double], b: Signal[Double],
      c: Signal[Double], delta: Signal[Double]): Signal[Set[Double]] = {
    Signal {
      val d = delta()
      val va = a()
      val vb = b()

      if (d < 0) Set()
      else {
        val x1 = (-vb + math.sqrt(d)) / (2 * va)
        val x2 = (-vb - math.sqrt(d)) / (2 * va)

        Set(x1, x2)
      }
    }
  }
}
