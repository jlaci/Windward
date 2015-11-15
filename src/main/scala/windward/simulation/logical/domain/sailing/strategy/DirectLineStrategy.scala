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
        List(new Turn(limitHeadingChange(getHeadingChange(getHeadingToDirectionPoint(sailboat.position, goalPosition), sailboat.heading), sailboat)))
    }


    private def limitHeadingChange(delta : Double, sailboat: Sailboat) : Double = {
        val max = sailboat.params.turnSpeed * SimulationUnits.timeStepInMilliseconds/1000

        if(delta > 0) {
            Math.min(delta, max)
        } else {
            Math.max(delta, -max)
        }
    }

    private def getHeadingChange(newHeading : Double, oldHeading : Double): Double = {
        var delta = newHeading - oldHeading

        if(delta > 180) {
            delta = delta - 360
        }

        delta
    }

    private def getHeadingToDirectionPoint(from : Coordinate[SimUnit], to : Coordinate[SimUnit]): Double = {
        var dir = DenseVector[Double](to.x.toFloat - from.x.toFloat, -(to.y.toFloat - from.y.toFloat))
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

        Math.toDegrees(angle)
    }

}
