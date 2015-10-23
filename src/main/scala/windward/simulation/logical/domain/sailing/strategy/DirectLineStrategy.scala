package windward.simulation.logical.domain.sailing.strategy

import breeze.linalg.{DenseVector}
import windward.simulation.logical.domain.sailing.Sailboat
import windward.simulation.logical.domain.sailing.strategy.actions.Turn
import windward.simulation.logical.strategy.TravelStrategy
import windward.simulation.physical.world.World
import windward.simulation.units.{Coordinate, SimUnit}

/**
 * A strategy that goes in a straight line to the end.
 *
 * @author Janoky Laszlo Viktor <janoky.laszlo@bmeautsoft.hu>
 */
class DirectLineStrategy(goalPosition : Coordinate[SimUnit]) extends TravelStrategy[Sailboat ,SailingAction](goalPosition){

    override def step(sailboat : Sailboat, world: World): List[SailingAction] = {
        val dir = DenseVector[Float](goalPosition.x.toFloat - sailboat.position.x.toFloat, goalPosition.y.toFloat - sailboat.position.y.toFloat);
        val north = DenseVector[Float](0, 1);

        val heading = Math.acos((dir dot north)/(dir.length * north.length));

        List(new Turn(((sailboat.heading - heading.toInt) / sailboat.params.turnSpeed)));
    }

}
