package windward

import windward.optimalization.stochasticbeamsearch.ZStrategySBSOptimizer
import windward.optimalization.strategies.ZStrategyEvaluator
import windward.simulation.{GlobalSimulator, SimulationParameters}
import windward.simulation.logical.domain.sailing.strategy.ZStrategy
import windward.simulation.logical.domain.sailing.{Sailboat, SailboatGenerator}
import windward.simulation.physical.WorldBuilder
import windward.simulation.units.{Coordinate, SimUnit, SimulationUnits}
import windward.view.ViewWindow


/**
 * Created by jlaci on 2015. 09. 18..
 */
object Application {

    def main(args: Array[String]): Unit = {
        println("Windward starting.")

        val startPos = (512, 960)
        val startAngle = 359

        val goalPos = (512, 0)
        val windDir = 350

        val params = new SimulationParameters(250, SimulationUnits.simUnitFromMeter(256), SimulationUnits.simUnitFromMeter(256))
        val sailboats = List[Sailboat](SailboatGenerator.getZStrategySailboat(new SimUnit(startPos._1), new SimUnit(startPos._2), startAngle, new SimUnit(goalPos._1), new SimUnit(goalPos._2)))
        val startingWorldState = WorldBuilder.createWorldWitGradientWind(params.worldWidth, params.worldHeight, windDir, 30)


        val states = for(sailboatState <- GlobalSimulator.run(params, sailboats, startingWorldState)._2) yield sailboatState(0)
        val baselineScore = ZStrategyEvaluator.evaluate(states.toList)


        val winner = ZStrategySBSOptimizer.optimizeZStrategy(sailboats(0), startingWorldState, new Coordinate[SimUnit](new SimUnit(goalPos._1), new SimUnit(goalPos._2)))
        println("Baseline score is " + baselineScore)

        val resultSailboat = Sailboat.copySailboatWith(sailboats(0), new ZStrategy(new Coordinate[SimUnit](new SimUnit(goalPos._1), new SimUnit(goalPos._2)), winner))
        GlobalSimulator.init(params, List(resultSailboat), startingWorldState)
        val window: ViewWindow = new ViewWindow()
        window.visible = true
        GlobalSimulator.start()


        //worldState(0) = WorldBuilder.createWorldWithUniformWind(parameters.worldWidth, parameters.worldHeight, 315, 20);
        //worldState(0) = WorldBuilder.createWorldWithRandomWind(parameters.worldWidth, parameters.worldHeight);

        /*GlobalSimulator.init(params, sailboats, startingWorldState)

        val window: ViewWindow = new ViewWindow()
        window.visible = true

        GlobalSimulator.start()*/

    }
}
