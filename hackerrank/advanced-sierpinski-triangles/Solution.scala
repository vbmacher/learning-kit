object Solution {
  type Point = (Int, Int)
  case class Triangle(a: Point, b:Point, c:Point)

  def cut(t: Triangle): List[Triangle] = t match {
    case Triangle(a,b,c) =>
      val xHH = (((c._1 - 1.0) - a._1) / 2.0).toInt
      val yHalf = ((a._2 + c._2) / 2.0).toInt

      List(
        Triangle((a._1 + xHH + 1, yHalf + 1), (b._1 - xHH - 1, yHalf+1), c),
        Triangle(a, (c._1 - 1, a._2), (a._1 + xHH, yHalf)),
        Triangle((c._1 + 1, a._2), b, (b._1 - xHH, yHalf))
      )
  }

  def trianglize(m: Int)(triangles: List[Triangle]): List[Triangle] = {
    if (m == 0) triangles
    else trianglize(m - 1)(triangles.flatMap(cut))
  }

  def points(t: Triangle): Seq[(Int, Int)] = t match {
    case Triangle(a,b,c) => for {
      i <- 0 to c._2
      x = a._1 + i
      y = b._1 - i
      if x <= y
      j <- x.toInt to y
    } yield (j, a._2 + i)
  }

  def lineize(line: List[Int]): String = {
    (for {
      i <- 0 until 63
      c = line.contains(i)
    } yield if (c) '1' else '_').mkString("")
  }


  def drawTriangles(n: Int) {
    val triangles = trianglize(n)(List(Triangle((0, 0), (62, 0), (31, 31)))) flatMap points groupBy (p => p._2)

    for (i <- 31 to 0 by -1) {
      if (triangles.contains(i))
        println(lineize(triangles(i) map (x => x._1)))
      else
        println("_______________________________________________________________")
    }
  }


  def main(args: Array[String]) {
    drawTriangles(readInt())
  }
}
