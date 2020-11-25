object Solution {
  def solution(a: Array[Int], k: Int): Array[Int] = {
    Array.tabulate[Int](a.length)(i => {
        a((a.length + i - (k % a.length)) % a.length)
    })
  }
}

