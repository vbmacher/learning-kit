package com.github.vbmacher.diffcode.similarity

import com.github.vbmacher.diffcode.model.VectorDocument

object Cosine extends Similarity[VectorDocument] {

  override val name: String = "cosine"

  override def compare(doc1: VectorDocument, doc2: VectorDocument): Double = {
    val rawMagnitude = doc1.magnitude() * doc2.magnitude()
    val magnitude = if (rawMagnitude == 0) 1 else rawMagnitude

    doc1.dot(doc2.vector) / magnitude
  }
}
