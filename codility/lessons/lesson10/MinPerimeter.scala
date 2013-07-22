object Solution {
  def solution(n: Int): Int = {
    // S = A * B
    // O = 2 * (A + B)
    // find 2 factors of S which are minimal....
    
    var min = 2 * (n + 1)
    var i = 1
    while (i * i <= n) {
        if (n % i == 0) {
            val perimeter = 2 * (n/i + i)
            if (perimeter < min) {
                min = perimeter
            }
        }
        i += 1
    }
    min
  }
}

