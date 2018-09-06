package vbmacher

object Scalc extends Grammar {

  def main(args: Array[String]): Unit = {
    println("Welcome to scalc!")
    print("> ")
    for (line <- scala.io.Source.stdin.getLines()) {
      println(run(line))
      print("> ")
    }

  }

}
