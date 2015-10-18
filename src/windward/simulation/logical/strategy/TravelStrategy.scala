package windward.simulation.logical.strategy

import windward.simulation.units.{Coordinate, SimUnit}

/**
 * A strategy for describing to getting to the goal.
 *
 * @author Janoky Laszlo Viktor <janoky.laszlo@bmeautsoft.hu>
 */
abstract class TravelStrategy(val goalPosition : Coordinate[SimUnit]) {

    def step() : Unit
    
}
