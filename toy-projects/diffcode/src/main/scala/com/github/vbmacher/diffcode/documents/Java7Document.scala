package com.github.vbmacher.diffcode.documents

import java.io.File

import com.github.vbmacher.diffcode.documents.Java7Document._
import com.github.vbmacher.diffcode.model.CodeDocument._
import com.github.vbmacher.diffcode.model.CodeDocument

import scala.io.Source
import scala.util.parsing.combinator.JavaTokenParsers

object Java7Document {

  case class CharLiteral(name: String) extends Token
  case class StringLiteral(name: String) extends Token
  case class IntegerLiteral(name: String) extends NamedToken
  case class FloatLiteral(name: String) extends NamedToken
  case class BooleanLiteral(name: String) extends NamedToken
  case class NullLiteral(name: String) extends NamedToken

  case class Identifier(name: String) extends Token // Not NamedToken - we ignore variable names
  case class Keyword(name: String) extends NamedToken
  case class Operator(name: String) extends NamedToken
  case class Separator(name: String) extends NamedToken


  def apply(content: String): Java7Document = new Java7Document(content)

  def apply(file: File): Java7Document = {
    val source = Source.fromFile(file)
    val content = try source.getLines() mkString "\n" finally source.close()

    Java7Document(content)
  }

}

class Java7Document(override val content: String) extends JavaTokenParsers with CodeDocument {

  override def parse(): Seq[Token] = {
    (parseAll(program, content) match {
      case Success(result, _) => result
      case NoSuccess(msg, next) => throw SyntaxError(msg, next.pos)
    }).filter {
      case x: Separator => false
      case _ => true
    }
  }

  // Below is implemented Java Lexer according to:
  // https://docs.oracle.com/javase/specs/jls/se7/html/jls-3.html#jls-3.9

  private final val keywords = Set(
    "abstract", "continue", "for", "new", "switch", "assert", "default", "if", "package", "synchronized",
    "boolean", "do", "goto", "private", "this", "break", "double", "implements", "protected", "throw",
    "byte", "else", "import", "public", "throws", "case", "enum", "instanceof", "return", "transient",
    "catch", "extends", "int", "short", "try", "char", "final", "interface", "static", "void",
    "class", "finally", "long", "strictfp", "volatile", "const", "float", "native", "super", "while"
  )

  private final val separators = Set("(", ")", "{", "}", "[", "]", ";", ",", ".")
  private final val operators = Set(
    ">>>=", ">>>", ">>=", ">>", "<<=", "<<", ">=", "<=", "%=", "^=", "|=", "&=", "/=", "*=", "-=", "+=", "++", "--",
    "||", "&&", "!=", "==", "=", ">", "<", "!", "~", "?", ":", "+", "-", "*", "/", "&", "|", "^", "%", "@"
  )

  override def skipWhitespace = false

  // Parser start non-terminal
  def program: Parser[Seq[Token]] = skip ~> rep(token <~ skip) <~ eof

  private def skip = rep(whiteSpace | comment) ^^^ Unit

  private def eof = "\\z".r | failure("unexpected character")

  private def token = positioned(identifierOrKeyword | literal | separator | operator)

  private def comment = {
    def singleLine = "//".r ~ rep(not("\n") ~ ".".r) ^^^ Unit
    def multiLine = "/*".r ~ rep(not("*/") ~ """(?s).""".r) ~ "*/" ^^^ Unit

    singleLine | multiLine
  }

  private def identifierOrKeyword = {
    ident ^^ { id => if (keywords.contains(id)) Keyword(id) else Identifier(id) }
  }

  private def literal = {
    def integerLit = integer ^^ IntegerLiteral
    def floatLit = float ^^ FloatLiteral
    def booleanLit = boolean ^^ BooleanLiteral
    def charLit = character ^^ CharLiteral
    def stringLit = string ^^ StringLiteral
    def nullLit = "null".r ^^ NullLiteral

    integerLit | floatLit | booleanLit | charLit | stringLit | nullLit
  }

  private def separator = oneOf(separators) ^^ Separator
  private def operator = oneOf(operators) ^^ Operator

  private def digits = """\d|(\d[\d_]*\d)""".r
  private def hexDigits = """[0-9a-fA-F]|([0-9a-fA-F][0-9a-fA-F_]*[0-9a-fA-F])""".r


  private def integer = {
    def octalDigits = """[0-7]|([0-7][0-7_]*[0-7])""".r
    def binDigits = """[01]|([01][01_]*[01])""".r

    def decimalNumeral = "0".r | ("[1-9]_*".r ~ digits.? ^^ { case p ~ q => p + q.getOrElse("") })
    def hexNumeral = "0[xX]".r ~ hexDigits ^^ { case p ~ q => p + q }
    def octalNumeral = "0_*".r ~ octalDigits ^^ { case p ~ q => p + q }
    def binNumeral = "0[bB]".r ~ binDigits ^^ { case p ~ q => p + q }

    (decimalNumeral | hexNumeral | octalNumeral | binNumeral) ~ "[lL]?".r ^^ { case p ~ q => p + q }
  }

  private def float = {
    def hexFloatDigits = (
      hexDigits
        | hexDigits ~ "\\.".r ^^ { case p ~ q => p + q }
        | hexDigits.? ~ "\\.".r ~ hexDigits ^^ { case p ~ q ~ r => p.getOrElse("") + q + r }
      )
    def hexSignificand = """-?0[xX]""".r ~ hexFloatDigits ^^ { case p ~ q => p + q }
    def hexFloat = hexSignificand ~ """[pP][+-]?""".r ~ digits ^^ { case p ~ q ~ r => p + q + r }

    ("""-?(\d+(\.\d*)?|\d*\.\d+)([eE][+-]?\d+)?""".r | hexFloat) ~ """[fFdD]?""".r ^^ { case p ~ q => p + q }
  }

  private def boolean = """true|false""".r

  private def character = """'([^'\\]|\\([btnfr"'\\]|[0-7]|([0-7][0-7])|([0-3][0-7][0-7])))'""".r

  private def string = """"([^"\\]|\\([btnfr"'\\]|[0-7]|([0-7][0-7])|([0-3][0-7][0-7])))*"""".r

  private def oneOf(strings: Set[String]) = strings map literal reduce(_ ||| _)
}
