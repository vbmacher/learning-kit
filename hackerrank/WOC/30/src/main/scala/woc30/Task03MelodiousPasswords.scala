package woc30

import java.util.Scanner

object Task03MelodiousPasswords {

  private val vowels = "aeiou".toList
  private val consonants = "bcdfghjklmnpqrstvwxz".toList

  def melodiousPasswords(n: Int): List[String] = {
    def vowel(n: Int, word: String): List[String] = {
      if (n == 0) List(word)
      else for {
        v <- vowels
        c <- consonant(n - 1, word + v)
      } yield c
    }

    def consonant(n: Int, word: String): List[String] = {
      if (n == 0) List(word)
      else for {
        c <- consonants
        v <- vowel(n - 1, word + c)
      } yield v
    }

    vowel(n, "") ++ consonant(n, "")
  }


  def main(args: Array[String]): Unit = {
    val n = new Scanner(System.in).nextInt()

    for (password <- melodiousPasswords(n)) {
      println(password)
    }
  }

}
