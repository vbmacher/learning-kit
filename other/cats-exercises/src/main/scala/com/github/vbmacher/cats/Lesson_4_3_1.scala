package com.github.vbmacher.cats

import cats.{Id, Monad}
import cats.syntax.functor._ // for map
import cats.syntax.flatMap._ // for flatMap


object Lesson_4_3_1 extends App {

  def sumSquares[A[_] : Monad](fx: A[Int], fy: A[Int]): A[Int] =
    fx.flatMap(x => fy.map(y => x*x + y*y))

  def pure[A](value: A): Id[A] = value

  def flatMap[A, B](monad: Id[A])(f: A => Id[B]): Id[B] = f(monad)

  def map[A, B](monad: Id[A])(f: A => B): Id[B] = f(monad)


  println(sumSquares(4: Id[Int], 4: Id[Int]))
}
