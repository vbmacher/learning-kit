package recfun

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
    * Exercise 1
    */
  def pascal(c: Int, r: Int): Int =
    if (c == 0 || c == r) 1
    else pascal(c - 1, r - 1) + pascal(c, r - 1)

  /**
    * Exercise 2
    */
  def balance(chars: List[Char]): Boolean = {
    def count(cs: List[Char], opened: Int): Int = {
      if (cs.isEmpty) {
        return opened
      }

      cs.head match {
        case '(' => count(cs.tail, opened + 1)
        case ')' if (opened > 0) => count(cs.tail, opened - 1)
        case ')' => 1
        case _ => count(cs.tail, opened)
      }
    }

    count(chars, 0) == 0
  }

  /**
    * Exercise 3
    */
  def countChange(money: Int, coins: List[Int]): Int = {
    if (money < 0 || coins.isEmpty) return 0
    if (money == 0) return 1

    countChange(money - coins.head, coins) + countChange(money, coins.tail)
  }
}
