package com.github.vbmacher.cats

import cats.Monoid
import cats.instances.int._
import cats.instances.option._
import cats.syntax.semigroup._

object Lesson_2_5_4 extends App {

  def add[A](items: List[A])(implicit monoid: Monoid[A]): A = items.foldLeft(monoid.empty)(_ |+| _)

  println(add(List(1, 2, 3)))
  println(add(List(Some(1), None, Some(2), None, Some(3))))

  // no implicits found for Monoid[Some[Int]]
  // println(add(List(Some(1), Some(2), Some(3))))

  case class Order(totalCost: Double, quantity: Double)

  implicit val orderMonoid = new Monoid[Order] {
    override def empty: Order = Order(0, 0)
    override def combine(x: Order, y: Order): Order = Order(x.totalCost + y.totalCost, x.quantity + y.quantity)
  }

  println(add(List(Order(1,3), Order(1,4), Order(5,7))))
}
