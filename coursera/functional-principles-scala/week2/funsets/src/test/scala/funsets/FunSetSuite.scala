package funsets

import org.scalatest.FunSuite


import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  // test("string take") {
  //   val message = "hello, world"
  //   assert(message.take(5) == "hello")
  // }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  // test("adding ints") {
  //   assert(1 + 2 === 3)
  // }


  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
      assert(contains(s2, 2), "Singleton")
      assert(contains(s3, 3), "Singleton")
    }
  }

  test("union contains all elements of each set") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("intersect") {
    new TestSets {
      val s1i = intersect(union(s1, s2), s1)
      val s2i = intersect(union(s1, s2), s2)

      assert(contains(s2i, 2), "(s1+s2) & s2 should be s2")
      assert(!contains(s2i, 1), "(s1+s2) & s2 should be s2")
      assert(contains(s1i, 1), "(s1+s2) & s1 should be s1")
      assert(!contains(s1i, 2), "(s1+s2) & s1 should be s1")
    }
  }

  test("diff") {
    new TestSets {
      val s1d = diff(union(s1, s2), s2)
      val s2d = diff(union(s1, s2), s1)

      assert(contains(s1d, 1), "s1+s2 - s2 should be s1")
      assert(!contains(s1d, 2), "s1+s2 - s2 should be s1")
      assert(contains(s2d, 2), "s1+s2 - s1 should be s2")
      assert(!contains(s2d, 1), "s1+s2 - s1 should be s2")
    }
  }

  test("filter") {
    new TestSets {
      val s123 = union(union(s1, s2), s3)

      val s1f = filter(s123, p => p == 1 || p == 3)
      assert(contains(s1f, 1))
      assert(!contains(s1f, 2))
      assert(contains(s1f, 3))
    }
  }

  test("forall") {
    assert(forall(y => y > 2, y => y > 2))
    assert(forall(y => y > 2 && y < 10, y => y > 2))
    assert(!forall(y => y > 2 || y == -1, y => y > 2))
  }

  test("exists") {
    assert(exists(y => y > 2 || y == -1, y => y == -1))
  }

  test("map identity") {
    val s = map(y => List(1,3,4,5,7,1000).contains(y), y => y)
    assert(List(1,3,4,5,7,1000).filter(p => contains(s, p)).size == 6)
  }

  test("map") {
    val s = map(y => y >= -5 && y < 0, y => -y)

    assert(contains(s, 1))
    assert(contains(s, 5))
    assert(contains(s, 3))
    assert(!contains(s, 0))
  }

  test("grader map") {
    val s = map(y => List(1,3,4,5,7,1000).contains(y), y => y - 1)
    assert(List(0,2,3,4,6,999).filter(p => contains(s, p)).size == 6)
  }

  test("grader forall . map") {
    assert(forall(map(y => true, y => y * 2), ((x: Int) => x % 2 == 0)),
      "was false. The set obtained by doubling all numbers should contain only even numbers.")
  }

}
