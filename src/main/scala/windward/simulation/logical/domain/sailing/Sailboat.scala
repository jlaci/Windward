package windward.simulation.logical.domain.sailing

import breeze.linalg.DenseVector
import windward.simulation.logical.domain.sailing.strategy.{ZStrategy, SailingAction}
import windward.simulation.logical.{Actor, Strategy}
import windward.simulation.physical.PhysicsUtility
import windward.simulation.physical.effects.CellEffect
import windward.simulation.physical.world.{Cell, World}
import windward.simulation.units.{Coordinate, SimUnit, SimulationUnits}

/**
 * Created by jlaci on 2015. 09. 28..
 */
class Sailboat(val posX: SimUnit,
               val posY : SimUnit,
               val params: SailboatParams,
               val heading : Double,
               val speed : Double,
               val activeSail : Int,
               val strategy : Strategy[Sailboat, SailingAction],
               val possibleActions : List[SailingAction],
               val ongoingActions : List[(SailingAction, Int)]) extends Actor(Coordinate.SimCoords(posX, posY)) {

    override def getEffects(world: World): Array[CellEffect] = {
        Array.empty
    }

    override def step(world: World): Sailboat = {
        var result = this

        //First we apply the previous state
        result = applyPreviousState(world)

        //Then we calculate the new
        result = calculateNewState(result, world)

        result
    }

    def applyPreviousState(world : World) : Sailboat = {
        move(world)
    }

    def calculateNewState(sailboat: Sailboat, world: World) : Sailboat = {
        var result = sailboat
        val newStrategy = strategy.copyStrategy()

        for(action <- newStrategy.step(result, world)) {
            result = action.applyOn(result)
        }

        Sailboat.copySailboatWith(result, newStrategy)
    }

    def getHeadingVector() : DenseVector[Double] = {
        DenseVector[Double](Math.cos(Math.toRadians(heading - 90)), Math.sin(Math.toRadians(heading - 90)))
    }

    def getEffectingCells(world : World) : List[Cell] = {
        world.getCellsInRadius(posX.toCellUnit(), posY.toCellUnit(), params.length.toCellUnit())
    }

    def move(world: World) : Sailboat = {
        val newSpeed = calculateSpeed(getEffectingCells(world)) * SimulationUnits.timeStepInMilliseconds / 1000f

        val velocityVector = getHeadingVector()

        val x = posX.toMeters() + velocityVector.data(0) * newSpeed
        val y = posY.toMeters() + velocityVector.data(1) * newSpeed

        new Sailboat(SimulationUnits.simUnitFromMeter(x), SimulationUnits.simUnitFromMeter(y), params, heading, newSpeed, 0, strategy, possibleActions, ongoingActions);
    }

    def calculateSpeed(cells : List[Cell]) : Double = {
        if(activeSail != -1) {
            val averageWindSpeed = getAverageWindSpeed(cells)
            val averageWindDirection = getAverageWindDirection(cells)
            val relativeWindDirection = getRelativeWindDirection(averageWindDirection)

            val apparentWindDirection = getApparentWindDirection(averageWindSpeed, relativeWindDirection)

            //The maximum achievable speed in these wind conditions
            val maxSpeed = params.sails(activeSail).polarCurve.getMaxSpeed(averageWindSpeed.toInt, apparentWindDirection.toInt)
            val sailEfficiency = maxSpeed / averageWindSpeed

            val currentKineticEnergy = PhysicsUtility.calculateKineticEnergy(speed, params.mass)
            val energyDifference = PhysicsUtility.calculateKineticEnergy(maxSpeed, params.mass) - currentKineticEnergy

            val deltaEnergy = energyDifference * sailEfficiency
            val newKineticEnergy = currentKineticEnergy + deltaEnergy
            PhysicsUtility.calculateSpeedFromKineticEnergy(newKineticEnergy, params.mass)
        } else {
            0
        }
    }

    def getAverageWindSpeed(cells : List[Cell]) : Double = {
        var averageWindSpeed = 0f;

        for(cell <- cells) {
            averageWindSpeed += cell.windSpeed
        }

        averageWindSpeed / cells.length
    }

    def getAverageWindDirection(cells : List[Cell]) : DenseVector[Double] = {
        var averageDirectionVector = DenseVector[Double](0,0);

        for(cell <- cells) {
            averageDirectionVector += DenseVector[Double](Math.cos(Math.toRadians(cell.windDirection)), Math.sin(Math.toRadians(cell.windDirection)))
        }

        (averageDirectionVector / cells.length.toDouble)
    }

    def getWindDirectionFromVector(vector : DenseVector[Double]) : Double = {

        val angle = Math.toDegrees(Math.atan2(vector.data(1), vector.data(0)))
        360 - angle
    }

    def getRelativeWindDirection(windDirection : DenseVector[Double]) : Double = {
        //TODO: understand why
        var windAngle = getWindDirectionFromVector(windDirection);

        if(windAngle > 180) {
            windAngle = 360 - windAngle
        }

        if(windAngle - heading < -180) {
            windAngle = windAngle + 360
        }
        windAngle - heading
    }

    /**
     * @param w True wind velocity
     * @param alpha Relative wind direction
     * @return
     */
    def getApparentWindDirection(w : Double, alpha : Double) : Double = {
        val v = speed
        val angle = Math.acos((w * Math.cos(alpha.toRadians) + speed)/(Math.sqrt(w * w + v * v + 2 * w * v * Math.cos(alpha.toRadians)))).toDegrees
        if(angle == 360) {
            0
        } else {
            angle
        }


    }

    def getApparentWindSpeed(windDirection : DenseVector[Double], windSpeed : Double) : Double = {
        val windVector = windDirection * windSpeed
        val velocityVector = getHeadingVector() * speed.toDouble
        val appWindVector = (velocityVector + windVector)
        Math.sqrt(appWindVector.data(0) * appWindVector.data(0) + appWindVector.data(1) * appWindVector.data(1))
    }

}

object Sailboat {
    def copySailboatWith(sailboat : Sailboat, strategy : Strategy[Sailboat, SailingAction]) : Sailboat = {
        new Sailboat(sailboat.posX, sailboat.posY, sailboat.params, sailboat.heading, sailboat.speed, sailboat.activeSail, strategy, sailboat.possibleActions, sailboat.ongoingActions)
    }
}
