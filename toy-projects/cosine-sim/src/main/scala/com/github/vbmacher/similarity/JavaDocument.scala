package com.github.vbmacher.similarity

import com.github.vbmacher.similarity.JavaDocument._

import scala.language.{implicitConversions, postfixOps}
import scala.util.parsing.combinator.JavaTokenParsers
import scala.util.parsing.input.{Position, Positional}


object JavaDocument {
  case class SyntaxError(msg: String, pos: Position) extends Exception(msg + ", pos: " + pos)

  abstract sealed class Token extends Positional
  sealed trait NameLike { def name: String }
  sealed trait ValueLike { def value: String }

  case class CharLiteral(value: String) extends Token //with ValueLike
  case class StringLiteral(value: String) extends Token //with ValueLike
  case class IntegerLiteral(value: String) extends Token with ValueLike
  case class FloatLiteral(value: String) extends Token with ValueLike
  case class BooleanLiteral(value: String) extends Token with ValueLike
  case class NullLiteral(value: String) extends Token with ValueLike

  case class Identifier(name: String) extends Token with NameLike
  case class Keyword(name: String) extends Token with NameLike
  case class Separator(name: String) extends Token with NameLike
  case class Operator(name: String) extends Token with NameLike

  def apply(fileName: String): JavaDocument = {
    val content = scala.io.Source.fromFile(fileName).getLines() mkString "\n"

    new JavaDocument(content)
  }

}

class JavaDocument(override val content: String) extends JavaTokenParsers with Document {
  override val vector: Frequency = {

    val tokens = parseAll(program, content) match {
      case Success(result, _) => result
      case NoSuccess(msg, next) => throw SyntaxError(msg, next.pos)
    }

    val vec = tokens
      .filter({
        case x : Separator => false
        case _ => true
      }).map({
      case x: NameLike => x.name
      case x: ValueLike => x.value
      case x => x.getClass.getSimpleName
    }).groupBy(c => c).mapValues(_ => 1) //_.size)

    println(vec)
    vec
  }


  // Below is implemented Java Lexer according to:
  // https://docs.oracle.com/javase/specs/jls/se7/html/jls-3.html#jls-3.9

  override def skipWhitespace = false


  def program: Parser[List[Token]] = skip ~> rep(token <~ skip) <~ eof

  def skip: Parser[Unit] = rep(whiteSpace | comment) ^^^ Unit

  def eof: Parser[String] = "\\z".r | failure("unexpected character")

  def token: Parser[Token] = positioned(identifierOrKeyword | literal | separator | operator)

  def comment: Parser[Unit] = {
    def singleComment = "//" ~ rep(not("\n") ~ ".".r) ^^^ ()
    def multilineComment = "/*" ~ rep(not("*/") ~ """(?s).""".r) ~ "*/" ^^^ ()

    singleComment | multilineComment
  }


  def identifierOrKeyword: Parser[Token] = {
    val keywords = Set(
      "abstract", "continue", "for", "new", "switch", "assert", "default", "if", "package", "synchronized",
      "boolean", "do", "goto", "private", "this", "break", "double", "implements", "protected", "throw",
      "byte", "else", "import", "public", "throws", "case", "enum", "instanceof", "return", "transient",
      "catch", "extends", "int", "short", "try", "char", "final", "interface", "static", "void",
      "class", "finally", "long", "strictfp", "volatile", "const", "float", "native", "super", "while"
    )

    ident ^^ { id => if (keywords.contains(id)) Keyword(id) else Identifier(id) }
  }

  def literal: Parser[Token] = {
    def integerLit = integer ^^ IntegerLiteral
    def floatLit = float ^^ FloatLiteral
    def booleanLit = boolean ^^ BooleanLiteral
    def charLit = character ^^ CharLiteral
    def stringLit = string ^^ StringLiteral
    def nullLit = "null".r ^^ NullLiteral

    integerLit | floatLit | booleanLit | charLit | stringLit | nullLit
  }

  def separator: Parser[Token] = ("(" | ")" | "{" | "}" | "[" | "]" | ";" | "," | ".") ^^ Separator

  def operator: Parser[Token] = (
    ">>>=" | ">>>" | ">>=" | ">>" | "<<=" | "<<" | ">=" | "<=" | "%=" | "^=" | "|=" | "&=" | "/=" | "*=" | "-=" |
      "+=" | "++" | "--" | "||" | "&&" | "!=" | "==" | "=" | ">" | "<" | "!" | "~" | "?" | ":" | "+" | "-" | "*" |
      "/" | "&" | "|" | "^" | "%" | "@"
    ) ^^ Operator

  def nonZeroDigit: Parser[String] = """[1-9]""".r
  def hexDigit: Parser[String] = """[0-9a-fA-F]""".r
  def octalDigit: Parser[String] = """[0-7]""".r
  def binDigit: Parser[String] = """[01]""".r

  def digits: Parser[String] = """\d|(\d[\d_]*\d)""".r
  def octalDigits: Parser[String] = """[0-7]|([0-7][0-7_]*[0-7])""".r
  def binDigits: Parser[String] = """[01]|([01][01_]*[01])""".r
  def hexDigits: Parser[String] = """[0-9a-fA-F]|([0-9a-fA-F][0-9a-fA-F_]*[0-9a-fA-F])""".r

  def decimalNumeral: Parser[String] = {
    "0" | ((nonZeroDigit ~ "_*".r) ~ digits.? ^^ { case p ~ r ~ s => p + r + s.getOrElse("") })
  }
  def octalNumeral: Parser[String] = "0_*".r ~ octalDigits ^^ { case p ~ q => p + q }
  def binNumeral: Parser[String] = "0[bB]".r ~ binDigits ^^ { case p ~ q => p + q }
  def hexNumeral: Parser[String] = "0[xX]".r ~ hexDigits ^^ { case p ~ q => p + q }

  def integer: Parser[String] = {
    (decimalNumeral | hexNumeral | octalNumeral | binNumeral) ~ "[lL]?".r ^^ { case p~q => p + q }
  }

  def float: Parser[String] = {
    def hexFloatDigits = (
      hexDigits
      | hexDigits ~ "." ^^ { case p ~ q => p + q }
      | hexDigits.? ~ "." ~ hexDigits ^^ { case p ~ q ~ r => p.getOrElse("") + q + r }
      )
    def hexSignificand = """-?0[xX]""".r ~ hexFloatDigits ^^ { case p ~ q => p + q }
    def hexFloat = hexSignificand ~ """[pP][+-]?""".r ~ digits ^^ { case p ~ q ~ r => p + q + r }

    ("""-?(\d+(\.\d*)?|\d*\.\d+)([eE][+-]?\d+)?""".r | hexFloat) ~ """[fFdD]?""".r ^^ { case p ~ q => p + q }
  }

  def boolean: Parser[String] = """true|false""".r

  def character: Parser[String] = """'([^'\\]|\\([btnfr"'\\]|[0-7]|([0-7][0-7])|([0-3][0-7][0-7])))'""".r

  def string: Parser[String] = """"([^"\\]|\\([btnfr"'\\]|[0-7]|([0-7][0-7])|([0-3][0-7][0-7])))*"""".r

}
