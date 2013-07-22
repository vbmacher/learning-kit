object Solution {
  def solution(x: Int, y: Int, d: Int): Int = {
      val distance = y - x
      distance / d + (if (distance % d != 0) 1 else 0)
  }
}
