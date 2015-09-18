import physical.WorldBuilder

/**
 * Created by jlaci on 2015. 09. 18..
 */
object Application {

  def main(args : Array[String]) : Unit = {
    println("Windward starting.")

    WorldBuilder.createWorld(64, 64);
  }
}
