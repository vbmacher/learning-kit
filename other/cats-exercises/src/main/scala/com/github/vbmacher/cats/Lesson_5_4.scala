package com.github.vbmacher.cats

import cats.data.EitherT

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object Lesson_5_4 extends App {

  type Response[A] = EitherT[Future, String, A]

  val powerLevels = Map(
    "Jazz"      -> 6,
    "Bumblebee" -> 8,
    "Hot Rod"   -> 10
  )

  def getPowerLevel(autobot: String): Response[Int] = {
    // weird
    powerLevels.get(autobot) match {
      case None => EitherT.left(Future(s"$autobot unreachable"))
      case Some(powerLevel) => EitherT.right(Future(powerLevel))
    }
  }

  def canSpecialMove(ally1: String, ally2: String): Response[Boolean] = {
    val combined = for {
      power1 <- getPowerLevel(ally1)
      power2 <- getPowerLevel(ally2)
    } yield power1 + power2

    combined.map(_ > 15)
  }

  def tacticalReport(ally1: String, ally2: String): String = {
    Await.result(canSpecialMove(ally1, ally2).value, 10.seconds) match {
      case Left(msg) => s"Comms error: $msg"
      case Right(true) => s"$ally1 and $ally2 are ready to roll out!"
      case Right(false) => s"$ally1 and $ally2 need a recharge."
    }
  }

  println(tacticalReport("Jazz", "Bumblebee"))
  // res13: String = "Jazz and Bumblebee need a recharge."
  println(tacticalReport("Bumblebee", "Hot Rod"))
  // res14: String = "Bumblebee and Hot Rod are ready to roll out!"
  println(tacticalReport("Jazz", "Ironhide"))
  // res15: String = "Comms error: Ironhide unreachable"
}
