package stackoverflow

import org.scalatest.{BeforeAndAfterAll, FunSuite}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import java.io.File

import stackoverflow.StackOverflow._

@RunWith(classOf[JUnitRunner])
class StackOverflowSuite extends FunSuite with BeforeAndAfterAll {


  lazy val testObject = new StackOverflow {
    override val langs =
      List(
        "JavaScript", "Java", "PHP", "Python", "C#", "C++", "Ruby", "CSS",
        "Objective-C", "Perl", "Scala", "Haskell", "MATLAB", "Clojure", "Groovy")
    override def langSpread = 50000
    override def kmeansKernels = 45
    override def kmeansEta: Double = 20.0D
    override def kmeansMaxIterations = 120
  }

  test("testObject can be instantiated") {
    val instantiatable = try {
      testObject
      true
    } catch {
      case _: Throwable => false
    }
    assert(instantiatable, "Can't instantiate a StackOverflow object")
  }

 W /*
  * [Test Description] 'clusterResults' running just 1 iteration on the sampled stackoverflow dataset should return correct result
[Observed Error] noUnexpectedElements was false
Expected: List((C#,100,9172,0), (C#,100,7292,3), (C#,100,1336,15), (C++,100,3777,1), (C++,100,3881,3), (C++,100,1169,10), (CSS,100,3652,0), (CSS,100,1977,3), (CSS,100,29,116), (Clojure,100,128,2), ...)
Actual:   List((C#,100,9172,0), (C#,100,7292,3), (C#,100,1336,15), (C++,100,3777,1), (C++,100,3881,3), (C++,100,1169,10), (CSS,100,3652,0), (CSS,100,1977,3), (CSS,100,29,116), (Clojure,100,128,2), ...)
The given collection contains some unexpected elements: List((Ruby,100,108,40), (Groovy,100,8,23), (Clojure,100,12,63))
[Lost Points] 10

[Test Description] 'clusterResults' running on the sampled stackoverflow dataset should return correct result
[Observed Error] noUnexpectedElements was false
Expected: List((C#,100,17519,1), (C#,100,262,44), (C#,100,19,242), (C++,100,8616,2), (C++,100,185,32), (C++,100,26,141), (CSS,100,5497,1), (CSS,100,140,27), (CSS,100,21,134), (Clojure,100,165,2), ...)
Actual:   List((C#,100,17519,1), (C#,100,262,44), (C#,100,19,242), (C++,100,8616,2), (C++,100,185,32), (C++,100,26,143), (CSS,100,5497,1), (CSS,100,140,27), (CSS,100,21,134), (Clojure,100,165,2), ...)
The given collection contains some unexpected elements: List((PHP,100,8,380), (Python,100,8,598), (PHP,100,160,35), (Ruby,100,46,62), (C++,100,26,143), ...)
[Lost Points] 10

  *
  * */


  test("clusterResults") {
    val vectors = StackOverflow.sc.parallelize(List( (450000, 39),(500000, 31),(150000,1),(150000,10),(500000, 55),(150000,2) ,(150000,22)))

    val means = Array((500000, 13),(150000,10))

    var results: Array[(String, Double, Int, Int)] = testObject.clusterResults(means, vectors)

    testObject.printResults(results)

    println(results(0))
    println(results(1))

    assert(results.contains("Python", 100.0, 4, 6)) //I like python~!
    assert(results.contains("Scala", 66.66666666666667, 3,39))
  }


}
