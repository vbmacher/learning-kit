package com.github.vbmacher.cats

import cats.instances.list._
import cats.syntax.parallel._

object Lesson_6_4_0_1 extends App {

  val l = (List(1,2),List(3,4),List(5)).parTupled

  // zips it
  println(l)

}
