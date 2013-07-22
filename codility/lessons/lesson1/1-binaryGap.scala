object Solution {
  def solution(n: Int): Int = {
    var startGap = -1
    var maxGap = 0
    var last = 0
    var i = 0
    var cn = n
    
    while (cn != 0) {
        if ((cn & 1) == 0) {
            if (last == 1) {
                startGap = i
            }
            last = 0
        } else if ((cn & 1) == 1) {
            if (startGap != -1) {
                maxGap = math.max(maxGap, i - startGap)
                startGap = -1
            }
            last = 1
        }
        cn = cn >>> 1
        i += 1
    }
    maxGap
  }
}
