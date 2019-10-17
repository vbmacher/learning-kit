package lambdaconf.introfp

// Learning material:
// https://gist.github.com/jdegoes/97459c0045f373f4eaf126998d8f65dc
// https://leanpub.com/fpmortals/read

// FP-only scala:
// https://github.com/scalaz/scalazzi

// Type-classes:
// https://github.com/mpilquist/simulacrum

// Lenses:
// https://ro-che.info/articles/2016-02-03-finally-tagless-boilerplate


import java.time.Instant

import scala.io.StdIn
import scala.language.{higherKinds, reflectiveCalls}
import scala.util.Random

object functions {

  object totality {
    // function: mapping from domain to codomain. Every value in domain must be covered (total).
    // partial function (non-total): not all values in domain are covered.

    // advantage: no runtime exceptions
    // helps us program without fear


    def nonTotal1(i: Int) = i match {
      case 0 => "YES"
    }

    def nonTotal2(s: String) = s.toInt

    def total2(i: Int) = i.toString

    def nonTotal3(s: String): String = null // if we ignore Scala feature, "null" is not a String => non total
    def total4(s: String): Unit = ()

    def nonTotal5(s: String): Nothing = ??? // Nothing is a "bottom type" => it's empty set (no values) therefore there's no way how to make this to be a function

    // TODO: is infinity-loop total function?
    def maybeTotal = {
      while (true) {} // is it a "bottom" ??
      4
    }
  }

  object determinism {
    // determinism:
    // if: F(a1) = b1 ^ F(a2) = b2 ^ a1 = a2  => b1 = b2
    //    q: referential transparency??

    // advantage: helps testing

    // () => Long
    def nonDeterministic1() = System.nanoTime()

    def nonDeterministic2() = Random.nextInt()

    def deterministic3(x: Int) = {
      try println("Hello world") catch {
        case _: Throwable =>
      }
      x * x
    }
  }

  object purity {
    // purity: function has no side effects - output value depends only on the input, nothing else.
    // purity & determinism is going along: there can't be pure nondeterministic function

    // impurity disadvantages:
    //  - we can't test the side effects
    //  - we can't understand what the function does from looking at its output type

    // advantages:
    //  - output type limits the space of all possible implementations
    //  - it gives the approximation of behavior
    //  - allows equational reasoning  - assignments are not "copying bits", just giving names => substitution

    def impure1(x: Int) = {
      impure3
      x * x
    }

    def impure2: String = StdIn.readLine() // non-total, non-deterministic, impure
    def impure3 = println("Hello world") // non-total => can potentially throw

    def pure4 = "hello"

    def pure5 = {
      pure4
      42
    }

    // transitive: impure functions "infects" all functions which call it (make them impure too)
  }

  def square(x: Int) = x * x // domain: Int, codomain: Int
  def fst[T1, T2](f: (T1, T2)) = f._1 // domain: (T1, T2), codomain: T1
  def str(i: Int) = s"i=$i" // domain: Int, codomain: String

}

object high_order {
  // high order: either takes a function, or return a function, or both
  // combinator:
  //   - takes function(s) and return a function, e.g.: compose
  //   - type constructor function of parameter(s) & return type must be same

  type Error = String
  type Parser[A] = String => Either[Error, (String, A)]

  def or[A](l: Parser[A], r: Parser[A]): Parser[A] = {
    (input: String) =>
      l(input) match {
        case Left(_) => r(input)
        case output => output
      }
  }
}

object polymorphism {

  // Scala does not support polymorphic functions
  // but methods can be polymorphic

  // methods are "faster" than functions (my note: weird..)

  // I can't write true identity function, but I can write identity method
  trait Identity {
    // we use trait here so we can have e.g. `def foo(i: Identity)` instead of `def foo(i: Identity.type)`
    // We could have also:
    //   object identity {}
    //   type identity = identity.type

    // apply: (A: Type) => (A => A)     <-- caller chooses type, callee must be prepared to take any type (universal type)
    // e.g. foo[A,B,C](a,b,c)  <-- two parameters lists - types [A,B,C] and values (a,b,c)

    // parametric polymorphism: use type parameter - parameters can be polymorphic
    def apply[A](a: A): A = a // that's the only one possible implementation
    def apply2[A](a: A): A = a match {
      case "oi" => "dsfgd".asInstanceOf[A] // that's the reason: can't CREATE new A without using reflection
    }

