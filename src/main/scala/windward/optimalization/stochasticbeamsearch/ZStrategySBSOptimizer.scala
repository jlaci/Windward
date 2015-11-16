package windward.optimalization.stochasticbeamsearch

import java.util.Random

import windward.simulation.{GlobalSimulator, SimulationParameters, Simulator}
import windward.simulation.logical.domain.sailing.Sailboat
import windward.simulation.logical.domain.sailing.strategy.{ZStrategy, ZStrategyParameters}
import windward.simulation.physical.world.World
import windward.simulation.units.{SimUnit, Coordinate}
import windward.view.ViewWindow

/**
 * Created by jlaci on 2015. 11. 16..
 */
object ZStrategySBSOptimizer {

    //Strategy search boundries
    val alfaMin = 10
    val alfaMax = 180

    //Search parameters
    val beams = 100
    val maxStep = 100
    val simSteps = 250

    def optimizeZStrategy(startingSailboat : Sailboat, startingState : World, goalPosition : Coordinate[SimUnit]): Unit = {
        val simParams = new SimulationParameters(simSteps, startingState.width, startingState.height)

        var currentWinnerScore = simSteps.toDouble
        var currentWinner : ZStrategyParameters = null

        for(beamNumber <- 1 to beams) {
            val searchParams = new SearchParams(maxStep, startingSailboat, startingState, generateRandomStartingParam(), alfaMin, alfaMax, goalPosition)
            println("Starting beam " + beamNumber)
            val beam = new StochasticBeam(new Simulator(), simParams, searchParams)
            beam.run()

            if(beam.winningScore < currentWinnerScore) {
                currentWinnerScore = beam.winningScore
                currentWinner = beam.winner
            }
        }

        println("Result score: " + currentWinnerScore + " for " + currentWinner)

        val resultSailboat = Sailboat.copySailboatWith(startingSailboat, new ZStrategy(goalPosition, currentWinner))
        GlobalSimulator.init(simParams, List(resultSailboat), startingState)
        val window: ViewWindow = new ViewWindow()
        window.visible = true
        GlobalSimulator.start()
    }


    def generateRandomStartingParam(): ZStrategyParameters ={
        val rnd = new Random()
        val alfa = rnd.nextInt(alfaMax - alfaMin) + alfaMin
        val a = rnd.nextInt(100)
        val b = rnd.nextInt(100)

        val a1 = a/100.0
        val b1 = b/100.0

        val l2 = Math.min(a1, b1)
        val l1 = Math.max(a1, b1)

        new ZStrategyParameters(alfa, l1, l2)
    }
}
