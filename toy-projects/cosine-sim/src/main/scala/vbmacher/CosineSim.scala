package vbmacher

trait CosineSim[T] {

  def sim(doc1: Document[T], doc2: Document[T]):Double = {
    val magnitude = doc1.mag() * doc2.mag()
    val magg = if (magnitude == 0) 1 else magnitude

    doc1.dot(doc2.vector) / magg
  }

}

object StringCosineSim extends CosineSim[String]