    // other kinds of polymorphism:
    //   - subtype polymorphism (in OOP - Dog, Cat, ... is Animal)
    //   - adhoc polymorphism
  }

  object identity extends Identity

  // "approximate" polymorphic functions
  identity(1) // 1
  identity("SSS") // "SSS"


  trait First {
    def apply[A, B](t: (A, B)): A = t._1
  }

  object First extends First

  trait Second {
    def apply[A, B](t: (A, B)): B = t._2
  }

  object Second extends Second


  // pass-around as a function:
  def x[A, B](f: ((A, B)) => A, v: (A, B)): A = f(v)

  x[Int, Int](First.apply, (1, 2)) // And not x(First, (1,2))

  // pass-around as type:
  def passAround(f: First): String = f(("45", "66"))
}

// Product types vs. Sum types
// Product type - projection, cartesian product, e.g. case class
// Sum type - one type which covers both at once, e.g. Either[A,B]

// Sum types - e.g. Either[Boolean, Boolean] or sealed trait -> size of the set is 4 (two for each Boolean)
// SQL has just Product types, not Sum types (except NULL - you can have "int or NULL" which is kind of Sum type)

// Sum types in Scala can be created using "sealed trait"

object types {

  object products {

    case class Person(name: String, age: Int) // The types are "isomorphic"
    type Person2 = (String, Int)

    def personToPerson2(p: Person) = (p.name, p.age)

    def person2ToPerson(p: Person2) = Person(p._1, p._2)

  }

  object sums {

    sealed trait Lunch

    final case class Vegetarian(d: String) extends Lunch

    final case object Paleo extends Lunch

    case class Normal(d: String) extends Lunch

    // just testing subtyping
    // class Subtyping() extends Normal("gg") // does not work

    val v: Lunch = ???
    v match {
      case Vegetarian(_) =>
      case Paleo =>
      case Normal(_) =>
    }

  }

  object adts {

    case class Email private(value: String)

    object Email {
      def apply(value: String): Option[Email] = ???
    }


    // in Java we would have (product-type alike):
    //        class PaymentMethod {
    //          static final int CREDIT_CARD = 1;
    //
    //          int getType();
    //
    //          @Nullable
    //          String getCreditCardNumber()
    //
    //          @Nullable
    //          String getBankAccountNumber()
    //
    //        }

    // Better (sum-type):
    sealed trait PaymentMethod

    final case class CreditCard(number: String, code: String, name: String) extends PaymentMethod

    final case object Cash extends PaymentMethod

    final case class Bank(routingNumber: String, swiftCode: String, accountNumber: String) extends PaymentMethod

    final case class Bitcoin(sig: String, address: String) extends PaymentMethod

    final case class CreditCardNumber private()

    object CreditCardNumber {
      def apply(number: String): Option[CreditCardNumber] = ???
    }

    // Example of product-type:
    final case class Employee(name: String, start: Instant, position: Position)

    sealed trait Position

    final case object CEO extends Position

    final case object CTO extends Position

    final case class Engineer(level: Int) extends Position

  }


  object excercises {

    case class Foo() // It *is* sum type with 1 value ("degenerated" product type - also with 1 value) - geometrically a "point"

    // identity for product is Unit
    type String2 = (String, Unit) // product type; isomorphic to String

    def stringToString2(s: String): String2 = (s, ())

    def string2ToString(s: String2): String = s._1 // we are not losing any information

    // identity for sum is Nothing
    type String3 = Either[Nothing, String] // sum type; isomorphic to String

    def string3ToString(s: String3): String = s match {
      case Left(n) => n // Dead code; compiler knows it won't happen, therefore allows it
      case Right(str) => str
    }

    def stringToString3(s: String): String3 = Right(s)


    final abstract class Void { // cannot be instantiated (it's abstract), and cannot be extended (it's final)
      def absurd[A]: A // cannot be implemented, you don't know how to create "A" from air
    }

    type String4 = Either[Void, String]

    def string4ToString(s: String4): String = s match {
      case Left(v) => v.absurd[String] // clever "trick"
      case Right(v) => v
    }

  }

}


// Types are sets, subtypes are subsets
// Universally qualified types are those which are parametric polymorphic

object type_constructor {

  case class Person(name: String)

  Person("name") // Person.apply  => is a DATA CONSTRUCTOR - function constructing the Person data

  // String - is a type
  // List - is NOT a type - it is a type constructor; or a type-level function
  // List[Int] - is a type, constructed using type constructor
  // actually, List : Type => Type

