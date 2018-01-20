package com.github.vbmacher.similarity

import scala.language.postfixOps
import scala.util.parsing.combinator.lexical.Lexical
import scala.util.parsing.input.CharArrayReader.EofCh

trait Document {
  type Frequency = Map[String, Int]

  def content: String

  def vector: Frequency

  def dot(vec2: Frequency): Double = vector.map {
    case (token, count) => vec2.getOrElse(token, 0) * count
  }.sum

  def magnitude(): Double = math.sqrt(vector.values.map(count => count * count) sum)
}

class StringDocument(override val content: String) extends Document {

  override val vector: Frequency = content.split("\\s").map((_, 1)).groupBy(_._1).mapValues(_.length)

}
object StringDocument {
  def apply(fileName: String): StringDocument = {
    val content = scala.io.Source.fromFile(fileName).getLines() mkString "\n"
    new StringDocument(content)
  }
}

