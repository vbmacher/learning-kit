package lc2018

import matryoshka._
import matryoshka.data._
import matryoshka.implicits._
import scalaz._
import Scalaz._

// My notes:
//   - explicit recursion is bad (mixed concerns, not reusable code, compiler doesn't help much)
//   - Recursion schemes FTW (https://github.com/slamdata/matryoshka)
//   - Families of schemas:
//     - folds (ex. cata) - destroy a structure bottom up,
//     - unfolds (ex. ana) - builds a structure,
//     - refolds (ex. hylo) - combines unfold then fold  - result is a "unstructured" type
//
// Using recursion schemes => never write recursion yourself!!!
// To use any recursion scheme, you need: Functor, Fix-point, Algebra (and/or co-algebra)
//
// Pattern functors - e.g. tree types might be nested (recursive)
// solution: use a fix-point type to "swallow" recursion
//   final case class Fix[F[_]](unFix: F[Fix[F]])
//
// sealed trait TreeF[A]
// final case class Node[A](left: A, right: A) extends TreeF[A]
// final case class Lead[A](label: Int)        extends TreeF[A]
//
//   ????
// implicit val treeFFunctor = new Functor[TreeF] {
//    def map[A,B](fa: TreeF[A])(f: A => B): TreeF[B] = tree match {
//      case Node(left, right) => Node(f(left), f(right))    // <--- non-recursive!! just one layer!!
//      case Leaf(label) => Leaf(f(label))
//    }
// }
//   ????
//
// Algebras: define what to do with a single layer of your recursive structure
//   - algebras: collapse, 1 layer at a time:    F[A] => A
//   - coalgebras: builds up 1 layer at a time: A => F[A]
//
// [A] is referred to as the (co)algebra's "carrier"
//
// Hylomorphism: (refold)
//
//    def hylo[F[_], A, B](a: A)(alg: Algebra[F, B], coalg: Coalebra[F, A]): B =
//       alg(coalg(a) map (hylo(_)(alg, coalg)))
//

/**
  * Let's begin with what's probably the simplest possible recursive structure: natural numbers
  *
  * Natural numbers can be defined recursively:
  * A number is either
  *   - zero, noted `Z`
  *   - the successor of a number, noted `S(n)` where n is the notation of some number
  *
  * This notation is often referred to as the Peano notation.
  */
object PeanoNumbers {

  /**
    * We want to encode Peano numbers as a recursive type.
    * This encoding will be a type constructor, out so-called "pattern-functor"
    *
    * Hint: there is a type in the standard library that has exactly the structure we want.
    */
  type PeanoNumberF[A] = Option[A]

  val numberTwo = Some(Some(None))
  val zero      = None

  /**
    * The problem with the PeanonumberF encoding is that now, different numbers
    * will have different types.
    *
    * We need a fix-point of PeanoNumberF to build a type that can represent all numbers.
    */
  type PeanoNumber = Fix[PeanoNumberF]

  /**
    * Now let's write our very first Algebra! Yay!
    *
    * We want to transform our Peano representation to Int. It's as simple as counting
    * the "layers" of "successor".
    */
  def countLayers: Algebra[PeanoNumberF, Int] = {
    case Some(x) => 1 + x
    case None    => 0
  }

  /**
    * We now have all the ingredients needed to use our first recursion scheme.
    *
    * Hint: this will use the algebra defined above to *destroy* our recursive structure.
    */
  def toInt(peano: PeanoNumber): Int = peano cata countLayers // we need "fold" (== cata)
  // the same as:    Fix.birecursiveT.cataT(peano)(countLayers)

  /**
    * Now we just need a value to test our functions
    */
  // would be possible to write like this?
  //  PeanoNumber(Some(PeanoNumber(Some(PeanoNumber(Some(PeanoNumber(None)))))))
  //
  // using Some takes very long to compile
  // using PeanoNumber as constructor does not work!
  val three: PeanoNumber = Fix(Some(Fix(Some(Fix(Some(Fix[PeanoNumberF](None))))))) // Fix(Option(Fix(Option(Fix(Option(Fix[PeanoNumberF](None)))))))

  assert(toInt(three) == 3)
}

/**
  * We now move on to a more interesting recursive structure: the binary tree.
  */
object BinaryTrees {

  sealed trait Tree
  final case class Branch(label: Int, left: Tree, right: Tree) extends Tree
  final case class Leaf(label: Int)                            extends Tree
  final case class Empty()                                     extends Tree

