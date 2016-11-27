object example {

  def sum(f: Int => Int)(a : Int, b:Int): Int = {

    def loop(v: Int, acc: Int) : Int = {
      if (v > b) {
        return acc
      }
      loop(v + 1, acc + f(v))
    }

    loop(a, 0)
  }

  def fact(a:Int) :Int =
    if (a == 0) 1
    else a * fact(a-1)

  sum(x => x)(1,3)
  sum(x => x*x*x)(1,3)
  sum(fact)(1,5)


}