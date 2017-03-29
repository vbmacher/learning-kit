package woc30

object Task04PolesDP {

  def combinations(n: Int, k: Int): List[List[Int]] = {

    def mk(zeros: Array[Int]): List[Int] = {
      var current = List(0)

      var pos = 1
      var counter = 1
      for (zero <- zeros) {
        while (pos < zero) {
          current = counter :: current
          counter += 1
          pos += 1
        }
        current = 0 :: current
        pos += 1
        counter = 1
      }
      while (pos < n) {
        current = counter :: current
        counter += 1
        pos += 1
      }

      current
    }


    var zeros = Array.tabulate(k - 1)(_ + 1)
    var result: List[List[Int]] = Nil

    // (N over K) moves... Too Slow
    var zeroIndex = zeros.length - 1
    while (zeros(0) <= n - k + 1) {
      result = mk(zeros) :: result

      while (zeroIndex > 0 && zeros(zeroIndex) == n - (zeros.length - zeroIndex)) {
        zeroIndex -= 1
      }
      zeros(zeroIndex) += 1
      for (i <- zeroIndex + 1 until zeros.length) {
        zeros(i) = zeros(i - 1) + 1
      }
      zeroIndex = zeros.length - 1

    }
    result
  }

  def mkCosts(n:Int, xws: Seq[(Int, Int)]): Array[Array[Int]] = {
    Array.tabulate(n, n) {
      (i,j) => math.max(0, (xws(n - j - 1)._1 - xws(n - i - 1)._1) * xws(n - j - 1)._2)
    }
  }


  def main(args: Array[String]): Unit = {
    val lines = io.Source.stdin.getLines()
    val nk = lines.next().split(" ").map(_.toInt)
    val (n, k) = (nk(0), nk(1))

    val xws = for {
      i <- 0 until n
      l = lines.next()
      xw = l.split(" ").map(_.toInt)
    } yield (xw(0), xw(1))

    if (k == 1) {
      val x = (for {
        i <- n - 1 to 1 by -1
      } yield (xws(i)._1 - xws(0)._1) * xws(i)._2).sum

      println(x)
      return ()
    }

    val stacks = combinations(n, k)
    val stackCosts = mkCosts(n, xws)

    val costs = (for {
      stack <- stacks
    } yield (for {
      i <- stack.indices
    } yield stackCosts(i + stack(i))(i)).sum).min

    println(costs)

  }


}
