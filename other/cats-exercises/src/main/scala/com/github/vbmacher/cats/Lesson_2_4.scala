package com.github.vbmacher.cats

import cats.{Eq, Semigroup}
import cats.syntax.eq._

object Lesson_2_4 extends App {

  trait Semigroup[A] {
    def combine(x: A, y: A): A
  }

  trait Monoid[A] extends Semigroup[A] {
    def empty: A
  }
  object Monoid {
    def apply[A](implicit monoid: Monoid[A]): Monoid[A] =
      monoid
  }

  def associativeLaw[A](a: A, b: A, c: A)(implicit m: Semigroup[A], e: Eq[A]) = {
    m.combine(m.combine(a, b), c) === m.combine(a, m.combine(b, c))
  }

  def identityLaw[A](a: A)(implicit m: Monoid[A], e: Eq[A]) = {
    m.combine(m.empty, a) === m.combine(a, m.empty)
  }

  // all monoids/semigroups for sets?

  // empty set
  // set of all sets
  // non-empty set
  //
  // union (addition)
  // intersection
  // exclusive intersection? union - intersection
  // difference (subtraction) -> is not associative

  object unionMonoid {
    implicit def setUnionMonoid[A]: Monoid[Set[A]] = {
      new Monoid[Set[A]] {
        override def empty: Set[A] = Set.empty
        override def combine(x: Set[A], y: Set[A]): Set[A] = x union y
      }
    }
    implicit val intUnionMonoid = Monoid[Set[Int]]
  }

  object symmetricDiffMonoid {
    implicit def symmetricDiff[A]: Monoid[Set[A]] = {
      new Monoid[Set[A]] {
        override def empty: Set[A] = Set.empty
        override def combine(x: Set[A], y: Set[A]): Set[A] = (x union y) diff (x intersect y)
      }
    }

    implicit val symmDiffMonoid = Monoid[Set[Int]]
  }

  implicit def setIntersectionSemigroup[A]: Semigroup[Set[A]] = {
    (x: Set[A], y: Set[A]) => x intersect y
  }


  implicit val intIntersectionSemigroup = Semigroup[Set[Int]]

  val set1 = Set(1,2,3)
  val set2 = Set(2,3,4,5)
  val set3 = Set(6,7)

  {
    import unionMonoid._
    println("union identity:" + identityLaw(set1))
    println("union associative:" + associativeLaw(set1, set2, set3))
  }

  {
    import symmetricDiffMonoid._
    println("symdiff identity:" + identityLaw(set1))
    println("symdiff associative:" + associativeLaw(set1, set2, set3))
  }

  println("intersection associative:" + associativeLaw(set1, set2, set3))

}
