//

// [0, 10, -5, -2, 0]   (got 0 expected 10)
// [5, 17, 0, 3] (got 0 expected 17)
// [5, 5, 5] (got 5 expected 0)


object Solution {
    
    // We know how to compute a maximum slice of (X+1 to Z-1).
    //
    // If we remove some "good" Y from the maximum slice, can there be another slice with
    // different X and Z which will perform better?
    //
    // Where (X+1) begins, we can still use "max_ending" - so (X+1) will be the same for
    // max slice and max double slice.
    //
    // What about (Z-1)? 
    // Let's think differently. Let's suppose we have a max slice, with given (X+1) and (Z+1).
    // If we choose Y= minimum number between (X+1) and (Z+1) and remove it, would it be still max slice?
    //
    // If the number was negative then yes (we will just improve it).
    //
    // If it was positive, then it means there are no negative numbers in the max slice (otherwise we would
    // choose it), so considering that (X+1) won't change, we can operate just with (Z+1). But since Y was positive
    // and we know that max slice ended in given (Z+1) anyway, then we won't improve the situation by moving
    // Z forwards, even after removing Y.
    //
    // So classic maximum slice is a "base".
    // Also, know that first and last item shouldn't be considered at all.
    // And that the minimum from X+1 to Z-1 can be computed "on the fly".
    
    
  def solution(a: Array[Int]): Int = {
    var maxEnding = a(1)
    var maxSlice = 0
    var min = a(1)
    
    for (i <- 2 until (a.length - 1)) {
        if (a(i) > maxEnding + a(i) - min) {
            maxEnding = a(i)
            min = a(i)
        } else {
            maxEnding += a(i)
            min = math.min(a(i), min)
        }
        maxSlice = math.max(maxEnding - min, maxSlice)
    }
    
    maxSlice
  }
}
