package windward.simulation.logical.domain.sailing

import windward.simulation.logical.{Strategy, Actor}
import windward.simulation.logical.domain.sailing.sail.Sail
import windward.simulation.logical.domain.sailing.strategy.SailingAction
import windward.simulation.physical.effects.CellEffect
import windward.simulation.physical.world.World
import windward.simulation.units.{Coordinate, SimUnit}

/**
 * Created by jlaci on 2015. 09. 28..
 */
class Sailboat(val posX: SimUnit,
               val posY : SimUnit,
               val params: SailboatParams,
               val heading : Int,
               val speed : Int,
               val sails : Array[Sail],
               val strategy : Strategy[Sailboat, SailingAction],
               val possibleActions : List[SailingAction],
               val ongoingActions : List[(SailingAction, Int)]) extends Actor(Coordinate.SimCoords(posX, posY)) {

    override def getEffects(world: World): Array[CellEffect] = {
        Array.empty;
    }

    override def step(world: World): Sailboat = {
        var result = this;

        for(action <- strategy.step(this, world)) {
            result = action.applyOn(result)
        }

        result
    }

}
