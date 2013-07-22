package l01

object C01 extends App {

  // you can write to stdout for debugging purposes, e.g.
  // println("this is a debug message")

    def solution(n: Int): Int = {

      // find the borders
      var left = math.ceil(math.log(n) / math.log(2)).toInt + 1

      var maxGap = 0
      var gapStart = -1

      while (left >= 0) {
        if ((n & (1L << left)) == (1L << left)) {
          if (gapStart == -1) gapStart = left
          else {
            val gap = gapStart - (left + 1)
            if (gap > maxGap) {
              maxGap = gap
            }
            gapStart = left
          }
        }
        left -= 1
      }

      maxGap
    }


    println(solution(15)) // 1111
    println(solution(9)) // 1001
    println(solution(1041)) // 10000010001
    println(solution(32)) // 100000
    println(solution(9)) // 1001
    println(solution(328)) // 101001000
    println(solution(2147483647)) // 1111111111111111111111111111111

}
