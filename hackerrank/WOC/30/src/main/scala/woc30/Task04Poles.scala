package woc30

import scala.collection.mutable.ArrayBuffer

object Task04Poles {
  /*
  * n = 5 : 2 * 1                  2
  *         12 * 10
  *         2 * 13
  *         4 * 18
  *         2 * 17
  * --------------
  * n = 4: 12*1 + 12*10 = 132
  *        2 * 13       = 26       26
  *        4 * 18       = 72
  *        2 * 17       = 34
  *---------------
  * n = 3: 14*1 + 14*10 = 154
  *        4*13 + 4*18  = 124
  *        2*17         = 34       34
  *---------------
  * n = 2: 14*1 + 14*10 = 154      154
  *        6*16 + 6*18  = 204
  *
  *                                ========
  *                                216
  *  */


  // This is NP-complete problem called k-Partitioning.
  //
  // However, there exist some improvement using dynamic programming.
  //
  // BUT THIS SOLUTION IS IN FACT GREEDY APPROACH. WHY?
  //   - it searches for the local optima and goes by its path (always by min stack sizes)
  //
  // BUT GREEDY IS DO NOT ALWAYS PRODUCE OPTIMAL COST!!! Because it's too efficient... and does not systematically
  // try other solutions

  class Root(var n: Int, k: Int, var children: ArrayBuffer[ArrayBuffer[Int]]) {
    var cost = 0

    def deflate(from: Int, tree: ArrayBuffer[Int]): Unit = {
      tree(from) += tree(from+1)
      tree.remove(from+1)
    }

    def dec():Unit = {
      if (children.isEmpty) {
        n -= 1
        return
      }
      val (min, index) = children.map(_.head).zipWithIndex.min //  todo: fasten
      cost += min

      for (i <- index - 1 to 0 by -1) {
        deflate(index - 1 - i, children(i))
      }
      if (index + 1 < children.size) {
        for (i <- 1 until children(index).size) {
          children(index + 1)(i - 1) += children(index)(i)
        }
        children.remove(index)
      } else {
        children(index).remove(0)
        if (children(index).isEmpty) {
          children.remove(index)
        }
      }

      n -= 1
    }

    def decToEnd(): Unit = {
      if (k == 1) {
        n += 1
      }
      while (n > k) {
        dec()
      }
    }
  }


  def stackPoles(n: Int, k: Int, xws: Seq[(Int, Int)]): Int = {
    if (n == k) return 0

    val dists = xws map(_._1) zip xws.tail.map(_._1) map(p => p._2 - p._1)
    val tabs = (for {
      i <- n - 1 to 0 by -1
    } yield dists.take(i).map(_ * xws(i)._2).to[ArrayBuffer].reverse).init

    var r = new Root(n, k, tabs.to[ArrayBuffer])
    r.decToEnd()
    r.cost
  }

  def main(args: Array[String]): Unit = {
    val lines = io.Source.stdin.getLines().toSeq
    val nk = lines.head.split(" ").map(_.toInt)
    val (n, k) = (nk(0), nk(1))

    val xws = for {
      l <- lines.tail
      xw = l.split(" ").map(_.toInt)
    } yield (xw(0), xw(1))


    println(stackPoles(n, k, xws))
  }

}
