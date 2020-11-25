object Solution {
  def solution(a: Array[Int]): Int = {
    // we know that: we can assign first flag to first peak
    // we can have maximum K flags, iff there are K peaks distanced with K from each other
    // if they are distanced less, there will be less than K flags
    //
    // when we assigned flag to the first peak, minimal distance (K) is therefore
    // at least the distance to the next peak.
    
    var peaks = Array.fill[Int](a.length)(0)
    var peaksCount = 0
    for (i <- 1 until (a.length - 1)) {
        peaks(i) = if (a(i-1) < a(i) && a(i+1) < a(i)) 1 else 0
        if (peaks(i) == 1) {
            peaksCount += 1
        }
    }
    
    var next = Array.fill[Int](a.length)(-1)
    var lastPeak = -1
    var veryLastPeak = -1
    for (i <- (peaks.length - 1) to 0 by -1) {
        if (peaks(i) == 1) {
            if (veryLastPeak == -1) {
                veryLastPeak = i
            }
            lastPeak = i
        }
        next(i) = lastPeak
    }
    
    for (k <- peaksCount to 1 by -1) {
        var j = next(0)
        var flagsLeft = k
        var previous = -1
        while (j <= veryLastPeak && next(j) != -1 && flagsLeft > 0) {
            if (previous != next(j)) {
                flagsLeft -= 1
            }
            j += k
        }
        if (flagsLeft == 0) return k
    }
    
    0
  }
}
