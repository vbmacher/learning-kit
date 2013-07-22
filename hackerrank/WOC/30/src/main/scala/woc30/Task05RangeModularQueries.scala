package woc30

object Task05RangeModularQueries {


  def main(args: Array[String]): Unit = {

    val lines = io.Source.stdin.getLines()
    val nq = lines.next().split(" ").map(_.toInt)
    val (n, q) = (nq(0), nq(1))

    val list = lines.next().split(" ").map(_.toInt).toList

    while (lines.hasNext) {
      val query = lines.next().split(" ").map(_.toInt)
      val (left,right,x,y) = (query(0), query(1), query(2), query(3))

      var count = 0
      for (i <- left to right) {
        if ((list(i) - y) % x == 0) {
          count += 1
        }
      }
      println(count)
    }
  }



}
