package vbmacher

trait Tree {
  def height: Int = 0

  override def toString: String = "_"
}

case class Node(value: Int, l: Tree, r: Tree, override val height: Int) extends Tree {
  override def toString: String = s"N[$value;$height ($l, $r)]"
}
case object End extends Tree


class AVL {
  type Situation = (Int, Int)
  private val LL = (-1, -1)
  private val LR = (-1, 1)
  private val RR = (1, 1)
  private val RL = (1, -1)

  var tree: Tree = End

  private def balance(node: Node, situation: Situation): (Node, Int) = {
    val balanceFactor = node.l.height - node.r.height

    if (balanceFactor >= -1 && balanceFactor <= 1) {
      return (node, situation._1)
    }
    if (situation == RR) (rotateLeft(node), 0)
    else if (situation == LL) (rotateRight(node), 0)
    else if (situation == RL) (rotateRightLeft(node), 0)
    else if (situation == LR) (rotateLeftRight(node), 0)
    else throw new IllegalStateException("no way!")
  }

  private def rotateLeft(node: Node): Node = node.r match {
    case Node(v, l, r, h) => Node(v, Node(node.value, node.l, l, h), r, node.height)
  }


  private def rotateRight(node: Node): Node = node.l match {
    case Node(v, l, r, h) => Node(v, l, Node(node.value, r, node.r, h), node.height)
  }

  private def rotateRightLeft(node: Node): Node = node.r match {
    case Node(v, l, r, h) => l match {
      case Node(lv, ll, lr, lh) =>
        Node(lv, Node(node.value, node.l, ll, node.height + 1), Node(v, lr, r, h), lh - 2)
    }
  }

  private def rotateLeftRight(node: Node): Node = node.l match {
    case Node(v, l, r, h) => r match {
      case Node(rv, rl, rr, rh) =>
        Node(rv, Node(v, l, rl, h), Node(node.value, rr, node.r, node.height + 1), rh - 2)
    }
  }

  def insert(value: Int) {

    def insertAndBalance(parent: Tree): (Tree, Int) = parent match {
      case End => (Node(value, End, End, 1), 0)
      case orig@Node(v, l, r, h) =>
        if (v > value) {
          val (n, grandchild) = insertAndBalance(l)
          balance(Node(v, n, r, h + 1), (-1, grandchild))
        } else if (v < value) {
          val (n, grandchild) = insertAndBalance(r)
          balance(Node(v, l, n, h + 1), (1, grandchild))
        } else (orig, 0)
    }

    tree = insertAndBalance(tree)._1
  }

  override def toString: String = tree.toString

}


