// https://app.codility.com/demo/results/trainingGGMNM8-ZUD/

object Solution {
  def solution(a: Array[Int]): Int = {
    var size = 0
    var candidate = -1
    
    for (ai <- a) {
        if (size == 0) {
            candidate = ai
            size = 1
        } else {
            if (ai == candidate) size += 1
            else size -=1
        }
    }
    var count = 0
    if (size > 0) {
        for (ai <- a) {
            if (candidate == ai) count += 1
        }
    }
    val leader = if (count > a.length / 2) candidate else -1
    
    
    // equi-leaders are the same as the leader. why? Ok, let's suppose the array has a leader "a", meaning
    // that it occurs more than 50% in the array. Then we split the array to 2 parts so each part has some
    // different leader "b" (but both parts have the same one). It means that "b" occurs more than 50% in
    // each part. But that means if we sum the "b" occurrences together, it must mean "b" occurs more than
    // 50% in the whole array, which is a conflict with the original assumption that "a" is the leader.
    // Therefore "a" must be also each equi-leader.

    if (leader == -1) 0
    else {
        var equileaders = 0
        
        var lcount = 0
        for (i <- a.indices) {
            if (a(i) == leader) {
                lcount += 1
            }
            val rcount = count - lcount
            
            if ((lcount > (i + 1) / 2) && (rcount > (a.length - i - 1) / 2)) {
                equileaders += 1
            }
        }
        equileaders
    }
  }
}
