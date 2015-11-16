package windward.optimalization.strategies

import windward.optimalization.StrategyEvaluator
import windward.simulation.logical.domain.sailing.Sailboat
import windward.simulation.logical.domain.sailing.strategy.{ZStrategy, ZStrategyState}

/**
 * Created by jlaci on 2015. 11. 16..
 */
object ZStrategyEvaluator extends StrategyEvaluator[ZStrategy]{

    override def evaluate(states: List[Sailboat]): Double = {
        for((state, i) <- states.zipWithIndex) {
            val strategy = state.strategy.asInstanceOf[ZStrategy]
            if(strategy.state == ZStrategyState.Finished) {
                return i
            }
        }
        return Double.PositiveInfinity
    }

}
