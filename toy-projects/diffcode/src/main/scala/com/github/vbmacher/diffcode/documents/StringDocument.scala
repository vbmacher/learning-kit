package com.github.vbmacher.diffcode.documents

import java.io.File

import com.github.vbmacher.diffcode.model.VectorDocument
import com.github.vbmacher.diffcode.model.VectorDocument.WordFrequency

import scala.io.Source

class StringDocument(override val content: String) extends VectorDocument {

  override val vector: WordFrequency = {
    content.split("\\s").groupBy(key => key).mapValues(group => group.length)
  }

}
object StringDocument {

  def apply(content: String): StringDocument = new StringDocument(content)

  def apply(file: File): StringDocument = {
    val source = Source.fromFile(file)
    val content = try source.getLines() mkString "\n" finally source.close()

    StringDocument(content)
  }
}
