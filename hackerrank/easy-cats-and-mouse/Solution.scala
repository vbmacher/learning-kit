object Solution {
  def catsAndMouse(x: Int, y: Int, z: Int) = {
    val diffX = math.abs(z - x)
    val diffY = math.abs(z - y)

    if (diffX == diffY) {
      println("Mouse C")
    } else if (diffX < diffY) {
      println("Cat A")
    } else {
      println("Cat B")
    }

  }

  def main(args: Array[String]) {
        val sc = new java.util.Scanner (System.in);
        var q = sc.nextInt();
        var a0 = 0;
        while(a0 < q){
            var x = sc.nextInt();
            var y = sc.nextInt();
            var z = sc.nextInt();
            a0+=1;

            catsAndMouse(x,y,z)
        }
    }
}