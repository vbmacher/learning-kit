package com.github.vbmacher.diffcode.model

import com.github.vbmacher.diffcode.model.VectorDocument.WordFrequency

trait Document {
  def content: String
}

trait VectorDocument extends Document {

  def vector: WordFrequency

  def dot(vec2: WordFrequency): Double = vector.map {
    case (token, count) => vec2.getOrElse(token, 0) * count
  }.sum

  def magnitude(): Double = math.sqrt(vector.values.map(count => count * count).sum)
}
object VectorDocument {

  type Word = String
  type Count = Int

  type WordFrequency = Map[Word, Count]
}



