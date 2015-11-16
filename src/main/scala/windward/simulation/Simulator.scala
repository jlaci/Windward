package windward.simulation

import windward.simulation.logical.LogicalSimulator
import windward.simulation.logical.domain.sailing.Sailboat
import windward.simulation.physical.PhysicalSimulator
import windward.simulation.physical.world.World

/**
 * Created by jlaci on 2015. 11. 16..
 */
class Simulator {

    var simTime: Int = 0;

    var worldState: Array[World] = null
    var sailboats: Array[Array[Sailboat]] = null
    var simulationParameters : SimulationParameters = null

    def run(parameters: SimulationParameters, startingSailboats : List[Sailboat], startingWorldState : World) : (Array[World],Array[Array[Sailboat]]) = {
        init(parameters, startingSailboats, startingWorldState)
        start()
        val result = (worldState, sailboats)
        worldState = null
        sailboats = null
        simulationParameters = null
        simTime = 0
        result
    }

    def init(parameters: SimulationParameters, startingSailboats : List[Sailboat], startingWorldState : World): Unit = {
        simulationParameters = parameters

        worldState = new Array[World](parameters.endTime + 1)
        worldState(0) = startingWorldState

        sailboats = new Array[Array[Sailboat]](parameters.endTime + 1)
        sailboats(0) = startingSailboats.toArray
    }

    def start(): Unit = {
        while (simTime < simulationParameters.endTime) {
            step()
            simTime += 1
        }
    }

    def step(): Unit = {
        worldState(simTime + 1) = PhysicalSimulator.step(worldState(simTime))
        sailboats(simTime + 1) = LogicalSimulator.step(worldState(simTime), sailboats(simTime))
    }

}
