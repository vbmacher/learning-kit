import scala.collection.mutable.ArrayBuffer

object Solution {

  type Graph = List[ArrayBuffer[Int]]

  class Topper(n: Int) {
    val graph : Graph = List.tabulate(n)(_ => new ArrayBuffer)
    var top = 0

    def friend(i: Int, j: Int): Unit = {

      def findFriendsOf(k: Int, friends: ArrayBuffer[Int], except:Set[Int]): Unit = {
        for (m <- 0 until n
             if !except.contains(m)
             if graph(k).contains(m)
        ) {
          if (!friends.contains(m)) {
            friends += m
            findFriendsOf(m, friends, except + m)
          }
        }
      }

      if (!graph(i).contains(j)) {
        graph(i) += j
      }
      if (!graph(j).contains(i)) {
        graph(j) += i
      }

      var xtop = 0
      for (k <- 0 until n) {
        val friends = new ArrayBuffer[Int]
        findFriendsOf(k, friends, Set(k))
        val cnt = friends.length
        xtop += cnt

    //    println("%d has %d friends".format(k, cnt))
      }
      top += xtop
    //  println("-------------")
    }
  }


  def main(args: Array[String]) {
    val sc = new java.util.Scanner (System.in)
    val t = sc.nextInt()
    var a0:Int = 0

    while(a0 < t){
      var n = sc.nextInt()
      val m = sc.nextInt()
      var a1:Int = 0

      val topper = new Topper(n)

      while(a1 < m){
        var x = sc.nextInt()
        var y = sc.nextInt()

        topper.friend(x-1,y-1)

        a1+=1
      }

      println(topper.top)
      a0+=1
    }
  }
}
