package woc30

import scala.annotation.tailrec

object Task02FindTheMinimum {

  @tailrec
  final def smin(n: Int, c:Int, buf: String): String = {
    if (c == n - 1) {
      return buf + "int" + (")" * (n-1))
    }
    smin(n, c+1, buf + "min(int, ")
  }

  def main(args: Array[String]): Unit = {
    val n = io.Source.stdin.getLines().next().toInt

    if (n == 1) println("int")
    else if (n > 1) println(smin(n, 0, ""))
  }

}
