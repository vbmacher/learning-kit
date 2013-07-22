package com.github.vbmacher.similarity

import scala.language.postfixOps

object Main {

  def main(args: Array[String]): Unit = {
    args combinations 2 map {
      arr => {
        val doc1 = StringDocument(arr(0)) // JavaDocument(arr(0))
        val doc2 = StringDocument(arr(1)) // JavaDocument(arr(1))

        (arr, "cosine=" + Similarity.cosine(doc1, doc2) + ", levenhstein=" + Similarity.levenhstein(doc1, doc2))
      }
    } foreach {
      case (fileNames, similarity) => println(fileNames.mkString(",") + " -> " + similarity)
    }
  }

}