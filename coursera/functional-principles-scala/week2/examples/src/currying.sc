object example {

  def product(f: Int=> Int)(a: Int, b:Int) : Int= {
    if (a > b) 1
    else f(a) * product(f)(a+1, b)
  }

  def fact(a : Int) = product(x=>x)(1, a)
  fact(5)

  def both(unit: Int, op: (Int, Int) => Int)(f: Int => Int)(a: Int, b: Int) : Int =
    if (a > b) unit
    else op(f(a), both(unit, op)(f)(a+1, b))


  def sum(f: Int => Int)(a: Int, b: Int) = both(0, (x, y) => x+y)(f)(a,b)

  def pproduct(f: Int => Int)(a: Int, b: Int) = both(1, (x,y) => x*y)(f)(a,b)


  sum(x=>x)(1,3)
  

}