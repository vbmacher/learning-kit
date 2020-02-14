package l02

object C02 extends App {

  def solution(a: Array[Int], k: Int): Array[Int] = {

    if (a.nonEmpty && k % a.length != 0) {
      val b = Array.fill(a.length)(0)

      for (i <- a.indices) {
        val j = (i + k) % a.length
        b(j) = a(i)
//      var previous = a(0)
//      var i = 0
//
//      for (_ <- a.indices) {
//        val prev = (i - k) % a.length
//        val next = (i + k) % a.length
//
//        val ai = previous
//        previous = a(next)
//        a(next) = ai
//
//        println(s"  i=$i next=$next, prev=$prev: ${a.mkString(",")}" )
//
//        i = next
      }
      b
    } else a
  }



  println(solution(Array(1,2,3), 3).mkString(","))
  println(solution(Array(1,2,3), 1).mkString(","))
  println(solution(Array(1,2,3), 2).mkString(","))
  println(solution(Array(1,2,3), 999).mkString(","))
  println(solution(Array(-1, -2, -3, -4, -5), 10).mkString(","))
  println(solution(Array(-1, -2, -3, -4, -5, -6), 10).mkString(",")) // should be: [-3, -4, -5, -6, -1, -2]
  println(solution(Array(1, 2, 3, 4, 5, 6), 10).mkString(",")) // should be: [-3, -4, -5, -6, -1, -2]
}
