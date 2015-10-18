package windward.simulation.logical

import windward.simulation.physical.effects.CellEffect
import windward.simulation.physical.world.World
import windward.simulation.units.{SimUnit, Coordinate}

/**
 * Created by jlaci on 2015. 09. 28..
 */
abstract class Actor(val position: Coordinate[SimUnit]) {

    def getEffects(world: World) : Array[CellEffect]

    def step(world: World) : Actor
}
