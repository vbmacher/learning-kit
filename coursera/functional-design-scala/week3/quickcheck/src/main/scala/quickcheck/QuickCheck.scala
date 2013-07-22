package quickcheck

import org.scalacheck.Arbitrary._
import org.scalacheck.Prop._
import org.scalacheck.Gen._
import org.scalacheck.{Arbitrary, _}

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {

  lazy val nonempty: Gen[H] = for {
    v <- arbitrary[A]
    m <- genHeap
  } yield insert(v, m)

  lazy val genHeap: Gen[H] = oneOf(const(empty), nonempty)

  implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)

  property("min1") = forAll { a: Int =>
    val h = insert(a, empty)
    findMin(h) == a
  }

  property("min2") = forAll {
    (a: Int, b: Int) =>
      val m = insert(a, insert(b, empty))
      findMin(deleteMin(m)) == math.max(a, b)
  }

  property("min3") = forAll {
    (a: Int, b: Int) =>
      val m = insert(a, insert(b, empty))
      findMin(m) == math.min(a, b)
  }

  property("gen1") = forAll { (h: H) =>
    val m = if (isEmpty(h)) 0 else findMin(h)
    findMin(insert(m, h)) == m
  }

  property("empty1") = forAll { (h: H) =>
    !isEmpty(insert(0, h))
  }

  property("empty2") = forAll { (_: H) =>
    isEmpty(empty)
  }

  property("empty3") = forAll {
    (a: Int) => isEmpty(deleteMin(insert(a, empty)))
  }

  property("del1") = forAll {
    (a: Int, b: Int) =>
      val m = insert(a, insert(b, empty))
      isEmpty(deleteMin(deleteMin(m)))
  }

  property("del2") = forAll {
    (a: Int, b: Int, c: Int) =>
      val m = insert(a, insert(b, insert(c, empty)))

      val min = List(a,b,c).min

      val nextmin = if (min == a) math.min(b, c)
      else if (min == b) math.min(a, c)
      else math.min(a, b)

      findMin(deleteMin(m)) == nextmin
  }

  property("meld1") = forAll {
    (h: H) =>
      meld(h, empty) == h
  }

  property("meld2") = forAll {
    (h: H) =>
      meld(empty, h) == h
  }

  property("meld3") = forAll {
    (a: Int, b:Int) =>
      val h1 = insert(a, empty)
      val h2 = insert(b, empty)

      findMin(meld(h1, h2)) == math.min(a, b)
  }

  property("meld3") = forAll {
    (a: Int, b:Int) =>
      val h1 = insert(a, empty)
      val h2 = insert(b, empty)

      findMin(deleteMin(meld(h1, h2))) == math.max(a, b)
  }


}
