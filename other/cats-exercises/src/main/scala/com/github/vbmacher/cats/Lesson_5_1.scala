package com.github.vbmacher.cats

import cats.Monad


object Lesson_5_1 {

  import cats.syntax.applicative._ // for pure
  import cats.syntax.flatMap._ // for pure
  import cats.syntax.monad._ // for pure

  // Hypothetical example. This won't actually compile:
//  def compose[M1[_]: Monad, M2[_]: Monad] = {
//    type Composed[A] = M1[M2[A]]
//
//    new Monad[Composed] {
//      def pure[A](a: A): Composed[A] =
//        a.pure[M2].pure[M1]
//
//      def flatMap[A, B](fa: Composed[A])
//        (f: A => Composed[B]): Composed[B] = {
//      // Problem! How do we write flatMap?
//
//        fa.flatMap {
//          m2 =>
//            m2.flatMap {
//              a => f(a) // M1[M2[B]]; but expects: M2[B]
//            } // M2[B]; but expects: M1[M2[B]]
//
//        }
//
//      }
//
//      override def tailRecM[A, B](a: A)(f: A => Composed[Either[A, B]]): Composed[B] = ???
//    }
//  }

//  def compose[M1[_]: Monad, M2[_]: Option : Monad] = {
//    type Composed[A] = M1[M2[A]]
//
//    new Monad[Composed] {
//      def pure[A](a: A): Composed[A] =
//        a.pure[M2].pure[M1]
//
//      // for option
//      def flatMap[A, B](fa: Composed[A])
//        (f: A => Composed[B]): Composed[B] = {
//        faa.flatMap(_.fold[Composed[B]](None.pure[M1])(f))
//      }
//
//      override def tailRecM[A, B](a: A)(f: A => Composed[Either[A, B]]): Composed[B] = ???
//    }
//  }

}
