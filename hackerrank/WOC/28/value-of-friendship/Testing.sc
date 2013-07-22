import Solution.{Root, Topper}

object Testing {
  var topper = new Topper(5)

  def oncemore(topper: Topper, n: Int) = {
    val ps = topper.graph.filter(_.isInstanceOf[Root])
      .map(_.asInstanceOf[Root].pairs)
      .sortBy(_.length)
      .reverse

    val ntop = new Topper(n)

    for (pairs <- ps) {
      for (pair <- pairs) {
        println(pair)
        ntop.friend(pair._1, pair._2)
      }
    }
    ntop.total
  }


//  1
//  5 4
//  1 2   - 2
//  3 2   - 2 + 2 + 2 = 6
//  4 2   - 3 +
//  4 3

  topper.friend(0,1)
  topper.friend(2,1)
  topper.friend(3,1)
  topper.friend(3,2)

  // 0-1
  // 2
  // 3-4
  //

//  1
//  5 3
//  1 2
//  3 4
//  4 5  ==> should be 16
//  topper.friend(0,1)
//  topper.friend(2,3)
//  topper.friend(3,4)

  oncemore(topper, 5)





}