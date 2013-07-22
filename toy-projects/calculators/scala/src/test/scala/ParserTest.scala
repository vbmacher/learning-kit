import org.scalatest.FunSuite

class ParserTest extends FunSuite {

  import Parser._

  test("FlatMap with Unit") {
    val result = item().flatMap(c => unit(c))("ahoj")

    assert(result === List(('a', "hoj")))
  }





}
