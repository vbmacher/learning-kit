package vbmacher

import scala.language.postfixOps

trait Document[T] {
  type Frequency = Map[T, Int]

  val vector: Frequency

  def dot(vec2: Frequency): Double = vector.map {
    case (word, count) => if (vec2 contains word) count * vec2(word) else 0
  }.sum

  def mag(): Double = math.sqrt(vector.map { case (_, c) => math.pow(c, 2) } sum)
}

class StringDocument(content: String) extends Document[String] {
  override val vector: Frequency = content.split("\\s")
    .map((_, 1))
    .groupBy(p => p._1)
    .map(p => (p._1, p._2.length))
}
object StringDocument {
  def apply(fileName: String): StringDocument = {
    val content = scala.io.Source.fromFile(fileName).getLines() mkString "\n"
    new StringDocument(content)
  }

}
