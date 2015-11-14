package windward.simulation.logical.domain.sailing.strategy

import breeze.linalg.{DenseVector}
import windward.simulation.logical.domain.sailing.Sailboat
import windward.simulation.logical.domain.sailing.strategy.actions.Turn
import windward.simulation.logical.strategy.TravelStrategy
import windward.simulation.physical.world.World
import windward.simulation.units.{SimulationUnits, Coordinate, SimUnit}

/**
 * A strategy that goes in a straight line to the end.
 *
 * @author Janoky Laszlo Viktor <janoky.laszlo@bmeautsoft.hu>
 */
class DirectLineStrategy(goalPosition : Coordinate[SimUnit]) extends TravelStrategy[Sailboat, SailingAction](goalPosition){

    override def step(sailboat : Sailboat, world: World): List[SailingAction] = {
        var dir = DenseVector[Double](goalPosition.x.toFloat - sailboat.position.x.toFloat, -(goalPosition.y.toFloat - sailboat.position.y.toFloat))
        val dirLength = Math.sqrt(dir.data(0) * dir.data(0) + dir.data(1) * dir.data(1))
        dir = dir/dirLength

        val north = DenseVector[Double](0, 1)
        val east = DenseVector[Double](1, 0)

        val northAngle = Math.acos(dir dot north)
        val eastAngle = Math.acos(dir dot east)

        var angle = 0d
        if(eastAngle >= Math.PI/2) {
            angle = (Math.PI * 2) - northAngle
        } else {
            angle = northAngle
        }

        val newHeading = Math.toDegrees(angle)
        val delta = newHeading - sailboat.heading
        val max = sailboat.params.turnSpeed * SimulationUnits.timeStepInMilliseconds/1000

        if(delta > 0) {
            List(new Turn(Math.min(delta, max)))
        } else {
            List(new Turn(Math.max(delta, -max)))
        }
    }

}
