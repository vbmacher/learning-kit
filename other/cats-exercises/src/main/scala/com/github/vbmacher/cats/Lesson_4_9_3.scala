package com.github.vbmacher.cats

import cats.data.State

object Lesson_4_9_3 extends App {

  type CalcState[A] = State[List[Int], A]

  def evalOne(sym: String): CalcState[Int] = {

    def binary(op: (Int, Int) => Int) = {
      State[List[Int], Int] {
        stack =>
          val b +: a +: rest = stack
          val result = op(a, b)
          (result +: rest) -> result
      }
    }

    def operand(num: Int) = {
      State[List[Int], Int] {
        stack => (num +: stack) -> num
      }
    }

    sym match {
      case "+" => binary(_ + _)
      case "-" => binary(_ - _)
      case "*" => binary(_ * _)
      case "/" => binary(_ / _)
      case number => operand(number.toInt)
    }
  }

  def evalAll(input: List[String]): CalcState[Int] = {
    input.foldLeft(State.empty[List[Int], Int]) {
      case (acc, element) => acc.flatMap {
        _ => evalOne(element)
      }
    }
  }

  def evalInput(input: String): Int = {
    evalAll(input.split(" ").toList).runA(Nil).value
  }



  println(evalOne("42").runA(Nil).value)

  val program = for {
    _   <- evalOne("1")
    _   <- evalOne("2")
    ans <- evalOne("+")
  } yield ans

  println(program.runA(Nil).value)


  val multistageProgram = evalAll(List("1", "2", "+", "3", "*"))
  println(multistageProgram.runA(Nil).value)


  val biggerProgram = for {
    _   <- evalAll(List("1", "2", "+"))
    _   <- evalAll(List("3", "4", "+"))
    ans <- evalOne("*")
  } yield ans

  println(biggerProgram.runA(Nil).value)


  println(evalInput("1 2 + 3 4 + *"))
}
