package com.github.vbmacher.cats

object Lesson1 extends App {

  // library ...
  sealed trait Printable[A] {
    def format(value: A): String
  }
  object Printable {

    def format[A](value: A)(implicit printable: Printable[A]): String = printable.format(value)

    def print[A](value: A)(implicit printable: Printable[A]): Unit = {
      println(format(value))
    }
  }

  object PrintableInstances {

    implicit val printableInt = new Printable[Int] {
      override def format(value: Int): String = value.toString
    }
    implicit val printableString = new Printable[String] {
      override def format(value: String): String = value
    }
  }

  object PrintableSyntax {

    implicit class PrintableOps[A](value: A) {
      def format(implicit printable: Printable[A]): String = printable.format(value)
      def print(implicit printable: Printable[A]): Unit = Printable.print(value)
    }
  }

  // end of library

  object Application {
    import PrintableInstances._

    final case class Cat(name: String, age: Int, color: String)

    implicit val printableCat = new Printable[Cat] {
      override def format(value: Cat): String = {
        s"${Printable.format(value.name)} is a ${Printable.format(value.age)} year-old ${Printable.format(value.color)} cat."
      }
    }

    def run() = {
      val cat = Cat("Miccy", 2, "brown")
      Printable.print(cat)

      import PrintableSyntax._
      cat.print
    }
  }


  Application.run()
}
