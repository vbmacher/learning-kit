package com.github.vbmacher.cats

import cats.data.Reader
import cats.syntax.applicative._

object Lesson_4_8_3 extends App {

  final case class Db(
    usernames: Map[Int, String],
    passwords: Map[String, String]
  )

  type DbReader[A] = Reader[Db, A]

  def findUsername(userId: Int): DbReader[Option[String]] = Reader {
    (db: Db) => db.usernames.get(userId)
  }

  def checkPassword(
    username: String,
    password: String): DbReader[Boolean] = Reader {
    (db: Db) => db.passwords.get(username).contains(password)
  }

  def checkLogin(userId: Int, password: String): DbReader[Boolean] =
    for {
      userName <- findUsername(userId)
      result <- userName.map(checkPassword(_, password)).getOrElse(false.pure[DbReader])
    } yield result


  val vbmacherCheck = checkLogin(1, "password")
  println(vbmacherCheck.run(Db(
    Map(1 -> "vbmacher"),
    Map("vbmacher" -> "password")
  )))
}
