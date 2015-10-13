package windward.simulation.logical.actors.sailing

import windward.simulation.logical.actors.Actor
import windward.simulation.physical.effects.CellEffect
import windward.simulation.physical.world.World
import windward.simulation.units.SimUnit

/**
 * Created by jlaci on 2015. 09. 28..
 */
class Sailboat(val posX: SimUnit,
               val posY : SimUnit,
               val length : Int,
               val height : Int,
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
