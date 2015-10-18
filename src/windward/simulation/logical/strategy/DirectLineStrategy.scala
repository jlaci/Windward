package windward.simulation.logical.strategy

import windward.simulation.units.{Coordinate, SimUnit}

/**
 * A strategy that goes in a straight line to the end.
 * @author Janoky Laszlo Viktor <janoky.laszlo@bmeautsoft.hu>
 */
class DirectLineStrategy(goalPosition : Coordinate[SimUnit]) extends TravelStrategy(goalPosition){
    override def step(): Unit = ???
}
