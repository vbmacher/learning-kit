package vbmacher

import scala.language.postfixOps

object Main {

  def main(args: Array[String]) = {
    val sims = args combinations 2 map {
      arr => {
        val doc1 = StringDocument(arr(0))
        val doc2 = StringDocument(arr(1))
        (arr, StringCosineSim.sim(doc1, doc2))
      }
    } foreach {
      case (fileNames, similarity) => println(fileNames.mkString(",") + " -> " + similarity)
    }
  }

}