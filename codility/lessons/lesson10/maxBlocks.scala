object Solution {
  def solution(a: Array[Int]): Int = {
    var peaks = Array.fill[Int](a.length)(0)
    for (i <- 1 until (a.length - 1)) {
        if (a(i - 1) < a(i) && a(i + 1) < a(i)) {
            peaks(i) = 1
        }
    }
    
    var next = Array.fill[Int](a.length)(-1)
    var lastPeak = -1
    for (i <- (peaks.length - 1) to 0 by -1) {
        if (peaks(i) == 1) {
            lastPeak = i
        }
        next(i) = lastPeak
    }
    
    var i = 1
    val n = next.length
    var blocks = 0
    while (i <= n) {
        if (n % i == 0) {
            var block = 0
            var blockLength = n / i
            def current = next(block)
            
            while ((block < n) && current != -1 && current < (block + blockLength)) {
                block += blockLength
            }
            if (block == n) {
                blocks = math.max(blocks, i)
            }
        }
        i += 1
    }
    blocks
  }
}
