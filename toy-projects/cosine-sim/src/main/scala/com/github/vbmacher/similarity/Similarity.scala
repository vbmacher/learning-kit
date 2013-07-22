package com.github.vbmacher.similarity

object Similarity {

  def cosine(doc1: Document, doc2: Document):Double = {
    val magnitude = doc1.magnitude() * doc2.magnitude()
    val magg = if (magnitude == 0) 1 else magnitude

    val dot = doc1.dot(doc2.vector)
    val dot2 = doc2.dot(doc1.vector)

    println(s"dot 1,2 = $dot; magg = $magg, sim = ${dot / magg}")
    println(s"dot 2,1 = $dot2; magg = $magg, sim = ${dot2 / magg}")

    dot / magg
  }

  def levenhstein(doc1: Document, doc2: Document): Double = {
    def min(args: Int*) = args.min

    val (len1, len2) = (doc1.content.length, doc2.content.length)
    val maxLength = math.max(len1, len2)
    if (min(len1, len2) == 0) return maxLength

    val matrix = Array.fill(len1, len2)(0)

    for (i <- 1 until len1) matrix(i)(0) = i
    for (j <- 1 until len2) matrix(0)(j) = j


    for (i <- 1 until len1; j <- 1 until len2) {
      val cost = if (doc1.content(i) == doc2.content(j)) 0 else 1

      matrix(i)(j) = min(matrix(i)(j - 1) + 1, matrix(i - 1)(j) + 1, matrix(i - 1)(j - 1) + cost)
    }

    1 - matrix(len1 - 1)(len2 - 1).toDouble / maxLength.toDouble
  }

}
