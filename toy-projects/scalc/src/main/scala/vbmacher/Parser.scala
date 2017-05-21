package vbmacher

trait Parser[A] {

  def apply(s: String): List[(A, String)]

  // Functor!
  def map[B](f: A => B): Parser[B] = s => for {
    (a, rest) <- this (s)
  } yield (f(a), rest)

  // Monad!
  def flatMap[B](f: A => Parser[B]): Parser[B] = s => for {
    (a, rest) <- this (s)
    bs <- f(a)(rest)
  } yield bs

  // Alternative!
  def or[B >: A](second: Parser[B]): Parser[B] = s => {
    val first = this(s)
    if (first.nonEmpty) first else second(s)
  }

  def oneOrMore(): Parser[List[A]] = for {
    a <- this
    next <- zeroOrMore()
  } yield a :: next


  def zeroOrMore(): Parser[List[A]] = oneOrMore().or(Parser.unit(Nil))

  def satisfy(f: A => Boolean): Parser[A] = s => {
    for {
      (a, rest) <- this (s)
      if f(a)
    } yield (a, rest)
  }

  def foldLeft(p: Parser[A => A]): Parser[A] = {
    flatMap(a => p.map(f => f(a)).foldLeft(p)).or(this)
  }

}

object Parser {

  def unit[A](a: A): Parser[A] = s => List((a, s))

  def empty[A]: Parser[A] = s => Nil

  def item(): Parser[Char] = s => if (s.isEmpty) Nil else List((s.head, s.tail))

  def symbol(s: Char): Parser[Char] = item().satisfy(_ == s)

  def plainNumber(): Parser[Int] = for {
    numbers <- item().satisfy(_.isDigit).oneOrMore()
  } yield numbers.mkString.toInt

  def number(): Parser[Int] = (for {
    c <- symbol('-')
    n <- plainNumber()
  } yield -1 * n).or(plainNumber())
}