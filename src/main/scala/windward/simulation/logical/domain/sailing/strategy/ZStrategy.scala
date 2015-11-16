package windward.simulation.logical.domain.sailing.strategy

import breeze.linalg.{DenseMatrix, DenseVector}
import windward.simulation.logical.domain.sailing.Sailboat
import windward.simulation.logical.domain.sailing.strategy.actions.{Stop, Turn}
import windward.simulation.logical.strategy.TravelStrategy
import windward.simulation.physical.world.World
import windward.simulation.units.{SimulationUnits, SimUnit, Coordinate}

/**
 * @author Janoky Laszlo Viktor <janoky.laszlo@bmeautsoft.hu>
 */
class ZStrategy(goalPosition : Coordinate[SimUnit], params : ZStrategyParameters) extends TravelStrategy[Sailboat, SailingAction](goalPosition){

    var state = ZStrategyState.Uninitialized
    var turn1position : Coordinate[SimUnit] = null
    var turn2position : Coordinate[SimUnit] = null

    override def step(sailboat : Sailboat, world: World): List[SailingAction] = {

        if(state == ZStrategyState.Uninitialized) {
            state = ZStrategyState.Start
            initStrategy(sailboat.position, sailboat.heading)
        }

        if(state == ZStrategyState.Start) {
            if(checkTargetReached(sailboat.position, turn1position)) {
                state = ZStrategyState.OneTurn
                turnTowards(sailboat, turn2position)
            } else {
                turnTowards(sailboat, turn1position)
            }
        } else if(state == ZStrategyState.OneTurn) {
            if(checkTargetReached(sailboat.position, turn2position)) {
                state = ZStrategyState.TwoTurns
                turnTowards(sailboat, goalPosition)
            } else {
                turnTowards(sailboat, turn2position)
            }
        } else if(state == ZStrategyState.TwoTurns) {
            if(checkTargetReached(sailboat.position, goalPosition)) {
                state = ZStrategyState.Finished
                List(new Stop())
            } else {
                turnTowards(sailboat, goalPosition)
            }
        } else {
            List(new Stop())
        }

    }

    private def turnTowards(sailboat : Sailboat, coordinate: Coordinate[SimUnit]) : List[SailingAction] = {
        List(new Turn(limitHeadingChange(getHeadingChange(getHeadingToDirectionPoint(sailboat.position, coordinate), sailboat.heading), sailboat)))
    }

    private def checkTargetReached(position : Coordinate[SimUnit], target : Coordinate[SimUnit]) : Boolean = {
        val targetVector = (Coordinate.VectorFromSimCoords(target) - Coordinate.VectorFromSimCoords(position))
        val distance = Math.sqrt(targetVector.data(0) * targetVector.data(0) + targetVector.data(1) * targetVector.data(1))
        val treshold = SimulationUnits.simUnitFromMeter(5).value
        distance < treshold
    }

    private def initStrategy(startingPosition : Coordinate[SimUnit], startingHeading : Double) {
        val axis = Coordinate.VectorFromSimCoords(startingPosition) - Coordinate.VectorFromSimCoords(goalPosition)
        val angle = Math.toRadians(params.alfa / 2)
        val leftRotationMatrix = DenseMatrix((Math.cos(angle),-Math.sin(angle)),(Math.sin(angle),Math.cos(angle)))
        val rightRotationMatrix = DenseMatrix((Math.cos(-angle),-Math.sin(-angle)),(Math.sin(angle),Math.cos(-angle)))

        val leftBoundary = leftRotationMatrix * axis
        val rightBoundary = rightRotationMatrix * axis

        val leftTurn1 = leftBoundary * params.l1 + Coordinate.VectorFromSimCoords(goalPosition)
        val leftTurn2 = leftBoundary * params.l2 + Coordinate.VectorFromSimCoords(goalPosition)

        val rightTurn1 = rightBoundary * params.l1 + Coordinate.VectorFromSimCoords(goalPosition)
        val rightTurn2 = rightBoundary * params.l2 + Coordinate.VectorFromSimCoords(goalPosition)

        //Find the first waypoint by which being the closest
        val leftHeadingChange = getHeadingChange(getHeadingToDirectionPoint(startingPosition, Coordinate.SimCoordsFromVector(leftTurn1)), startingHeading)
        val rightHeadingChange = getHeadingChange(getHeadingToDirectionPoint(startingPosition, Coordinate.SimCoordsFromVector(rightTurn1)), startingHeading)

        if(Math.abs(leftHeadingChange) < Math.abs(rightHeadingChange)) {
            turn1position = Coordinate.SimCoordsFromVector(leftTurn1)
            turn2position = Coordinate.SimCoordsFromVector(rightTurn2)
        } else {
            turn1position = Coordinate.SimCoordsFromVector(rightTurn1)
            turn2position = Coordinate.SimCoordsFromVector(leftTurn2)
        }
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

    override def copyStrategy() : ZStrategy = {
        val result = new ZStrategy(goalPosition, params)
        result.state = state
        result.turn1position = turn1position
        result.turn2position = turn2position
        result
    }
}

class ZStrategyParameters (val alfa : Double, val l1 : Double, val l2 : Double) {
    override def toString() : String = {
        "alfa: " + alfa + " l1: " + l1 + " l2: " + l2
    }
}

object ZStrategyState extends Enumeration {
    val Uninitialized, Start, OneTurn, TwoTurns, Finished = Value
}