  // parametric polymorphism (infinite number of type arguments) is stronger than subtype polymorphism (finite number of subtypes)
  // the more polymorphic a function is, the less ways of implementing there are.

  // # Kinds
  // List: * => *
  // Map: [*,*] => *

  trait StackLike[Stack] {
    def push(i: Int): Stack = ??? // problem here..
  }

  trait StackLike2[Stack[_]] { // Kind notation. Giving a name to "_" is meaningless, won't be used.
    // It says that Stack is a * => * type, so it expects one type parameter.
    // StaclLike2 has type (* => *) => *
    // if we subsitute, we can say like S = (* => *), then StackLike2: S => *

    def push[A](s: Stack[A], a: A): Stack[A] // Having push(s: Stack[theNamed], .. - won't work

    def pop[A](s: Stack[A]): Option[(A, Stack[A])]
  }

  val ListStackLike = new StackLike2[List] {
    override def push[A](s: List[A], a: A) = ???

    override def pop[A](s: List[A]) = ???
  }

  // Every kind returns a type, not type constructor.
  // So, this is not well-kinded: (* => (* => *))
  // E.g. Int => (String => Boolean)   <-- there's not something like that in Scala

  // In Scala3 (dotty) it would be possible to use Map[String, _]  - will return type constructor

  // Examples which work:
  //  StackLike2[List]
  //  StackLike2[Deque]
  //  StackLike2[Vector]
  //  StackLike2[Seq]
  //  StackLike2[Function0]

  // Examples which won't work:
  //  StackLike2[String]
  //  StackLike2[Map]

  val f: StackLike2[Function0] = new StackLike2[Function0] {
    override def push[A](s: () => A, a: A) = ???

    override def pop[A](s: () => A) = ???
  }

  // Works but useless, since I lost the name of the parameter, I can't define functions in the trait using the param
  //    trait Y[_]
  // Doe not work
  //    val yString = Y[String]
  //    val yList = Y[List]
  //    val yMap = Y[Map]

  // NOTE: Java supports just kinds *

  /*
    A       : *
    A[_]    : * => *
    A[_,_]  : [*, *] => *
    A[_[_]] : (* => *) => *      A[S] where S = _[_] : (* => *) => *

    A[_[_, _], _[_]]: A[K, V] where K = _[_, _], V = _[_]:
       K : [*, *] => *
       V : * => *
       A : [ [*,*] => *, * => * ] => *

   const (*f(*c const))


  */


  /*

  scala> trait A[B[_,_], C[_]]
  defined trait A

  scala> :kind A
  A's kind is X[F1[A1,A2],F2[A3]]

  scala> trait D[_[_,_], _[_]]
  <console>:12: error: _ is already defined as type _
         trait D[_[_,_], _[_]]
  */


  /*

  A[_[_[_]], _, _[_, _, _] : [ (* => *) => * , * , [*, *, *] => *] => *

  */

  trait Algorithm[MapLike[_, _], ListLike[_]] {
    //
  }

  /*

   [*,*] => *  // A[_, _]
   * => A
   (* => *) => * : // A[_[_]]


   ((* => *) => *) => * : // A[_[_[_]]]
   [[*,*] => *, * => *] : // A[_[_,_] , _[_]]

   [((* => *) => *) => *, * => *] => *   //  A[_[_[_[_]]] , _[_]]]

   */

  // Curry-Howard isomorphism:
  //  If I can implement proposition (converted to types), I have a proof of the proposition
  def prop1[A, B]: A => B = (a: A) => (??? : B) // false, cannot implement
  def prop2[A, B >: A]: A => B = (a: A) => a // true

  // Also why parametric polymorphism is useful:
  def repeat0(n: Int, s: String) = "john" // it can be implemented many ways

  def repeat1[A](n: Int, s: A, f: (A, A) => A): A =
    if (n <= 1) s
    else f(s, repeat1(n - 1, s, f)) // we limited possible implementations here (need to use A)

  // homework (lets better way):  def repeat2[A](n: Int, s: A, f: (A, A) => A): A = s

}

object typed_lambda {

  // A type lambda is a way to partially apply a higher-kinded type


  trait CollectionLike[F[_]] { // kind: (* => *) => *
    def foldLeft[A, Z](fa: F[A])(z: Z)(f: (Z, A) => Z): Z
  }

  val ListCollectionLike = new CollectionLike[List] {
    override def foldLeft[A, Z](fa: List[A])(z: Z)(f: (Z, A) => Z) = fa.foldLeft(z)(f)
  }


