import scala.collection.mutable

object Solution {

  def main(args: Array[String]): Unit = {
    val sc = new java.util.Scanner (System.in)

    val queries: Int = sc.nextInt()

    for (i: Int <- 0 until queries) {
      val (n, m) = (sc.nextInt(), sc.nextInt())

      var nodes: Array[Int] = Array.tabulate(n)(i => -1)
      val indexes: Array[mutable.ArrayBuffer[Int]] = Array.tabulate(m)(i => new mutable.ArrayBuffer)
      var index = 0

      for (l <- 0 until m) {
        val (i,j) = (sc.nextInt() - 1, sc.nextInt() - 1)

        if (nodes(i) == -1 && nodes(j) == -1) {
          nodes(i) = index
          nodes(j) = index

          indexes(index) += i
          indexes(index) += j

          index += 1
        } else if (nodes(i) == -1) {
          nodes(i) = nodes(j)
          indexes(nodes(j)) += i
        } else if (nodes(j) == -1) {
          nodes(j) = nodes(i)
          indexes(nodes(i)) += j
        } else if (nodes(i) != nodes(j)) {
          if (indexes(nodes(i)).length < indexes(nodes(j)).length) {
            val vi = nodes(i)
            for (k <- indexes(vi)) {
              nodes(k) = nodes(j)
            }
            indexes(nodes(j)) ++= indexes(vi)
            indexes(vi).clear()
          } else {
            val vj = nodes(j)
            for (k <- indexes(vj)) {
              nodes(k) = nodes(i)
            }
            indexes(nodes(i)) ++= indexes(vj)
            indexes(vj).clear()
          }
        }
      }

      val friends = Array.tabulate(n)(n => 0L)
      val groups = indexes.filterNot(_.isEmpty).map(_.length)

      var total = 0L
      var friendsCount = 0L
      var nonredundant = 0L

      for (k <- groups.sorted.reverse) {
        var fc = 0L
        for (l <- 1L until k.asInstanceOf[Long]) {
          fc = l * (l + 1L)
          total += (friendsCount + fc)
        }
        friendsCount += (k * (k - 1L))

        nonredundant += (k - 1L)
      }

      total += friendsCount * (m.asInstanceOf[Long] - nonredundant)
      println(total)
    }
  }


}