object Solution {
  def solution(a: Array[Int]): Int = {
      var result = 0
      for (ai <- a) {
          result = result ^ ai 
      }
      result
  }
}