  // # Kind projection, partial type application

  //    def MapCollectionLike[K] = new CollectionLike[Map[K, _]] {} // will exist in Scala3

  def MapValueCollectionLike[K] = {
    type MapK[A] = Map[K, A]

    new CollectionLike[MapK] {
      override def foldLeft[A, Z](fa: MapK[A])(z: Z)(f: (Z, A) => Z) = fa.values.foldLeft(z)(f)
    }
  }

  def MapKeyCollectionLike[V] = new CollectionLike[Map[?, V]] { // kind projection translates kinto typed lambda
    override def foldLeft[A, Z](fa: Map[A, V])(z: Z)(f: (Z, A) => Z) = fa.keys.foldLeft(z)(f)
  }

  def MapKeyCollectionLike2[V] = new CollectionLike[({type MapV[K] = Map[K, V]})#MapV] { // existential type???
    override def foldLeft[A, Z](fa: Map[A, V])(z: Z)(f: (Z, A) => Z) = fa.keys.foldLeft(z)(f)
  }


  // tough to grasp.....
  def Tuple2CollectionLike[A] = {
    type TupleA[B] = Tuple2[A, B]

    new CollectionLike[TupleA] {
      override def foldLeft[B, Z](fa: TupleA[B])(z: Z)(f: (Z, B) => Z) = f(z, fa._2)
    }
  }

  trait Sized[F[_]] {
    def size[A](fa: F[A]): Int
  }

  val ListSized = new Sized[List] {
    override def size[A](fa: List[A]): Int = fa.length
  }


  // This solution uses: type lambda
  // Map: [*,*] => *
  // F: * => *
  def MapSized[K]: Sized[({type J[V] = Map[K, V]})#J] = {
    type MapK[V] = Map[K, V]

    new Sized[MapK] {
      override def size[A](fa: MapK[A]) = fa.size
    }
  }

  val mapSized = MapSized.size(Map(1 -> "ahoj", 2 -> "hello"))


  // this solution uses: Kind projection
  def MapSized2[K]: Sized[Map[K, ?]] = {
    new Sized[Map[K, ?]] {
      override def size[A](fa: Map[K, A]) = fa.size
    }
  }

  val mapSized2 = MapSized2.size(Map(1 -> "ahoj"))

  // this solution uses: Type lambda simplified
  def MapSized3[K] = new Sized[({type MapK[V] = Map[K, V]})#MapK] {
    override def size[A](fa: Map[K, A]) = fa.size
  }

  val mapSized3 = MapSized3.size(Map(1 -> "ahoj", 2 -> "hello"))

}


object existentials {

  // universals and existentials are duals
  // you can do project existential to universal (skolemization)

  // Both universals and existentials are having a type parameter => are form of parametric polymorphism
  // Except that for universal the param type is known to the outside, while in existential it is not

  trait FastMap[A] {
    self =>
    type A0 // "borrow" type up here from the successor!! trick
    val original: Seq[A0]
    val mapping: A0 => A

    // we'll use map fusion - do not actually map
    final def map[B](f: A => B): FastMap[B] = new FastMap[B] {
      type A0 = self.A0
      val original = self.original
      val mapping = self.mapping.andThen(f)
    }

    final def run: Seq[A] = original.map(mapping)
  }

  object FastMap {
    def apply[A](a: A*): FastMap[A] = new FastMap[A] {
      type A0 = A
      val original = Seq(a: _*)
      val mapping = Predef.identity[A]
    }
  }


  // example:
  val fastMap = FastMap(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
  fastMap.map(_ + 1).map(_ * 10).map(_ - 10) // a form of "chain of responsibility" pattern

  fastMap.run

  // Existential types are used when they actually don't need to be exposed, like in COMPOSITION!! e.g.:
  //
  // A --(f)--> B   B ---(g)---> C
  //
  // A ------ f . g -----------> C      => in Scala you can do f.andThen(g)  =>  Here B is not needed to be known


  def size(l: List[_]): Int = l.length // l has existential type

}


object type_classes {

  // essentially required: (A, A) => A. Then repeat it n times.
  def repeat[A](n: Int, a: A): A = ???

  // we require associativity:
  // append :  (A, A) => A

  // A type class is a bunch of operations and laws. It is superior to OOP.
  // in OOP e.g.:
  trait Appendable {
    self =>
    def append(r: self.type): self.type
  } // here you FORCE client to change inheritance hierarchy - require to extend it, it's not MODULAR


