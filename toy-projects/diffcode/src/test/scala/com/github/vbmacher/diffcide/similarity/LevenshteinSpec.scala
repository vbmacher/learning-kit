package com.github.vbmacher.diffcide.similarity

import com.github.vbmacher.diffcode.documents.StringDocument
import com.github.vbmacher.diffcode.similarity.Levenshtein
import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class LevenshteinSpec extends FlatSpec {

  "Comparison of empty and non-empty document" should "give result 0" in {
    val emptyDoc = StringDocument("")
    val nonEmptyDoc = StringDocument("Hello, world!")

    assert(Levenshtein.compare(emptyDoc, nonEmptyDoc) === 0)
    assert(Levenshtein.compare(nonEmptyDoc, emptyDoc) === 0)
  }

  "Comparison of equal documents" should "give result 1" in {
    val text = "Hello, world!"
    val doc1 = StringDocument(text)
    val doc2 = StringDocument(text)

    assert(Levenshtein.compare(doc1, doc2).round === 1)
    assert(Levenshtein.compare(doc2, doc1).round === 1)
  }

}
