package com.github.vbmacher.diffcode.model

import com.github.vbmacher.diffcode.model.CodeDocument._
import com.github.vbmacher.diffcode.model.VectorDocument.WordFrequency

import scala.util.parsing.input.{Position, Positional}

trait CodeDocument extends VectorDocument {

  def parse(): Seq[Token]

  override lazy val vector: WordFrequency = {
    parse().map {
      case token: NamedToken => token.name
      case token => token.getClass.getSimpleName
    }.groupBy(tokenText => tokenText).mapValues(_.size)
  }
}

object CodeDocument {
  case class SyntaxError(message: String, pos: Position) extends Exception(s"$message, position: $pos")

  trait Token extends Positional
  trait NamedToken extends Token { def name: String }

}