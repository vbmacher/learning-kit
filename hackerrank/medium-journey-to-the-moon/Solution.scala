object Solution {
  type Pairs = Seq[(Int, Int)]

  def build(pairs: Pairs, n:Int): (Seq[Int],Int) = {
    val array = Array.fill(n){-1}
    var maxItem = 0
    var maxIndex = 0

    for ((a, b) <- pairs) {
      val va = array(a)
      val vb = array(b)

      if ((va > 0) && (vb > 0)) {
        if (va != vb) {
          for (i <- 0 to maxItem) {
            if (array(i) == vb) {
              array(i) = va
            }
          }
        }
      } else if (va > 0) {
        array(b) = va
      } else if (vb > 0) {
        array(a) = vb
      } else {
        maxIndex += 1
        array(a) = maxIndex
        array(b) = maxIndex
      }

      val maxAB = Math.max(a,b)
      if (maxAB > maxItem) {
        maxItem = maxAB
      }
    }

    val unknown = (for (i <- 0 until n
                        if array(i) == -1) yield 1).sum
    (array, unknown)
  }

  def distinct(graph: Seq[Int]):Seq[Int] = graph
      .filterNot(_ == -1)
      .groupBy(k => k)
      .map(p => p._2.length).toList

  def combs(countries: Seq[Int]):Long = countries match {
    case Nil => 0L
    case x::xs => xs.map(_ * x.asInstanceOf[Long]).foldLeft(0L)(_ + _) + combs(xs)
  }

  def main(args: Array[String]) {
    val ni = readLine().split(" ")
    val (n,i) = (ni(0).toInt, ni(1).toInt)
    val pairs = (1 to i).map(_ => readLine().split(" ").map(_.toInt))
      .map(p => p match { case Array(i, j) => (i,j)})

    val (graph, unknown) = build(pairs,n)
    val countries = distinct(graph)

    val unknownCombs = unknown.asInstanceOf[Long] * (unknown - 1L) / 2L
    val c = combs(countries) + unknown * countries.sum + unknownCombs
    println(c)
  }

}
