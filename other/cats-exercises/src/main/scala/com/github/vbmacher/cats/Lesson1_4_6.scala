package com.github.vbmacher.cats

import cats._
import cats.implicits._

object Lesson1_4_6 extends App {

  final case class Cat(name: String, age: Int, color: String)

  implicit val catShow = Show.show[Cat](value => {
    s"${value.name.show} is a ${value.age.show} year-old ${value.color.show} cat."
  })

  val cat = Cat("Miccy", 2, "brown")

  println(cat.show)
}
