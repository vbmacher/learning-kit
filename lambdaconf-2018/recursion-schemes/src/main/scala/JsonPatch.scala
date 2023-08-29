package lc2018

import jto.validation.jsonast.JValue

sealed trait Operation
case object Add     extends Operation
case object Remove  extends Operation
case object Replace extends Operation

sealed trait Position
final case class Field(name: String) extends Position
final case class Index(value: Int)   extends Position
//final case class Last(pos: Position) extends Position
case object End extends Position
final case class JsonPatch(op: Operation, path: List[Position], value: JValue)
