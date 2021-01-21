package com.github.vbmacher.cats

object Lesson_7_1_2 extends App {

  println(List(1,2,3).foldLeft(List.empty[Int]) {
    case (acc, item) => item :: acc
  })

  println(List(1,2,3).foldRight(List.empty[Int]) {
    case (item, acc) => item :: acc
  })
}
