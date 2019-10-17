package com.github.vbmacher.diffcode.similarity

import com.github.vbmacher.diffcode.model.Document

object Levenshtein extends Similarity[Document] {

  override val name: String = "levenshtein"

  override def compare(doc1: Document, doc2: Document): Double = {
    def min(args: Int*) = args.min

    val (lenDoc1, lenDoc2) = (doc1.content.length, doc2.content.length)
    val maxDocLength = math.max(lenDoc1, lenDoc2)

    if (min(lenDoc1, lenDoc2) == 0) return 0

    val edits = Array.fill(lenDoc1, lenDoc2)(0)

    for (i <- 1 until lenDoc1) edits(i)(0) = i
    for (j <- 1 until lenDoc2) edits(0)(j) = j

    for (i <- 1 until lenDoc1; j <- 1 until lenDoc2) {
      val cost = if (doc1.content(i) == doc2.content(j)) 0 else 1

      edits(i)(j) = min(
        edits(i)(j - 1) + 1, // char at j is "extra"
        edits(i - 1)(j) + 1, // char at i is "extra"
        edits(i - 1)(j - 1) + cost // are chars at i and j different?
      )
    }

    // normalization
    1 - edits(lenDoc1 - 1)(lenDoc2 - 1).toDouble / maxDocLength.toDouble
  }
}
