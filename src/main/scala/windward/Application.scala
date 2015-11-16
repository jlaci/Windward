package windward

import windward.optimalization.stochasticbeamsearch.ZStrategySBSOptimizer
import windward.simulation.SimulationParameters
import windward.simulation.logical.domain.sailing.{Sailboat, SailboatGenerator}
import windward.simulation.physical.WorldBuilder
import windward.simulation.units.{Coordinate, SimUnit, SimulationUnits}


/**
 * Created by jlaci on 2015. 09. 18..
 */
object Application {

    def main(args: Array[String]): Unit = {
        println("Windward starting.")

        val params = new SimulationParameters(250, SimulationUnits.simUnitFromMeter(256), SimulationUnits.simUnitFromMeter(256))

        val sailboats = List[Sailboat](SailboatGenerator.getZStrategySailboat(new SimUnit(512), new SimUnit(960), 45, new SimUnit(512), new SimUnit(0)))

        val startingWorldState = WorldBuilder.createWorldWitGradientWind(params.worldWidth, params.worldHeight, 350, 30);
        //worldState(0) = WorldBuilder.createWorldWithUniformWind(parameters.worldWidth, parameters.worldHeight, 315, 20);
        //worldState(0) = WorldBuilder.createWorldWithRandomWind(parameters.worldWidth, parameters.worldHeight);


        ZStrategySBSOptimizer.optimizeZStrategy(sailboats(0), startingWorldState, new Coordinate[SimUnit](new SimUnit(512), new SimUnit(0)))

        /*GlobalSimulator.init(params, sailboats, startingWorldState)

        val window: ViewWindow = new ViewWindow()
        window.visible = true

        GlobalSimulator.start()*/

    }
}
