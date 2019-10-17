package com.github.vbmacher.diffcide.similarity

import com.github.vbmacher.diffcode.documents.StringDocument
import com.github.vbmacher.diffcode.similarity.Cosine
import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CosineSpec extends FlatSpec {


  "Comparison of empty and non-empty document" should "give result 0" in {
    val emptyDoc = StringDocument("")
    val nonEmptyDoc = StringDocument("Hello, world!")

    assert(Cosine.compare(emptyDoc, nonEmptyDoc) === 0)
    assert(Cosine.compare(nonEmptyDoc, emptyDoc) === 0)
  }


  "Comparison of equal documents" should "give result 1" in {
    val text = "Hello, world!"
    val doc1 = StringDocument(text)
    val doc2 = StringDocument(text)

    assert(Cosine.compare(doc1, doc2).round === 1)
    assert(Cosine.compare(doc2, doc1).round === 1)
  }


}
