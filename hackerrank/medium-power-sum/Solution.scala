object Solution {
    
    def numberOfWays(x:Int,n:Int):Int = {
        def combinations(y: Int, ps: List[Int]):Int = {
            if (y < 0) 0
            else if (y == 0) 1
            else {
                (for {
                  i <- ps.indices
                  pps = ps.drop(i + 1)
                  p = ps(i)
                  c = combinations(y - p, pps)
                } yield c).sum
            }
        }

        val ps = (1 to math.pow(x, 1.0/n).toInt).map(math.pow(_, n).toInt)
        combinations(x, ps.toList)
    }

    def main(args: Array[String]) {
       println(numberOfWays(readInt(),readInt()))
    }
}
