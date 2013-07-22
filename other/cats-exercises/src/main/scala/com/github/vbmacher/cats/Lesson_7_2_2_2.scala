package com.github.vbmacher.cats

import com.github.vbmacher.cats.Lesson_7_2_2_1.listTraverse

object Lesson_7_2_2_2 extends App {

  def process(inputs: List[Int]) =
    listTraverse(inputs)(n => if(n % 2 == 0) Some(n) else None)

  println(process(List(2, 4, 6)))
  println(process(List(1, 2, 3)))
}