  // Type-class:
  // Associativity: append(append(a1, a2), a3) == append(a1, append(a2, a3))
  // - enforce laws using SCALACHECK !!!
  trait Semigroup[A] { // this is a type CLASS
    def append(l: A, r: A): A // semigroup has operation append() which conforms associativity
  }

  // Type-class instances:
  object Semigroup {
    // If we don't control the types - like Int, or Tuple2, put the instances into companion object of the type class
    // If you do, put the instances in the companion objects of that class
    // NOTE: only one instance per type

    implicit val IntAdditionSemigroup = new Semigroup[Int] { // this is an INSTANCE of an type class
      override def append(l: Int, r: Int) = l + r
    }

    implicit def SemigroupTuple2[A, B](implicit A: Semigroup[A], B: Semigroup[B]): Semigroup[(A, B)] = {
      (l: (A, B), r: (A, B)) => (A.append(l._1, r._1), B.append(l._2, r._2))
    }

    implicit val StringSemigroup = new Semigroup[String] {
      override def append(l: String, r: String) = l + r
    }

    // # my implementation (works ok too):
    //      implicit def MapSemigroup[K, V: Semigroup] = new Semigroup[Map[K, V]] {
    //        override def append(l: Map[K, V], r: Map[K, V]) = {
    //          l.map {
    //            case (key, value) => (key, r.get(key).map(_ <> value).getOrElse(value))
    //          } ++ r.filterKeys (key => !l.contains(key))
    //        }
    //      }

    implicit def MapSemigroup[K, V: Semigroup]: Semigroup[Map[K, V]] = (l: Map[K, V], r: Map[K, V]) => {
      (l.toList ++ r.toList).foldLeft[Map[K, V]](Map()) {
        case (map, (key, value0)) =>
          val value = map.get(key).fold(value0)(_ <> value0)
          map.updated(key, value)
      }
    }

    implicit def ListSemigroup[A] = new Semigroup[List[A]] {
      override def append(l: List[A], r: List[A]) = l ++ r
    }

    def apply[A](implicit S: Semigroup[A]): Semigroup[A] = S

  }

  // type-classes allows this:
  def repeat1[A](n: Int, a: A)(implicit S: Semigroup[A]): A =
    if (n <= 1) a
    else S.append(a, repeat1(n - 1, a)(S))


  repeat1(5, 1)
  repeat1(6, (5, 6))


  // context-bound is syntax sugar for implicits:
  def repeat2[A: Semigroup](n: Int, a: A): A =
    if (n <= 1) a
    else implicitly[Semigroup[A]].append(a, repeat2(n - 1, a))

  def repeat3[A: Semigroup](n: Int, a: A): A =
    if (n <= 1) a
    else Semigroup[A].append(a, repeat3(n - 1, a)) // because Semigroup.apply() exists!!


  // or even more "fancy":
  implicit class SemigroupSyntax[A](l: A) {
    def <>(r: A)(implicit S: Semigroup[A]): A = S.append(l, r)
  }

  def repeat4[A: Semigroup](n: Int, a: A): A = if (n <= 1) a else a <> repeat4(n - 1, a)

  repeat4(5, " Hellp")
  "Hello" <> "World"
  ("Hello", "Goodbye") <> ("Goodbye", "Hello")
  (("Hello", "hallo"), "Goodbye") <> (("Goodbye", "bye"), "Hello")

  repeat4(4, Map(1 -> "a", 2 -> "b"))
  repeat4(5, 1 :: 2 :: 3 :: 4 :: 5 :: Nil)


  trait Monoid[A] extends Semigroup[A] { // another type-class
    // Must satisfy:
    // Left  Zero: append(zero, a) == a
    // Right Zero: append(a, zero) == a

    def zero: A
  }

  object Monoid {
    def apply[A](implicit M: Monoid[A]): Monoid[A] = M

    implicit val MonoidInt: Monoid[Int] = new Monoid[Int] {
      override val zero = 0

      override def append(l: Int, r: Int) = l <> r
    }

  }

  def zero[A: Monoid]: A = Monoid[A].zero // implicitly instantiate Monoid, thanks to apply() and implicit vals

  Monoid[Int].append(1, 2) == Semigroup[Int].append(1, 2) // if not equal, we have a incoherence problem

  zero[Int]

  // Example monoids: List, Map, Tuple, addition, multiplication
  // in real world: e.g. a Permission. You can append permissions in associative way, and have a concept of "no permission".


