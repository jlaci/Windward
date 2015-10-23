package windward.simulation.logical.domain.sailing

import breeze.linalg.DenseVector
import windward.simulation.logical.domain.sailing.strategy.SailingAction
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
               val heading : Int,
               val speed : Float,
               val activeSail : Int,
               val strategy : Strategy[Sailboat, SailingAction],
               val possibleActions : List[SailingAction],
               val ongoingActions : List[(SailingAction, Int)]) extends Actor(Coordinate.SimCoords(posX, posY)) {

    override def getEffects(world: World): Array[CellEffect] = {
        Array.empty;
    }

    override def step(world: World): Sailboat = {
        var result = this;

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
        var result = sailboat;

        for(action <- strategy.step(result, world)) {
            result = action.applyOn(result)
        }

        result
    }

    def getHeadingVector() : DenseVector[Double] = {
        DenseVector[Double](Math.cos(Math.toRadians(heading - 90)), -Math.sin(Math.toRadians(heading - 90)))
    }

    def getEffectingCells(world : World) : List[Cell] = {
        world.getCellsInRadius(posX.toCellUnit(), posY.toCellUnit(), params.length.toCellUnit())
    }

    def move(world: World) : Sailboat = {
        val newSpeed = calculateSpeed(getEffectingCells(world)) * SimulationUnits.timeStepInMilliseconds / 1000f

        val velocityVector = getHeadingVector()

        //val x = posX.toMeters() + Math.cos(Math.toRadians(heading - 90)) * newSpeed
        //val y = world.height.toMeters() - Math.sin(Math.toRadians(heading - 90)) * newSpeed

        val x = posX.toMeters() + velocityVector.data(0) * newSpeed
        val y = posY.toMeters() - velocityVector.data(1) * newSpeed

        new Sailboat(SimulationUnits.simUnitFromMeter(x), SimulationUnits.simUnitFromMeter(y), params, heading, newSpeed, 0, strategy, possibleActions, ongoingActions);
    }

    def calculateSpeed(cells : List[Cell]) : Float = {
        val averageWindSpeed = getAverageWindSpeed(cells)
        val averageWindDirectionVector = getAverageWindDirection(cells)
        val relativeWindDirection = getRelativeWindDirection(averageWindDirectionVector)

        //The maximum achievable speed in these wind conditions
        val maxSpeed = params.sails(activeSail).polarCurve.getMaxSpeed(averageWindSpeed.toInt, relativeWindDirection.toInt)
        val sailEfficiency = maxSpeed / averageWindSpeed

        val currentKineticEnergy = PhysicsUtility.calculateKineticEnergy(speed, params.mass)
        val energyDifference = PhysicsUtility.calculateKineticEnergy(maxSpeed, params.mass) - currentKineticEnergy

        val deltaEnergy = energyDifference * sailEfficiency
        val newKineticEnergy = currentKineticEnergy + deltaEnergy
        PhysicsUtility.calculateSpeedFromKineticEnergy(newKineticEnergy, params.mass)
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
            averageDirectionVector += DenseVector[Double](Math.cos(Math.toRadians(cell.windDirection)), Math.sin(Math.toRadians(cell.windDirection)));
        }

        (averageDirectionVector / cells.length.toDouble)
    }


    def getRelativeWindDirection(windDirection : DenseVector[Double]) : Double = {
        val headingVector = getHeadingVector()
        Math.toDegrees(Math.acos((headingVector dot windDirection)/(headingVector.length * windDirection.length)));
    }

}
