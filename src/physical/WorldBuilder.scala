package physical

/**
 * Created by jlaci on 2015. 09. 18..
 */
object WorldBuilder {

  def createWorld(width : Int, height : Int) : World = {
    new World(width, height);
  }
}