  // Transitive law: eq(a,b) && eq(b,c) => eq(a,c)
  // We should have: 1 / 10 = 1 / (2 * 5) = (1 / 2) * (1 / 5)  - but when using floating point it breaks
  // Therefore we shouldn't use floating point for currency :D
  trait Eq[A] {
    def eq(l: A, r: A): Boolean
  }

  object Eq {
    def apply[A](implicit A: Eq[A]): Eq[A] = A

    implicit val EqInt = new Eq[Int] {
      override def eq(l: Int, r: Int): Boolean = l == r
    }
  }

  implicit class EqSyntax[A](l: A) {
    def ===(r: A)(implicit A: Eq[A]): Boolean = A.eq(l, r)
  }

  1 === 1 // true

  // In FP, we discourage using Scala `.eq()` method, because it has wrong datatype. Instead we encourage using
  // type classes for equality.

}

object functor {

  /*
   Laws:
     - Identity law   : fmap(identity)(fa)   === fa
     - Composition law: fmap(f)(fmap(g)(fa)) === fmap(f.compose(g))(fa)
  */
  trait Functor[F[_]] {
    def fmap[A, B](f: A => B): F[A] => F[B]
  }

  object Functor {
    def apply[F[_] : Functor]: Functor[F] = implicitly[Functor[F]]

    implicit val OptionFunctor = new Functor[Option] {
      override def fmap[A, B](f: A => B): Option[A] => Option[B] = _.map(f)
    }
    implicit val ListFunctor = new Functor[List] {
      override def fmap[A, B](f: A => B): List[A] => List[B] = _.map(f)
    }

    // also works:
    //      implicit def FunctionFunctor[T] = new Functor[T => ?] {
    //        override def fmap[A, B](f: A => B): (T => A) => (T => B) = tToA => tToA.andThen(f)  //f.compose(tToA)
    //      }
    //
    implicit def FunctionFunctor[T] = new Functor[Function[T, ?]] {
      override def fmap[A, B](f: A => B): (T => A) => (T => B) = tToA => f.compose(tToA)
    }
  }

  implicit class FunctorSyntax[F[_], A](fa: F[A]) {
    def fmap[B](f: A => B)(implicit F: Functor[F]): F[B] = F.fmap(f)(fa)
  }

  List(1, 2, 3).fmap(_.toString)


  // FUNCTORS COMPOSITION
  // - Functors compose very well!

  //   Natural transformation
  case class Compose[F[_], G[_], A](fga: F[G[A]]) // e.g. for solving ss.map(_.map(_.map...)))
  //   Product-type-like composition
  case class Product[F[_], G[_], A](l: F[A], r: G[A])

  //   Sum-type-like composition
  case class Coproduct[F[_], G[_], A](e: Either[F[A], G[A]])


  trait Apply[F[_]] extends Functor[F] {
    // `f` is a function "lifted" to functor F
    def ap[A, B](f: F[A => B], fa: F[A]): F[B] // cannot be implemented generally...
    // Also, it's possible to BATCH (concurrently run) both F[A=>B] and F[A]
    // Not possible in Monad

    def zip[A, B](l: F[A], r: F[B]): F[(A, B)]
  }

  object Apply {
    implicit val OptionApply = new Apply[Option] {
      override def ap[A, B](f: Option[A => B], fa: Option[A]): Option[B] = f match {
        case (Some(aToB)) => fa.fmap(aToB)
        case _ => None
      }

      override def zip[A, B](l: Option[A], r: Option[B]): Option[(A, B)] = (l, r) match {
        case (Some(a), Some(b)) => Some((a, b))
        case _ => None
      }

      override def fmap[A, B](f: A => B): Option[A] => Option[B] = Functor[Option].fmap(f)
    }
  }

  // example:
  // Applicative: packed function, apply packed value on it
  // Option((i: Int) => i.toString).ap(Option(1))


  trait Applicative[F[_]] extends Apply[F] {
    def point[A](a: A): F[A]
  }

  object Applicative {

    implicit val ListApply = new Applicative[List] {
      override def point[A](a: A) = List(a)

      override def ap[A, B](f: List[A => B], fa: List[A]) = for {
        f <- f
        a <- fa
      } yield f(a) // all combinations - cross product

      override def zip[A, B](l: List[A], r: List[B]) = ???

      override def fmap[A, B](f: A => B): List[A] => List[B] = _.map(f)
    }

  }

