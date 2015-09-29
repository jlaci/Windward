package windward.simulation.logical.actors

import windward.simulation.physical.effects.CellEffect
import windward.simulation.physical.world.{World}
import windward.simulation.units.SimUnit

/**
 * Created by jlaci on 2015. 09. 28..
 */
class Sailboat(val posX: SimUnit, val posY : SimUnit, val length : Int, val height : Int, val heading : Int, val speed : Int) extends Actor(posX.toInt, posY.toInt) {

    override def getEffects(world: World): Array[CellEffect] = {
        null;
    }

    override def step(world: World): Actor = {
        null;
    }

}
