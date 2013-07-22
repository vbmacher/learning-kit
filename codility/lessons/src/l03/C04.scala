package l03

object C04 extends App {

  def solution(a: Array[Int]): Int = {
    val b = Array.fill(a.length + 2)(0)
    for (item <- a) b(item) = 1

    for (i <- b.indices.tail)
      if (b(i) == 0) return i

    throw new IllegalArgumentException("All elements are there?!")
  }
}
