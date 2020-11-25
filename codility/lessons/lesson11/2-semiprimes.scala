object Solution {
    
    def findPrimes(n: Int): Array[Int] = {
        var sieve = Array.fill[Boolean](n + 1)(true)
        
        sieve(0) = false
        if (n > 1) {
            sieve(1) = false
        }
        var i = 2
        while (i * i <= n) {
            var k = i * i
            while (k <= n) {
                sieve(k) = false
                k += i
            }
            i += 1
        }
        
        sieve.zipWithIndex.filter(_._1).map(_._2)
    }
    
    def findCumulativeSemiPrimes(n: Int, primes: Array[Int]): Array[Int] = {
        val semiSieve = Array.fill[Int](n + 1)(0)
        for (i <- primes.indices) {
            for (j <- i until primes.length) {
                val product = primes(i) * primes(j)
                if (product <= n) {
                    semiSieve(product) = 1
                }
            }
        }
        
        // make semiSieve cumulative
        for (i <- 1 until semiSieve.length) {
            semiSieve(i) += semiSieve(i - 1)
        }
        semiSieve
    }
    
    def solution(n: Int, p: Array[Int], q: Array[Int]): Array[Int] = {
        val primes = findPrimes(n / 2)
        val semiPrimes = findCumulativeSemiPrimes(n, primes)
        
        // do the queries
        p.zip(q).map {
            case (from, to) =>
            
                semiPrimes(to) - semiPrimes(from - 1)
        }
    }
}
