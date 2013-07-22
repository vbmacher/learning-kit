package com.github.vbmacher.cats

import cats.Eval

object Lesson_4_6_5 extends App {

  def foldRight[A, B](as: List[A], acc: B)(fn: (A, B) => B): B =
    foldRightEval(as, Eval.now(acc)) {
      case (a, eb) => eb.map(b => fn(a, b))
    }.value


  def foldRightEval[A, B](as: List[A], acc: Eval[B])(fn: (A, Eval[B]) => Eval[B]): Eval[B] = {
    as match {
      case Nil => acc
      case head :: tail =>
        Eval.defer(fn(head, foldRightEval(tail, acc)(fn)))
    }
  }


  println(foldRight((1 to 100000).toList, 0L)(_ + _))
}
