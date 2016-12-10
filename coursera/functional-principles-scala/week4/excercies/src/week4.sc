package idealized.scala

abstract class Boolean {

  def ifThenElse[T](t: => T, e: => T): T

  def &&[T](t: => Boolean) = ifThenElse[T](t, false)

  def ||[T](t: => Boolean) = ifThenElse[T](true, t)

  def unary_! : Boolean = ifThenElse[Boolean](false, true)

  def <[T](t: => Boolean) = ifThenElse[Boolean](false, t)
}


object true extends Boolean {

  def ifThenElse[T](t: => Boolean, e: => Boolean): T = t

}

object false extends Boolean {

  def ifThenElse[T](t: => Boolean, e: => Boolean): T = e
}