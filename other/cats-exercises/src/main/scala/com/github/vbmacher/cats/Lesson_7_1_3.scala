package com.github.vbmacher.cats

import cats.Monoid

object Lesson_7_1_3 extends App {
  // List's map, flatMap, filter, and sum methods in terms of foldRight.

  def map[A, B](ls: List[A])(f: A => B): List[B] = {
    ls.foldRight(List.empty[B]) { case (item, acc) => f(item) :: acc }
  }

  def flatMap[A, B](ls: List[A])(f: A => List[B]): List[B] = {
    ls.foldRight(List.empty[B]) { case (item, acc) => f(item) ::: acc }
  }

  def filter[A](ls: List[A])(f: A => Boolean): List[A] = {
    ls.foldRight(List.empty[A]) {
      case (item, acc) if f(item) => item :: acc
      case (_, acc) => acc
    }
  }

  def sum[A: Numeric](ls: List[A]): A = {
    ls.foldRight(Numeric[A].zero) {
      case (item, acc) => Numeric[A].plus(item, acc)
    }
  }

  def sumMonoid[A: Monoid](ls: List[A]): A = {
    ls.foldRight(Monoid.empty[A])(Monoid.combine)
  }


  println(map(List(1, 2, 3))(_ * 2))
  println(flatMap(List(1, 2, 3))(a => List(a, a * 10, a * 100)))
  println(filter(List(1, 2, 3))(_ % 2 == 1))
  println(sum(List(1, 2, 3)))
  println(sumMonoid(List(1, 2, 3)))
}
