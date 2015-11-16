package windward.simulation

import windward.simulation.logical.domain.sailing.Sailboat
import windward.simulation.physical.world.World
import windward.simulation.units.SimulationUnits


/**
 * Created by jlaci on 2015. 09. 25..
 */
object GlobalSimulator {

    var simulator: Simulator = new Simulator()

    def run(parameters: SimulationParameters, startingSailboats : List[Sailboat], startingWorldState : World) : (Array[World],Array[Array[Sailboat]]) = {
        simulator.run(parameters, startingSailboats, startingWorldState)
    }

    def init(parameters: SimulationParameters, startingSailboats : List[Sailboat], startingWorldState : World): Unit = {
       simulator.init(parameters, startingSailboats, startingWorldState)
    }

    def start(): Unit = {
        println("Starting simulation")
        simulator.start()
    }

    def step(): Unit = {
        println("Simulation step " + simulator.simTime + " time: " + ( simulator.simTime * SimulationUnits.timeStepInMilliseconds / 1000) + "s")
        simulator.step()
    }
}
