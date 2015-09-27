package windward.simulation

import windward.simulation.physical.{PhysicalSimulator, WorldBuilder}
import windward.simulation.physical.world.World


/**
 * Created by jlaci on 2015. 09. 25..
 */
object Simulator {

    var simTime: Int = 0;

    val SIM_TIME_STEP = 1;
    val SIM_END_TIME = 100;

    val worldState: Array[World] = new Array[World](SIM_END_TIME / SIM_TIME_STEP + 1);

    def init(parameters: SimulationParameters): Unit = {
        worldState(0) = WorldBuilder.createWorldWitGradientWind(parameters.worldWidth, parameters.worldHeight, 45, 300);
        //WeatherGenerator.initUniformWind(world, 0, 6);
        //WeatherGenerator.initRandomWind(world, 6, 30);
    }

    def start(): Unit = {
        println("Starting simulation")
        while (simTime + SIM_TIME_STEP <= SIM_END_TIME) {
            step()
            simTime += SIM_TIME_STEP
        }
    }

    def step(): Unit = {
        println("Simulation step " + simTime / SIM_TIME_STEP + " time: " + simTime)
        worldState((simTime + SIM_TIME_STEP) / SIM_TIME_STEP) = PhysicalSimulator.step(worldState(simTime / SIM_TIME_STEP));
    }
}
