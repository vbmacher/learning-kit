object Solution {

  def main(args: Array[String]): Unit = {
    val lines = scala.io.Source.stdin.getLines()

    val t = lines.next().toInt
    for (n <- 1 to t) {
      val m = lines.next().toInt
      val d = m % 7
      if (d < 2) println("Second")
      else println("First")
    }
  }

}
