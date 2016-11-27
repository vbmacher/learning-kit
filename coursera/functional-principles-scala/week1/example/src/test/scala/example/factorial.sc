def factorial(x: Int): Int = {

  def mfac(y: Int, result: Int): Int = {
    if (y == 0) {
      return result
    } else {
      return mfac(y - 1, result * y)
    }
  }

  mfac(x, 1)
}

factorial(5)