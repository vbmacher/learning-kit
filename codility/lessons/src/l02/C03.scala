package l02

object C03 extends App {

  def solution(a: Array[Int]): Int = {
    // N is an odd integer within the range [1..1,000,000];
    //each element of array A is an integer within the range [1..1,000,000,000];

    // O (N log N)
    // sort

//    def qs(i: Int, j: Int, med: Int): Unit = {
//      var ii = i
//      var jj = j
//
//      while (ii < jj) {
//        while (a(ii) < med && ii < jj) ii += 1
//        while (a(jj) >= med && ii < jj) jj -= 1
//
//        if (ii != jj) {
//          val x = a(ii)
//          a(ii) = a(jj)
//          a(jj) = x
//        }
//      }
//
//      if (i < (j / 2)) qs(i, j / 2, a(i))
//      if (i < (j / 2 + 1)) qs(j / 2 + 1, j, a(j / 2))
//    }
//
//    qs(0, a.length - 1, a(0))
    val b = a.sorted

    var v = b(0)
    var c = 1

    for (i <- 1 until b.length) {
      if (c == 1 && b(i) == v) c = 0
      else if (c == 1) return v
      else if (c == 0) {
        v = b(i)
        c = 1
      }
    }

    if (c == 1) v
    else throw new IllegalArgumentException("each element has its pair")
  }


  println(solution(Array(9, 3, 9, 3, 9, 7, 9)))  // 7
  println(solution(Array(9)))  // 9
}
