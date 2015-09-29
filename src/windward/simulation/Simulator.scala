package windward.simulation

import windward.simulation.logical.LogicalSimulator
import windward.simulation.physical.{PhysicalSimulator, WorldBuilder}
import windward.simulation.physical.world.World
import windward.simulation.units.SimulationUnits


/**
 * Created by jlaci on 2015. 09. 25..
 */
object Simulator {

    var simTime: Int = 0;

    var worldState: Array[World] = null;
    var simulationParameters : SimulationParameters = null;

    def init(parameters: SimulationParameters): Unit = {
        simulationParameters = parameters;
        worldState = new Array[World](parameters.endTime + 1)
        worldState(0) = WorldBuilder.createWorldWitGradientWind(parameters.worldWidth, parameters.worldHeight, 45, 300);
        //WeatherGenerator.initUniformWind(world, 0, 6);
        //WeatherGenerator.initRandomWind(world, 6, 30);
    }

    def start(): Unit = {
        println("Starting simulation")
        while (simTime < simulationParameters.endTime) {
            step()
            simTime += 1;
        }
    }

    def step(): Unit = {
        println("Simulation step " + simTime + " time: " + (simTime * SimulationUnits.timeStepInMilliseconds / 1000) + "s")
        worldState(simTime + 1) = PhysicalSimulator.step(worldState(simTime));
        LogicalSimulator.step();
    }
}
