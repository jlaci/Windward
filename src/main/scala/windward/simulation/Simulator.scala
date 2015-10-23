package windward.simulation

import windward.simulation.logical.LogicalSimulator
import windward.simulation.logical.domain.sailing.{Sailboat, SailboatGenerator}
import windward.simulation.physical.world.World
import windward.simulation.physical.{PhysicalSimulator, WorldBuilder}
import windward.simulation.units.{SimUnit, SimulationUnits}


/**
 * Created by jlaci on 2015. 09. 25..
 */
object Simulator {

    var simTime: Int = 0;

    var worldState: Array[World] = null
    var sailboats: Array[Array[Sailboat]] = null
    var simulationParameters : SimulationParameters = null

    def init(parameters: SimulationParameters): Unit = {
        simulationParameters = parameters;
        worldState = new Array[World](parameters.endTime + 1)
        worldState(0) = WorldBuilder.createWorldWitGradientWind(parameters.worldWidth, parameters.worldHeight, 315, 30);

        sailboats = new Array[Array[Sailboat]](parameters.endTime + 1)
        sailboats(0) = new Array[Sailboat](1);
        sailboats(0)(0) = SailboatGenerator.getTestSailboat(new SimUnit(512), new SimUnit(512), 45, parameters.worldWidth, parameters.worldHeight)

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
        sailboats(simTime + 1) = LogicalSimulator.step(worldState(simTime), sailboats(simTime));
    }
}
