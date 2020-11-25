package com.github.vbmacher.cats

import cats.syntax.eq._
import cats._

object Lesson_1_5_5 extends App {

  final case class Cat(name: String, age: Int, color: String)

  implicit val eqCat = Eq.instance[Cat] {
    (cat1, cat2) =>
      cat1.age === cat2.age && cat1.color === cat2.color && cat1.name === cat2.name
  }

  val cat1 = Cat("Garfield",   38, "orange and black")
  val cat2 = Cat("Heathcliff", 33, "orange and black")

  val optionCat1 = Option(cat1)
  val optionCat2 = Option.empty[Cat]


  println(cat1 === cat2)
  println(cat1 =!= cat2)

  println(optionCat1 === optionCat2)
  println(optionCat1 =!= optionCat2)
}
