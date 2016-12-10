object XX {

  trait Expr {

    def eval(): Int = this match {
        case Number(n) => n
        case Prod(e1, e2) => e1.eval() * e2.eval()
        case Sum(e1, e2) => e1.eval + e2.eval
    }

    def show(): String = this match {
      case Number(n) => n.toString
      case Sum(e1, e2) => e1.show() + "+" + e2.show()
      case Prod(e1, e2) => (e1 match {
        case r@Sum(_, _) => "(" + r.show + ")"
        case x => x.show
      }) + "*" + (e2 match {
        case r@Sum(_, _) => "(" + r.show + ")"
        case x => x.show
      })
    }
  }

  case class Number(n: Int) extends Expr {

    def apply(n: Int): Number = new Number(n)

  }

  case class Sum(e1: Expr, e2: Expr) extends Expr {

    def apply(e1: Expr, e2: Expr) = new Sum(e1, e2)
  }

  case class Prod(e1: Expr, e2: Expr) extends Expr {
    def apply(e1: Expr, e2: Expr): Prod = new Prod(e1, e2)
  }


  Sum(Number(5), Number(3)).eval()

  Sum(Number(5), Sum(Number(2), Number(3))).show


  Sum(Prod(Number(5), Number(2)), Number(3)).show

  Prod(Number(5), Sum(Number(2), Number(3))).show

}