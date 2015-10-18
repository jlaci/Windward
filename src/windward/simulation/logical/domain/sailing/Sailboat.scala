package windward.simulation.logical.domain.sailing

import windward.simulation.logical.Actor
import windward.simulation.logical.domain.sailing.sail.Sail
import windward.simulation.physical.effects.CellEffect
import windward.simulation.physical.world.World
import windward.simulation.units.SimUnit

/**
 * Created by jlaci on 2015. 09. 28..
 */
class Sailboat(val posX: SimUnit,
               val posY : SimUnit,
               val length : SimUnit,
               val height : SimUnit,
               val heading : Int,
               val speed : Int,
               val sails : Array[Sail]) extends Actor(posX.toInt, posY.toInt) {

    override def getEffects(world: World): Array[CellEffect] = {
        Array.empty;
    }

    override def step(world: World): Actor = {
        null;
    }

}
