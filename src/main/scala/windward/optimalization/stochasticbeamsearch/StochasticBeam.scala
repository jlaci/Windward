package windward.optimalization.stochasticbeamsearch

import java.util.Random

import windward.optimalization.strategies.ZStrategyEvaluator
import windward.simulation.units.{SimUnit, Coordinate}
import windward.simulation.{SimulationParameters, Simulator}
import windward.simulation.logical.domain.sailing.Sailboat
import windward.simulation.logical.domain.sailing.strategy.{ZStrategy, ZStrategyParameters}
import windward.simulation.physical.world.World

/**
 * Created by jlaci on 2015. 11. 16..
 */
class StochasticBeam(val simulator : Simulator,
                     val simulationParameters: SimulationParameters,
                     val searchParams: SearchParams) extends Runnable {

    var winner : ZStrategyParameters = null
    var winningScore = 0.0

    override def run(): Unit = {
        var finished = false
        var stepNumber : Int = 0
        var currentParams = searchParams.startingParams
        val rnd = new Random()
        var baseScore = simulationParameters.endTime.toDouble

        while(!finished) {
            stepNumber = stepNumber + 1
            if(stepNumber > searchParams.maxStep) {
                finished = true
            }

            val strategy1 = new ZStrategy(searchParams.goalPosition, shiftTurn1Up(currentParams))
            val strategy2 = new ZStrategy(searchParams.goalPosition, shiftTurn1Down(currentParams))
            val strategy3 = new ZStrategy(searchParams.goalPosition, shiftTurn2Up(currentParams))
            val strategy4 = new ZStrategy(searchParams.goalPosition, shiftTurn2Down(currentParams))
            val strategy5 = new ZStrategy(searchParams.goalPosition, increaseAlfa(currentParams))
            val strategy6 = new ZStrategy(searchParams.goalPosition, decreaseAlfa(currentParams))

            val s1Change = Math.abs(Math.min(0, evaluateStrategy(strategy1) - baseScore))
            val s2Change = Math.abs(Math.min(0, evaluateStrategy(strategy2) - baseScore))
            val s3Change = Math.abs(Math.min(0, evaluateStrategy(strategy3) - baseScore))
            val s4Change = Math.abs(Math.min(0, evaluateStrategy(strategy4) - baseScore))
            val s5Change = Math.abs(Math.min(0, evaluateStrategy(strategy5) - baseScore))
            val s6Change = Math.abs(Math.min(0, evaluateStrategy(strategy6) - baseScore))

            val sum = (s1Change + s2Change + s3Change + s4Change + s5Change + s6Change)

            if(sum == 0) {
                finished = true
            } else {
                val s1Prob = (s1Change / sum) * 100
                val s2Prob = (s2Change / sum) * 100
                val s3Prob = (s3Change / sum) * 100
                val s4Prob = (s4Change / sum) * 100
                val s5Prob = (s5Change / sum) * 100
                val s6Prob = (s6Change / sum) * 100

                val rand = rnd.nextDouble() % 100
                var start = 0.0

                if(start <= rand && rand < start + s1Prob){
                    currentParams = shiftTurn1Up(currentParams)
                }
                start += s1Prob

                if(start <= rand && rand < start + s2Prob){
                    currentParams = shiftTurn1Down(currentParams)
                }
                start += s2Prob

                if(start <= rand && rand < start + s3Prob){
                    currentParams = shiftTurn1Up(currentParams)
                }
                start += s3Prob

                if(start <= rand && rand < start + s4Prob){
                    currentParams = shiftTurn2Up(currentParams)
                }
                start += s4Prob

                if(start <= rand && rand < start + s5Prob){
                    currentParams = increaseAlfa(currentParams)
                }
                start += s5Prob

                if(start <= rand && rand < start + s6Prob){
                    currentParams = decreaseAlfa(currentParams)
                }
                start += s6Prob
            }
            val baseStrategy = new ZStrategy(searchParams.goalPosition, currentParams)
            baseScore = evaluateStrategy(baseStrategy)
        }

        winner = currentParams
        winningScore = evaluateStrategy(new ZStrategy(searchParams.goalPosition, winner))
    }

    private def evaluateStrategy(strategy : ZStrategy) : Double = {
        val sailboat = copySailboatWith(searchParams.startingSailboat, strategy)
        val states = for(sailboatState <- simulator.run(simulationParameters, List(sailboat), searchParams.startingState)._2) yield sailboatState(0)
        ZStrategyEvaluator.evaluate(states.toList)
    }

    private def shiftTurn1Up(strategy : ZStrategyParameters): ZStrategyParameters = {
        val l1 = Math.max(0, strategy.l1 - 0.01)
        new ZStrategyParameters(strategy.alfa, Math.max(l1, strategy.l2), Math.min(l1, strategy.l2))
    }

    private def shiftTurn1Down(strategy : ZStrategyParameters): ZStrategyParameters = {
        val l1 = Math.min(1, strategy.l1 + 0.01)
        new ZStrategyParameters(strategy.alfa, Math.max(l1, strategy.l2), Math.min(l1, strategy.l2))
    }

    private def shiftTurn2Up(strategy : ZStrategyParameters): ZStrategyParameters = {
        val l2 = Math.max(0, strategy.l2 - 0.01)
        new ZStrategyParameters(strategy.alfa, Math.max(l2, strategy.l1), Math.min(l2, strategy.l1))
    }

    private def shiftTurn2Down(strategy : ZStrategyParameters): ZStrategyParameters = {
        val l2 = Math.min(1, strategy.l2 + 0.01)
        new ZStrategyParameters(strategy.alfa, Math.max(l2, strategy.l1), Math.min(l2, strategy.l1))
    }

    private def increaseAlfa(strategy : ZStrategyParameters): ZStrategyParameters = {
        val alfa = Math.min(strategy.alfa + 1, searchParams.maxAlfa)
        new ZStrategyParameters(alfa, strategy.l1, strategy.l2)
    }

    private def decreaseAlfa(strategy : ZStrategyParameters): ZStrategyParameters = {
        val alfa = Math.max(strategy.alfa - 1, searchParams.minAlfa)
        new ZStrategyParameters(alfa, strategy.l1, strategy.l2)
    }

    private def copySailboatWith(sailboat : Sailboat, strategy : ZStrategy) : Sailboat = {
        new Sailboat(sailboat.posX, sailboat.posY, sailboat.params, sailboat.heading, sailboat.speed, sailboat.activeSail, strategy, sailboat.possibleActions, sailboat.ongoingActions)
    }
}

class SearchParams (val maxStep : Int,
                    val startingSailboat : Sailboat,
                    val startingState : World,
                    val startingParams: ZStrategyParameters,
                    val minAlfa : Int,
                    val maxAlfa : Int,
                    val goalPosition : Coordinate[SimUnit]){

}
