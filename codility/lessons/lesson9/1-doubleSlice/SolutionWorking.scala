object Solution {

  def solution(a: Array[Int]): Int = {
      
      var left = Array.fill[Int](a.length)(0)
      var right = Array.fill[Int](a.length)(0)
      
      for (i <- 1 until (a.length - 1)) {
          left(i) = math.max(left(i-1), left(i-1) + a(i))
          
          val ri = a.length - i - 2
          right(ri) = math.max(right(ri + 1), right(ri + 1) + a(ri + 1))
      }
      
      var maxslice = 0
      for (i <- 1 until (a.length - 1)) {
          maxslice = math.max(maxslice, left(i) + right(i+1))
      }
      maxslice
  }
}
