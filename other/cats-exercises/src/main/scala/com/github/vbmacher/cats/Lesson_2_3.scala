package com.github.vbmacher.cats

import cats.Eq

import cats.syntax.eq._

object Lesson_2_3 extends App {

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

  def associativeLaw[A](a: A, b: A, c: A)(implicit m: Monoid[A], e: Eq[A]) = {
    m.combine(m.combine(a, b), c) === m.combine(a, m.combine(b, c))
  }

  def identityLaw[A](a: A)(implicit m: Monoid[A], e: Eq[A]) = {
    m.combine(m.empty, a) === m.combine(a, m.empty)
  }

  // how many boolean monoids exists?
  val xorMonoid = new Monoid[Boolean] {
    override def empty: Boolean = false
    override def combine(x: Boolean, y: Boolean): Boolean = x ^ y
  }
  val orMonoid = new Monoid[Boolean] {
    override def empty: Boolean = false
    override def combine(x: Boolean, y: Boolean): Boolean = x || y
  }
  val andMonoid = new Monoid[Boolean] {
    override def empty: Boolean = true
    override def combine(x: Boolean, y: Boolean): Boolean = x && y
  }
  val nxorMonoid = new Monoid[Boolean] {
    override def empty: Boolean = true
    override def combine(x: Boolean, y: Boolean): Boolean = !(x ^ y)
  }

  def test(msg: String, m: Monoid[Boolean]): Unit = {
    val a = true
    val b = false
    val c = true
    implicit val mon: Monoid[Boolean] = m

    println(msg)
    println("associative = " + associativeLaw(a, b, c))
    println("identity = " + identityLaw(a))
  }

  test("xor", xorMonoid)
  test("or", orMonoid)
  test("and", andMonoid)
  test("nxor", nxorMonoid)
}
