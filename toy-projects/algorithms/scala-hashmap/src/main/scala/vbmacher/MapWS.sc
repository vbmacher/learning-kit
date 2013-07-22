import vbmacher.BucketMap

object MapWS {

  println("A")

  val map = new BucketMap[Int, String]

  val nmap = map + (0, "Peter") + (1, "Tomas") + (10, "Jezis")

  val x = nmap(0)
  val y = nmap(1)



}