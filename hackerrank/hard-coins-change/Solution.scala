import scala.io.StdIn

object Solution {

  // 166 23
  // 5 37 8 39 33 17 22 32 13 7 10 35 40 2 43 49 46 19 41 1 12 11 28
  //
  // expecteD: 96190959

  // WA: 250 26
  // 8 47 13 24 25 31 32 35 3 19 40 48 1 4 17 38 22 30 33 15 44 46 36 9 20 49

  // expected: 3542323427

  def compute(n: Int, m: Int, coins:Seq[Int]):Long = {
    var table: Array[Array[Long]] = Array.fill(m + 1, n + 1)(-1)

    def combs(i: Int, make: Int): Long = {
      if (make == 0) {
        return 1L
      }
      if (make < 0) {
        return 0L
      }
      if (i < m && table(i)(make) == -1) {
        table(i)(make) = combs(i + 1, make) + combs(i, make - coins(i))
      }
      if (i < m) table(i)(make) else 0L
    }

    combs(0, n)
  }

  def main(args: Array[String]): Unit = {
    val nm = StdIn.readLine().split(" ").map(_.toInt)
    val (n, m) = (nm(0), nm(1))
    val coins = StdIn.readLine().split(" ").map(_.toInt)

    println(compute(n, m, coins))
  }

}
