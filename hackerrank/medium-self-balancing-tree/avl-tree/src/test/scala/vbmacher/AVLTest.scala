package vbmacher

import org.scalatest.FunSuite


class AVLTest extends FunSuite {

  test("insert 3 values") {
    val avl = new AVL()
    avl.insert(1)
    avl.insert(2)
    avl.insert(3)

    val node = avl.tree.asInstanceOf[Node]

    assert(node.value === 2)
    assert(node.l.asInstanceOf[Node].value == 1)
    assert(node.r.asInstanceOf[Node].value == 3)
  }

  test("insert 5 values") {
    val avl = new AVL()
    avl.insert(1)
    avl.insert(2)
    avl.insert(3)
    avl.insert(4)
    avl.insert(5)

    val node = avl.tree.asInstanceOf[Node]

    assert(node.value === 2)
    assert(node.l.asInstanceOf[Node].value === 1)
    assert(node.r.asInstanceOf[Node].value === 4)

    val rnode = node.r.asInstanceOf[Node]
    assert(rnode.l.asInstanceOf[Node].value == 3)
    assert(rnode.r.asInstanceOf[Node].value == 5)
  }

  test("insert 6 values") {
    val avl = new AVL()
    avl.insert(1)
    avl.insert(2)
    avl.insert(3)
    avl.insert(4)
    avl.insert(5)
    avl.insert(6)

    val node = avl.tree.asInstanceOf[Node]

    assert(node.value === 4)
    assert(node.l.asInstanceOf[Node].value === 2)
    assert(node.r.asInstanceOf[Node].value === 5)

    val lnode = node.l.asInstanceOf[Node]
    assert(lnode.l.asInstanceOf[Node].value === 1)
    assert(lnode.r.asInstanceOf[Node].value === 3)

    val rnode = node.r.asInstanceOf[Node]
    assert(rnode.r.asInstanceOf[Node].value === 6)
  }

  test("insert 7 values") {
    val avl = new AVL()
    avl.insert(1)
    avl.insert(2)
    avl.insert(3)
    avl.insert(4)
    avl.insert(5)
    avl.insert(6)
    avl.insert(7)

    println(avl)

    val node = avl.tree.asInstanceOf[Node]

    assert(node.value === 4)
    assert(node.l.asInstanceOf[Node].value === 2)
    assert(node.r.asInstanceOf[Node].value === 6)

    val lnode = node.l.asInstanceOf[Node]
    assert(lnode.l.asInstanceOf[Node].value === 1)
    assert(lnode.r.asInstanceOf[Node].value === 3)

    val rnode = node.r.asInstanceOf[Node]
    assert(rnode.l.asInstanceOf[Node].value === 5)
    assert(rnode.r.asInstanceOf[Node].value === 7)
  }

}