package physical

import physical.world.World

/**
 * Created by jlaci on 2015. 09. 18..
 */
object WorldBuilder {

  def createEmptyWorld(width : Int, height : Int) : World = {
    new World(width, height);
  }

}
