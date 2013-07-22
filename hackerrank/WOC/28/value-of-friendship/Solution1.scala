import scala.collection.mutable.ArrayBuffer

object Solution {

  abstract class Component

  case class Node(var root: Root) extends Component {
    override def toString: String = "Node to %d".format(root.index)
  }
  case class Root(index: Int, children: ArrayBuffer[Node] = new ArrayBuffer(),
                  pairs: ArrayBuffer[(Int, Int)] = new ArrayBuffer()) extends Component {
    override def toString: String = "Root(%d) with children: %s".format(index, children)
  }

  type Graph = Array[Component]

  class Topper(n: Int) {
    val graph: Graph = Array.tabulate(n)(i => Root(i))
    var total: Long = 0
    var friends: Long = 0

    def friend(i: Int, j: Int): Unit = {
      def mkPair(i: Int, j: Int): Unit = {
        val root = Root(i)
        val node = Node(root)
        val pair = (i,j)
        root.children += node
        root.pairs += pair

        graph(i) = root
        graph(j) = node

        friends += 2L
      }

      def rootToNode(oldRoot: Root, newRoot: Root, i: Int, newPair: (Int, Int)): Unit = {
        minusFriends(oldRoot, newRoot)

        val node = Node(newRoot)
        graph(i) = node
        newRoot.children ++= oldRoot.children
        newRoot.pairs ++= oldRoot.pairs

        for (c <- oldRoot.children) {
          c.root = newRoot
        }
        newRoot.children += node
        newRoot.pairs += newPair

        plusFriends(newRoot)
      }

      def minusFriends(ri: Root, rj: Root): Unit = {
        var cnti:Long = (1L+ri.children.length) * ri.children.length.asInstanceOf[Long]
        var cntj:Long = (1L+rj.children.length) * rj.children.length.asInstanceOf[Long]
        friends -= (cnti + cntj)
      }

      def plusFriends(ri: Root): Unit = {
        var cnti:Long = (1L +ri.children.length) * ri.children.length.asInstanceOf[Long]

        friends += cnti
      }

      val vi = graph(i)
      val vj = graph(j)

      vi match {
        case Root(_, ci, pi) => vj match {
          case Root(_, cj, pj) =>
            if (ci.isEmpty && cj.isEmpty) mkPair(i, j)
            else {
              val (ri, rj) = (vi.asInstanceOf[Root], vj.asInstanceOf[Root])
              if (ri != rj) {
                if (ci.length <= cj.length) rootToNode(ri, rj, i, (i, j))
                else rootToNode(rj, ri, j, (i, j))
              } else ri.pairs += ((i,j))
            }
          case Node(rj) =>
            val ri = vi.asInstanceOf[Root]
            if (ri != rj) {
              if (ri.children.length <= rj.children.length) rootToNode(ri, rj, i, (i, j))
              else rootToNode(rj, ri, rj.index, (i, j))
            } else ri.pairs += ((i,j))
        }
        case Node(ri) => vj match {
          case Root(_, cj, pj) =>
            val rj = vj.asInstanceOf[Root]
            if (ri != rj) {
              if (rj.children.length <= ri.children.length) rootToNode(rj, ri, j, (i, j))
              else rootToNode(ri, rj, ri.index, (i, j))
            } else ri.pairs += ((i,j))
          case Node(rj) =>
            if (ri != rj) {
              if (ri.children.length <= rj.children.length) rootToNode(ri, rj, ri.index, (i, j))
              else rootToNode(rj, ri, rj.index, (i, j))
            } else ri.pairs += ((i,j))
        }
      }

      total += friends
    }
  }



  def main(args: Array[String]) {
    val sc = new java.util.Scanner(System.in)
    val t = sc.nextInt()
    var a0: Int = 0

    while (a0 < t) {
      var n = sc.nextInt()
      val m = sc.nextInt()
      var a1: Int = 0

      var topper = new Topper(n)

      while (a1 < m) {
        var x = sc.nextInt()
        var y = sc.nextInt()

        topper.friend(x - 1, y - 1)

        a1 += 1
      }


      val ps = topper.graph.filter(_.isInstanceOf[Root]).map(_.asInstanceOf[Root].pairs).sortBy(_.length).reverse

      topper = new Topper(n)

      for (pairs <- ps) {
        for (pair <- pairs) {
          topper.friend(pair._1, pair._2)
        }
      }

      println(topper.total)
      a0 += 1
    }
  }
}
