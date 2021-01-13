package com.github.vbmacher.cats

import cats.Monad
import cats.syntax.flatMap._
import cats.syntax.functor._

import scala.annotation.tailrec

object Lesson_4_10_1 extends App {

  sealed trait Tree[+A]

  final case class Branch[A](left: Tree[A], right: Tree[A])
    extends Tree[A]

  final case class Leaf[A](value: A) extends Tree[A]

  def branch[A](left: Tree[A], right: Tree[A]): Tree[A] =
    Branch(left, right)

  def leaf[A](value: A): Tree[A] =
    Leaf(value)


  implicit val treeMonad: Monad[Tree] = new Monad[Tree] {
    override def flatMap[A, B](fa: Tree[A])(f: A => Tree[B]): Tree[B] = {
      fa match {
        case Leaf(value) => f(value)
        case Branch(left, right) => branch(flatMap(left)(f), flatMap(right)(f))
      }
    }

    override def pure[A](x: A): Tree[A] = leaf(x)

    // TailRecM should recursively call itself until the result of `f` returns a Right
    override def tailRecM[A, B](a: A)(f: A => Tree[Either[A, B]]): Tree[B] = {

      @tailrec
      def loop(open: List[Tree[Either[A, B]]], closed: List[Option[Tree[B]]]): List[Tree[B]] = {
        open match {
          case Branch(l, r) +: rest =>
            loop(l :: r :: rest, None :: closed)

          case Leaf(Left(value)) :: rest =>
            loop(f(value) :: rest, closed)

          case Leaf(Right(value)) :: rest =>
            loop(rest, Some(leaf(value)) :: closed)

          case Nil =>
            closed.foldLeft(List.empty[Tree[B]]) {
              case (acc, tree) =>
                tree.map(_ :: acc).getOrElse {
                  val l :: r :: tail = acc
                  branch(l, r) :: tail
                }
            }
        }
      }

      loop(List(f(a)), Nil).head

      //      flatMap(f(a)) {
      //        case Left(value) => tailRecM(value)(f)
      //        case Right(value) => Leaf(value)
      //      }
    }
  }


  val tree = branch(
    branch(leaf(1), leaf(2)),
    branch(
      branch(leaf(4), leaf(5)),
      leaf(7)
    )
  )

  println(tree)
  println(tree.map(_ + 1))

  val l = leaf(5)

  val result = for {
    a <- tree
    b <- l
  } yield a + b
  println(result)

  println(tree.tailRecM[Tree, Boolean](tree => tree.map(v => Right[Tree[Int], Boolean](true))))


}