  final case class Identity[A](run: A) // It's a monad & comonad - because it can go from a => Identity(A) and flipwise

  trait Monad[F[_]] extends Applicative[F] { // Programmable semicolon :)
    def bind[A, B](fa: F[A])(f: A => F[B]): F[B]
  }

  object Monad {
    def apply[F[_]](implicit F: Monad[F]): Monad[F] = F

    implicit val IdentityMonad = new Monad[Identity] {
      override def bind[A, B](fa: Identity[A])(f: A => Identity[B]): Identity[B] = f(fa.run)

      override def point[A](a: A) = Identity(a)

      override def ap[A, B](f: Identity[A => B], fa: Identity[A]) = Identity(f.run(fa.run))

      override def zip[A, B](l: Identity[A], r: Identity[B]) = Identity((l.run, r.run))

      override def fmap[A, B](f: A => B): Identity[A] => Identity[B] = (i: Identity[A]) => Identity(f(i.run))
    }
  }
  implicit class MonadSyntax[F[_], A](fa: F[A]) {
    def map[B](f: A => B)(implicit F: Monad[F]): F[B] = F.fmap(f)(fa)

    def flatMap[B](f: A => F[B])(implicit F: Monad[F]): F[B] = F.bind(fa)(f)
  }

}

object applied {
  import functor._

  /* Description of an effectful program that will produce A. */
  final case class IO[A](unsafePerdormIO: () => A) {
    self =>
    def map[B](f: A => B): IO[B] = IO(() => f(self.unsafePerdormIO()))

    def flatMap[B](f: A => IO[B]): IO[B] = IO(() => f(self.unsafePerdormIO()).unsafePerdormIO()) // wrong: f(self.unsafePerdormIO())
  }

  object IO {
    def point[A](a: => A): IO[A] = IO(() => a)

    implicit val MonadIO: Monad[IO] = new Monad[IO] {
      override def bind[A, B](fa: IO[A])(f: A => IO[B]): IO[B] = f(fa.unsafePerdormIO())

      override def point[A](a: A) = IO.point(a)

      override def ap[A, B](f: IO[A => B], fa: IO[A]) = f.flatMap(aToB => fa.fmap(aToB))

      override def zip[A, B](l: IO[A], r: IO[B]) = ???

      override def fmap[A, B](f: A => B): IO[A] => IO[B] = _.map(f)

    }
  }

  // total, deterministic, pure
//  def println(s: String): IO[Unit] = IO.point(scala.Predef.println(s)) // for some reason IO.point calls println
//  val readline: IO[String] = IO.point(scala.io.StdIn.readLine())

  case class Location()

  case class Cell(items: List[Item])

  case class World(description: String, items: List[Item] = Nil, options: Map[Location, World] = Map())



  sealed trait Item
  case class Food(health: Int, description: String)
  case class Weapon(damage: Int, description: String)



  case class Player(name: String, inventory: List[Item] = Nil, location: Location = Location())
  case class State(player: Player, world: World)

  type Error = String

  sealed trait Command
  case object Exit extends Command
  case object Look extends Command
  case class Pickup(what: String) extends Command


  // Unpleasant:
  //val state: State = ???
  //state.copy(player = state.player.copy(location = state.player.location.copy(....)))

  // Better way: LENSES !!!
  // (see tagless)



  def parseInput(input: String): Either[Error, Command] = {
    val tokens = input.split("\\s+").toList.filter(_ != "").map(_.toLowerCase)

    tokens match {
      case "exit" :: _ => Right(Exit)
      case "look" :: _ => Right(Look)
      case "pickup" :: what :: _ => Right(Pickup(what))
      case _ => Left("Unexpected input!!!")
    }
  }

  def update(command: Command, state: State): (String, Option[State]) = { // Option used 'cause we want to stop sometimes
    command match {
      case Exit => ("Goodbye, " + state.player.name +"!", None)
      case Look => ("You are standing HERE", Some(state))
      case Pickup(what) => ("Nothing man", Some(state))
    }
  }

  def mainLoop[F[_]: Monad: Console](state: State): F[Unit] = {
    val maybeContinue: Command => F[Unit] = { command =>
      val (message, next) = update(command, state)
      for {
        _ <- println[F](message)
        _ <- next.fold(Monad[F].point(()))(mainLoop(_))
      } yield ()
    }

    def printContinue(line: String): F[Unit] =
      for {
        _ <- println[F](line)
        _ <- mainLoop(state)
      } yield ()


    for {
      either <- readline[F].map(parseInput)
      _ <- either.fold(printContinue, maybeContinue)
    } yield ()
  }


