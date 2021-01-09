package com.github.vbmacher.cats

object Lesson_4_1_2 extends App {

  trait Monad[F[_]] {
    def pure[A](a: A): F[A]

    def flatMap[A, B](value: F[A])(func: A => F[B]): F[B]

    def map[A, B](value: F[A])(func: A => B): F[B] =
      flatMap(value)(v => pure(func(v)))
  }

}
