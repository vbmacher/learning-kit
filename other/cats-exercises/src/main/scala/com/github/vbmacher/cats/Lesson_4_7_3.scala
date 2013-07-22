package com.github.vbmacher.cats

import cats.data.Writer
import cats.instances.vector._
import cats.syntax.writer._
import cats.syntax.applicative._

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.duration._

object Lesson_4_7_3 extends App {

  def slowly[A](body: => A) =
    try body finally Thread.sleep(100)

  type Logged[A] = Writer[Vector[String], A]


  def factorial(n: Int): Logged[Int] = {
    for {
      ans <- if (n == 0) {
        1.pure[Logged]
      } else {
        factorial(n - 1).map(_ * n)
      }
      _ <- Vector(s"fact $n $ans").tell
    } yield ans
  }

  val result = Await.result(Future.sequence(Vector(
    Future(factorial(5)),
    Future(factorial(5))
  )).map(_.map(_.written)), 5.seconds)

  println(result)
}