  def program[F[_]: Monad: Console]: F[Unit] =
    for {
      _ <- println[F]("What is your name?")
      name <- readline[F]
      _ <- println[F]("Hello, " + name)

      player = Player(name)
      state = State(player, World("204 Wolf Law Building"))

      _ <- mainLoop(state)
    } yield ()



  // TESTABILITY

  trait Log[F[_]] {
    def log(level: LogLevel, line: => String): F[Unit]
  }
  sealed trait LogLevel


  trait Console[F[_]] {
    def println(line: String): F[Unit]
    def readline: F[String]
  }
  object Console {
    def apply[F[_]](implicit F: Console[F]): Console[F] = F

    implicit val ConsoleIO: Console[IO] = new Console[IO] {
      override def println(line: String) = IO.point(scala.Predef.println(line))
      override def readline = IO.point(scala.io.StdIn.readLine())
    }
  }


  case class Buffer(output: List[String], input: List[String])
  case class FakeConsole[A](run: Buffer => (Buffer, A))
  object FakeConsole {

    implicit val ConsoleFakeConsole = new Console[FakeConsole] {
      def println(s: String): FakeConsole[Unit] =
        FakeConsole(buffer => (buffer.copy(output = buffer.output :+ s), ()))

      override def readline: FakeConsole[String] =
        FakeConsole(buffer => buffer.input match {
          case Nil => throw new Exception("Broken test!")
          case line :: rest => (buffer.copy(input = rest), line)
        })
    }

    implicit val MonadFakeConsole = new Monad[FakeConsole] {
      override def bind[A, B](fa: FakeConsole[A])(f: A => FakeConsole[B]): FakeConsole[B] =
        FakeConsole { buffer1 =>
          val (buffer2, a) = fa.run(buffer1)

          f(a).run(buffer2)
        }

      override def point[A](a: A) = FakeConsole(buffer => (buffer, a))

      override def ap[A, B](f: FakeConsole[A => B], fa: FakeConsole[A]) =
        FakeConsole(buffer => {
          val (buffer2, ab) = f.run(buffer)
          val (buffer3, a) = fa.run(buffer2)

          (buffer3, ab(a))
        })

      override def zip[A, B](l: FakeConsole[A], r: FakeConsole[B]) = ???

      override def fmap[A, B](f: A => B): FakeConsole[A] => FakeConsole[B] =
        (fa: FakeConsole[A]) => FakeConsole { buffer1 =>
          val (buffer2, a) = fa.run(buffer1)
          (buffer2, f(a))
        }
    }
  }

  def println[F[_]: Console](s: String): F[Unit] = Console[F].println(s)
  def readline[F[_]: Console]: F[String] = Console[F].readline

  def main(): Unit = program[IO].unsafePerdormIO()

  def test: Seq[String] = program[FakeConsole].run(Buffer(
    output = Nil,
    input = List("John", "look", "look", "exit")
  ))._1.output

}

object optics {
  // Optics:
  //  - handling nested structures in FP
  //  - they compose very well (except Setter & Getter)
  //
  //
  // Optics[S,T,A,B]
  //
  //  S:          T:
  //     A           B
  //      +--------^



  // if S == T, A == B: Optics[S, A]

  // Focusing on Sum-type    : Prism
  // Focusing on Product-type: Lens

  case class Person(name: String, age: Int)
  object Person {
    val _Name = Lens[Person, String](_.name, (name: String) => _.copy(name = name))
    val _Age = Lens[Person, Int](_.age, (age: Int) => _.copy(age = age))
  }

  case class Car(driver: Person)
  object Car {
    val _Person = Lens[Car, Person](_.driver, (person: Person) => _.copy(driver = person))
  }
  val TestCar = Car(Person("John", 23))


  case class Lens[S, A](get: S => A, set: A => S => S) { self =>

    def >>> [B](that: Lens[A, B]): Lens[S, B] =
      Lens[S, B](
        get = (s: S) => that.get(self.get(s)),
        set = (b: B) => (s: S) => self.set(that.set(b)(self.get(s)))(s)
      )

    def modify(f: A => A): S => S =
      (s:S) => self.set(f(self.get(s)))(s)

  }


  import Car._
  import Person._

  (_Person >>> _Age) : Lens[Car, Int]

  (_Person >>> _Age).modify(_ + 10)(TestCar)


}