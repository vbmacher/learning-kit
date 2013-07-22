package csp

import scala.collection.mutable

abstract class Domain[T] {
  def isEmpty(): Boolean
  def size(): Long
}

abstract class Var[U](domain: Domain[U])
case class FreeVar[U](domain: Domain[U]) extends Var[U](domain)
case class SetVar[T,U](domain: Domain[U], value: T) extends Var[U](domain)


class Assignment[U](vars: Seq[Var[U]], relation: (Var[U], Var[U]) => Boolean)
                   (onComplete: Assignment[U] => Unit = _ => ()) {

  private implicit val mostConstrainedOrdering = Ordering.by[Var[U], Long] {
    case FreeVar(domain) => domain.size()
    case SetVar(domain, _) => domain.size()
  }
  private val mostConstrainingOrdering = Ordering.by[Var[U], Long] {
    variable => relatedVars(variable).size
  } reverse

  def complete(): Boolean = {
    val result = !vars.exists {
      case FreeVar(_) => true
    }
    if (result) onComplete(this)
    result
  }

  def relatedVars(variable: Var[U]): Seq[Var[U]] = for {
    second <- vars
    if variable != second
    if relation(variable, second)
  } yield second

  def freeVars()
              (mostConstraining: Ordering[Var[U]] = mostConstrainingOrdering)
              (implicit mostConstrained: Ordering[Var[U]]): Seq[Var[U]] = {
    (vars.filter {
      case FreeVar(_) => true
    } sorted) sorted mostConstraining
  }

}

class CSP[U](findFirst: Boolean = true) {

  private def ac3(assignment: Assignment[U]): Unit = {
    var queue = new mutable.Queue[FreeVar[U]]()



  }

  def solve(assignment: Assignment[U]): Unit = {
    if (assignment.complete() && findFirst) {
      return
    }





    var stack = List()










  }

}
