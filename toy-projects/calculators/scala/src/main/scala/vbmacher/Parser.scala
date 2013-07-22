package vbmacher

trait Parser[A] {

  def parse(s: String): List[(A, String)]

  // Functor!
  def map[B](f: A => B): Parser[B] = s => for {
    (a, rest) <- parse(s)
  } yield (f(a), rest)

  // Monad!
  def flatMap[B](f: A => Parser[B]): Parser[B] = s => for {
    (a, rest) <- parse(s)
    bs <- f(a).parse(rest)
  } yield bs

  // Alternative!
  def or[B >: A](second: Parser[B]): Parser[B] = s => {
    val firstParsed = parse(s)
    if (firstParsed.nonEmpty) firstParsed else second.parse(s)
  }

  def ||[B >: A](second: Parser[B]): Parser[B] = or(second)

  def oneOrMore(): Parser[List[A]] = for {
    a <- this
    next <- zeroOrMore()
  } yield a :: next


  def zeroOrMore(): Parser[List[A]] = oneOrMore() || Parser.unit(Nil)

  def satisfy(f: A => Boolean): Parser[A] = s => {
    for {
      (a, rest) <- parse(s)
      if f(a)
    } yield (a, rest)
  }

  def chainl(p: Parser[A => A]): Parser[A] = flatMap(a => p.map(f => f(a)).chainl(p)) || this

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