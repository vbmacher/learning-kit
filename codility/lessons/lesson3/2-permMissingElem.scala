object Solution {
  def solution(a: Array[Int]): Int = {
    var perm = Array.fill[Boolean](a.length + 1)(false)
    for (ai <- a) {
        perm(ai - 1) = true
    }
    for (i <- perm.indices) {
        if (!perm(i)) {
            return i + 1
        }
    }
    return 0
  }
}
