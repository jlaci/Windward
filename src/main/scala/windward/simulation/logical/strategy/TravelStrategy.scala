package windward.simulation.logical.strategy

import windward.simulation.logical.{Actor, Strategy, Action}
import windward.simulation.units.{Coordinate, SimUnit}

/**
 * A strategy for describing an Actor getting to the goal.
 *
 * @author Janoky Laszlo Viktor <janoky.laszlo@bmeautsoft.hu>
 */
abstract class TravelStrategy[actorType <: Actor, actionType <: Action[actorType]](val goalPosition : Coordinate[SimUnit]) extends Strategy[actorType, actionType]{

}
