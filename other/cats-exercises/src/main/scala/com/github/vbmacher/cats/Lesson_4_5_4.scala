package com.github.vbmacher.cats

import cats.MonadError
import cats.syntax.applicative._

import scala.util.Try

object Lesson_4_5_4 extends App {

  def validateAdult[F[_]](age: Int)(implicit me: MonadError[F, Throwable]): F[Int] =
    me.ensure(age.pure[F])(new IllegalArgumentException("Age must be greater than or equal to 18"))(_ >= 18)


  println(validateAdult[Try](18))
  // res7: Try[Int] = Success(18)
  println(validateAdult[Try](8))
  // res8: Try[Int] = Failure(
  //   java.lang.IllegalArgumentException: Age must be greater than or equal to 18
  // )
  type ExceptionOr[A] = Either[Throwable, A]
  println(validateAdult[ExceptionOr](-1))
  // res9: ExceptionOr[Int] = Left(
  //   java.lang.IllegalArgumentException: Age must be greater than or equal to 18
  // )
}
