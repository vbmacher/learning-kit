package com.github.vbmacher.similarity

import scala.language.postfixOps

trait Document {
  type Frequency = Map[String, Int]

  def content: String

  def vector: Frequency

  def dot(vec2: Frequency): Double = vector.map {
    case (token, count) => vec2.getOrElse(token, 0) * count
  }.sum

  def magnitude(): Double = math.sqrt(vector.values.map(count => count * count).sum)
}

class StringDocument(override val content: String) extends Document {

  override val vector: Frequency = {
    content.split("\\s").groupBy(key => key).mapValues(group => group.length)
  }

}
object StringDocument {
  def apply(fileName: String): StringDocument = {
    val content = scala.io.Source.fromFile(fileName).getLines() mkString "\n"
    new StringDocument(content)
  }
}

