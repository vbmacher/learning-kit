package woc30

import scala.annotation.tailrec

object Task01Candy {

  @tailrec
  final def count(c: List[Int], n:Int, t: Int, status: Int, added: Int): Int = c match {
    case Nil => added
    case (ci::cs) =>
      if (t <= 1) added
      else if (status - ci < 5) count(cs, n, t - 1, n, added + (n - (status - ci)))
      else count(cs, n, t - 1, status - ci, added)
  }

  def main(args: Array[String]): Unit = {
    val lines = io.Source.stdin.getLines().toSeq
    val nt = lines.head.split(" ").map(_.toInt)
    val (n,t) = (nt(0), nt(1))
    val c = lines(1).split(" ").map(_.toInt).toList

    println(count(c, n, t, n, 0))
  }


}
