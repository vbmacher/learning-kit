package com.github.vbmacher.cats

object Lesson_3_5_5_1 extends App {

  trait Printable[A] { self =>

    def format(value: A): String

    def contramap[B](func: B => A): Printable[B] =
      new Printable[B] {
        def format(value: B): String = self.format(func(value))
      }
  }

  def format[A](value: A)(implicit p: Printable[A]): String = p.format(value)

  case class Dog(name: String)
  case class Car(name: String)

  implicit val pcar = new Printable[Car] {
    override def format(value: Car): String = s"Car ${value.name}"
  }
  implicit val pdog = pcar.contramap[Dog](d => Car(d.name))

  val car = Car("timmy")
  val dog = Dog("haha")

  println(format(car))
  println(format(dog))


  object another {
    implicit val stringPrintable: Printable[String] =
      new Printable[String] {
        def format(value: String): String =
          s"'${value}'"
      }

    implicit val booleanPrintable: Printable[Boolean] =
      new Printable[Boolean] {
        def format(value: Boolean): String =
          if(value) "yes" else "no"
      }
  }

  import another._
  println(format("hello"))
  println(format(true))


  final case class Box[A](value: A)

  // contravariant functor is useful only if Printable[A] alone can be used
  // to represent Box[A]. So we convert from Box[A] => A using contramap function
  // and then work only with A.
  // If Box[A] had more values, they'd get either lost or must be comehow "squashed" into A.
  //
  // map: appends operation:          (A => B)  map (B => C)  map (C => X)  : A => X
  // contramap: prepends operation:   (C => X) cmap (B => C) cmap (A => B)  : A => X
  implicit def pbox[A](implicit p: Printable[A]): Printable[Box[A]] = {
    p.contramap[Box[A]](_.value)
  }

  format(Box("hello world"))
  format(Box(true))
}
