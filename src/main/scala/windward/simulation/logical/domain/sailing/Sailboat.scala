package windward.simulation.logical.domain.sailing

import windward.simulation.logical.domain.sailing.strategy.SailingAction
import windward.simulation.logical.{Actor, Strategy}
import windward.simulation.physical.PhyisicsUtility
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
        move(this, world)
    }

    def calculateNewState(sailboat: Sailboat, world: World) : Sailboat = {
        var result = sailboat;

        for(action <- strategy.step(result, world)) {
            result = action.applyOn(result)
        }

        result
    }

    def move(sailboat: Sailboat, world: World) : Sailboat = {
        val newSpeed = calculateSpeed(sailboat, world.getCellsInRadius(sailboat.posX.toCellUnit(), sailboat.posY.toCellUnit(), sailboat.params.length.toCellUnit()));
        val x = sailboat.posX.toMeters() + Math.cos(Math.toRadians(sailboat.heading)) * newSpeed
        val y = sailboat.posY.toMeters() + Math.sin(Math.toRadians(sailboat.heading)) * newSpeed


        new Sailboat(SimulationUnits.simUnitFromMeter(x.toInt), SimulationUnits.simUnitFromMeter(y.toInt), params, heading, newSpeed, 0, strategy, possibleActions, ongoingActions);
    }

    def calculateSpeed(sailboat: Sailboat, cells : List[Cell]) : Float = {
        var averageWindSpeed = 0f;
        var averageDirection = 0;

        for(cell <- cells) {
            averageWindSpeed += cell.windSpeed

            if(cell.windDirection > 180) {
                averageDirection += -1 * (cell.windDirection - 180)
            } else {
                averageDirection += cell.windDirection
            }
        }

        averageWindSpeed = averageWindSpeed / cells.length
        averageDirection = averageDirection / cells.length


        //The maximum achievable speed in these wind conditions
        val maxSpeed = sailboat.params.sails(sailboat.activeSail).polarCurve.getMaxSpeed(averageWindSpeed.toInt, sailboat.heading - averageDirection);
        val speedDifference = maxSpeed - sailboat.speed;

        val energyDifference = PhyisicsUtility.calculateKineticEnergy(speedDifference, sailboat.params.mass)
        val sailEfficiency = averageWindSpeed / maxSpeed

        val currentKineticEnergy = PhyisicsUtility.calculateKineticEnergy(sailboat.speed, sailboat.params.mass)
        val deltaEnergy = energyDifference * sailEfficiency;
        val newKineticEnergy = currentKineticEnergy + deltaEnergy;
        PhyisicsUtility.calculateSpeedFromKineticEnergy(newKineticEnergy, sailboat.params.mass)
    }

}
