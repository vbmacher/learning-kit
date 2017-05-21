package vbmacher

trait Expr {
  def eval: Int
}

case class Add(b: Expr)(a: Expr) extends Expr {
  def eval: Int = a.eval + b.eval
}
case class Sub(b: Expr)(a: Expr) extends Expr {
  def eval: Int = a.eval - b.eval
}
case class Mul(b: Expr)(a: Expr) extends Expr {
  def eval: Int = a.eval * b.eval
}
case class Div(b: Expr)(a: Expr) extends Expr {
  def eval: Int = a.eval / b.eval
}
case class Lit(n: Int) extends Expr {
  def eval: Int = n
}

trait Grammar {
  import Parser._

  // start -> factor { '+'|'-' factor }
  def start(): Parser[Expr] = {
    factor().foldLeft(plusFactor().or(minusFactor()))
  }

  def plusFactor(): Parser[Expr => Expr] = for {
    _ <- symbol('+')
    e <- factor()
  } yield Add(e)

  def minusFactor(): Parser[Expr => Expr] = for {
    _ <- symbol('-')
    e <- factor()
  } yield Sub(e)


  // factor -> term { '*'|'/' term }
  def factor(): Parser[Expr] = {
    term().foldLeft(mulTerm().or(divTerm()))
  }

  def mulTerm(): Parser[Expr => Expr] = for {
    _ <- symbol('*')
    e <- term()
  } yield Mul(e)

  def divTerm(): Parser[Expr => Expr] = for {
    _ <- symbol('/')
    e <- term()
  } yield Div(e)


  // term -> N | '(' start ')'
  def term(): Parser[Expr] = {
    (for {n <- number()} yield Lit(n)).or(
      for {
        _ <- symbol('(')
        e <- start()
        _ <- symbol(')')
      } yield e
    )
  }

  def run(l: String): Int = start().parse(l) match {
    case Nil =>
      println(s"Unrecognized string: $l")
      0
    case (a, bs) :: xs =>
      val aEval = a.eval
      if (xs.isEmpty) {
        if (bs.isEmpty) aEval
        else {
          println(s"Unrecognized string: $bs")
          aEval
        }
      } else {
        println("Multiple solutions?!")
        aEval
      }
  }

}

