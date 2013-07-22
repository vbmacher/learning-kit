
abstract class Nat {

  def isZero: Boolean

  def predecessor: Nat
  def successor: Nat

  def +(t: Nat): Nat
  def -(t: Nat): Nat

}


object Zero extends Nat {
  override def isZero: Boolean = true

  override def predecessor: Nat = throw new Error("0.predecessor")

  override def successor: Nat = new Succ(Zero)

  override def +(t: Nat): Nat = t

  override def -(t: Nat): Nat =
    if (t.isZero) this else throw new Error("0 - nat")

  override def toString = "zero"
}

class Succ(t: Nat) extends Nat {
  override def isZero: Boolean = false

  override def predecessor: Nat = t

  override def successor: Nat = new Succ(this)

  override def +(o: Nat): Nat = new Succ(t + o)

  override def -(o: Nat): Nat =
    if (o == Zero) this else predecessor - o.predecessor


  override def toString = "succ(" + predecessor.toString + ")"
}


def three = new Succ(new Succ(new Succ(Zero)))
def two = new Succ(new Succ(Zero))

two + three

three - two

three - three

two - three

