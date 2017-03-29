object Solution {

    def main(args: Array[String]) {
        var min = Long.MaxValue
        var max = 0L
        var sum = 0L
        for (c <- readLine().split(" ").map(_.toLong)) {
            if (c > max) max = c
            if (c < min) min = c
            sum += c
        }
        println((sum-max) + " " + (sum-min))
    }
}

