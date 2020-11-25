object Solution {
  def solution(a: Array[Int]): Int = {
    var left = Array.fill[Int](a.length)(0)
    var right = Array.fill[Int](a.length)(0)
    var min = Int.MaxValue
    
    left(0) = a(0)
    right(0) = a(a.length - 1)
    for (i <- 1 until a.length) {
        left(i) = left(i - 1) + a(i)
        right(i) = right(i - 1) + a(a.length - i - 1)
    }
    for (i <- 0 until (a.length - 1)) {
        min = math.min(min, math.abs(left(i) - right(a.length - i - 2)))
    }
    min
  }
}
