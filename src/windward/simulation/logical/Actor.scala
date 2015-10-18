package windward.simulation.logical

import windward.simulation.physical.effects.CellEffect
import windward.simulation.physical.world.World

/**
 * Created by jlaci on 2015. 09. 28..
 */
abstract class Actor(val x : Int, val y : Int) {

    def getEffects(world: World) : Array[CellEffect]

    def step(world: World) : Actor
}