  /**
    * So the first thing to do is to "translate" our Tree to a pattern-functor.
    * This is done by adding a type parameter and replace each recursive occurrences
    * of Tree by this type parameter in the ADT.
    */
  sealed trait TreeF[+A]
  final case class BranchF[A](label: Int, left: A, right: A) extends TreeF[A]
  final case class LeafF[A](label: Int)                      extends TreeF[A]
  final case class EmptyF()                                  extends TreeF[Nothing]

  /**
    * Of course, we need to have an instance of Functor[TreeF] for it to be a real pattern-functor.
    */
  implicit val treeFFunctor: Functor[TreeF] = new Functor[TreeF] {
    override def map[A, B](fa: TreeF[A])(f: A => B) = fa match {
      case BranchF(label, l, r) => BranchF(label, f(l), f(r))
      case LeafF(label)         => LeafF(label)
      case EmptyF()             => EmptyF()
    }
  }

  /**
    * It's a good idea to have a pair of (co)algebras that go from Tree to TreeF (and vice versa).
    */
  def treeAlg: Algebra[TreeF, Tree] =
    (v1: TreeF[Tree]) =>
      v1 match {
        case BranchF(label, l, r) => Branch(label, l, r)
        case LeafF(label)         => Leaf(label)
        case EmptyF()             => Empty()
    }
  def treeCoalg: Coalgebra[TreeF, Tree] =
    (v1: Tree) =>
      v1 match {
        case Branch(label, l, r) => BranchF(label, l, r)
        case Leaf(label)         => LeafF(label)
        case Empty()             => EmptyF()
    }

  /**
    * These two (co)algebras make it easy to provide a Birecursive instance for Tree/TreeF.
    * This allows to treat Tree as if it were a TreeF, and thus enables to use schemes directly
    * on a Tree (rather than having to wrap it in a fixpoint).
    */
  // Needed to have Recursive.AllOps[] later!!!
  implicit val treeBirecursive: Birecursive.Aux[Tree, TreeF] = Birecursive.fromAlgebraIso(treeAlg, treeCoalg)

  import Recursive.ops._

  /**
    * A function TreeF[List[Int]] => List[Int]
    *
    * The produced list contains the labels of all the nodes in the tree
    * as enumerated by a depth-first, left-to-right traversal.
    */
  def toList: Algebra[TreeF, List[Int]] = {
    case BranchF(label, l, r) => l ::: List(label) ::: r
    case LeafF(label)         => List(label)
    case EmptyF()             => Nil
  }

  val testTree: Recursive.AllOps[Tree, TreeF] = Branch(12, Branch(10, Leaf(1), Empty()), Leaf(15))

  assert(testTree.cata(toList) == List(1, 10, 12, 15))

  /**
    * A function List[Int] => TreeF[List[Int]]
    *
    * This function MUST produce a "sort tree", that is, a tree where each
    * node has a label that is greater than all the labels in its left subtree
    * and lesser than all the labels in its right subtree.
    */
  def fromList: Coalgebra[TreeF, List[Int]] =
    (v1: List[Int]) =>
      v1 match {
        case Nil          => EmptyF()
        case one :: Nil   => LeafF(one)
        case head :: rest =>
//          val (left, right) = rest.sorted.splitAt((head::rest).size / 2)   // <-- median
//          BranchF(left.last, left.init, right)

          val (left, right) = rest.partition(_ < head)
          BranchF(head, left, right)
    }

  /**
    * I wonder what this mystery function does…
    */
  // it's a quicksort  ==> it's a hylomorphism :)
  def mystery(input: List[Int]): List[Int] = input.hylo(toList, fromList)
  /* TESINTG:

  import matryoshka._, implicits._
  import data.Fix, Fix._
  import lc2018.BinaryTrees._

  implicit val listBirec = Birecursive.fromAlgebraIso(toList, fromList)
  val tree = List(1, 2, 3).ana[Fix[TreeF]](fromList)

  // res3: matryoshka.data.Fix[lc2018.BinaryTrees.TreeF] = Fix(BranchF(1,Fix(EmptyF()),Fix(BranchF(2,Fix(EmptyF()),Fix(LeafF(3))))))

  tree.cata(toList)

  // List(1, 2, 3)


   * */
  /*
 * # Recursive
 *   - a way to abstract from fixed points
 *
 *   - project / embed ==> converting between type and pattern-functor
 *
 * trait Recursive[T] {
 *   type Base[A] = ...
 *
 *   def project(t: T): Base[T] = ...
 *
 * }
 *
 * If we want to limit Base[A] we use Aux ????
 *
 * type Aux[T, F] = Recursive[T] { type Base = F }
 *
 * */
}
