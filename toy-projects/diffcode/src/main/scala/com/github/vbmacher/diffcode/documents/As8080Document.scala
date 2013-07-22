package com.github.vbmacher.diffcode.documents

import java.io.File

import com.github.vbmacher.diffcode.documents.As8080Document._
import com.github.vbmacher.diffcode.model.CodeDocument
import com.github.vbmacher.diffcode.model.CodeDocument.{NamedToken, SyntaxError, Token}

import scala.io.Source
import scala.util.parsing.combinator.RegexParsers

object As8080Document {

  case class Instruction(name: String) extends NamedToken
  case class Preprocessor(name: String) extends NamedToken
  case class Register(name: String) extends NamedToken
  case class Separator(name: String) extends NamedToken
  case class Operator(name: String) extends NamedToken

  case class Literal(name: String) extends NamedToken
  case class Identifier(name: String) extends Token // Not NamedToken - we ignore identifier names
  case class Label(name: String) extends Token // Not NamedToken - we ignore label names

  def apply(content: String): As8080Document = new As8080Document(content)

  def apply(file: File): As8080Document = {
    val source = Source.fromFile(file)
    val content = try source.getLines() mkString "\n" finally source.close()

    As8080Document(content)
  }

}
class As8080Document(override val content: String) extends CodeDocument with RegexParsers {

  override def parse(): Seq[CodeDocument.Token] = {
    (parseAll(program, content) match {
      case Success(result, _) => result
      case NoSuccess(msg, next) => throw SyntaxError(msg, next.pos)
    }).filter {
      case x: Separator => false
      case _ => true
    }

  }

  private final val instructions = Set(
    "stc", "cmc", "inr", "dcr", "cma", "daa", "nop", "mov", "stax", "ldax", "add", "adc", "sub", "sbb", "ana", "xra",
    "ora", "cmp", "rrc", "rlc", "ral", "rar", "push", "pop", "dad", "inx", "dcx", "xchg", "xthl", "sphl", "lxi", "mvi",
    "adi", "aci", "sui", "sbi", "ani", "xri", "ori", "cpi", "sta", "lda", "shld", "lhld", "pchl", "jmp", "jc", "jnc",
    "jz", "jnz", "jp", "jm", "jpe", "jpo", "call", "cc", "cnc", "cz", "cnz", "cp", "cm", "cpe", "cpo", "ret", "rc",
    "rnc", "rz", "rnz", "rm", "rm", "rpe", "rpo", "rst", "ei", "di", "in", "out", "hlt"
  )
  private final val preprocessors = Set(
    "org", "equ", "set", "include", "if", "endif", "macro", "endm", "db", "dw", "ds", "$"
  )
  private final val registers = Set("a", "b", "c", "d", "e", "h", "l", "m", "psw", "sp")
  private final val separators = Set("(", ")", ",")
  private final val operators = Set("+", "-", "*", "/", "=", "mod", "shr", "shl", "and", "or", "xor", "not")

  override def skipWhitespace = false

  override val whiteSpace = "[\\n\\r \\t\\f]+".r

  // Parser start non-terminal
  def program: Parser[Seq[Token]] = skip ~> rep(token <~ skip) <~ eof


  private def comment = ";[^\\r\\n]*".r

  private def skip = rep(whiteSpace | comment) ^^^ Unit


  private def eof = "\\z".r | failure("unexpected character")

  private def token = positioned(
    identifier ||| instruction ||| preprocessor ||| register ||| separator ||| operator ||| literal ||| label
  )

  private def instruction = oneOf(instructions) ^^ Instruction
  private def preprocessor = oneOf(preprocessors) ^^ Preprocessor
  private def register = oneOf(registers) ^^ Register
  private def separator = oneOf(separators) ^^ Separator
  private def operator = oneOf(operators) ^^ Operator

  private def literal = {
    def decimal = "[0-9]+[dD]?".r
    def octal = "[0-7]+[oOqQ]".r
    def hexa = "[0-9][0-9a-fA-F]*[hH]".r
    def binary = "[0-1]+[bB]".r
    def string = "'[^'\\n\\r]+'".r

    (decimal ||| octal ||| hexa ||| binary ||| string) ^^ Literal
  }

  private def identifier = "([a-zA-Z_\\?@])[a-zA-Z_\\?@0-9]*".r ^^ Identifier

  private def label = "([a-zA-Z_\\?@])[a-zA-Z_\\?@0-9]*\\:".r ^^ Label

  private def oneOf(strings: Set[String]) = {
    strings map (a => a:Parser[String]) reduce(_ ||| _)
  }

}
