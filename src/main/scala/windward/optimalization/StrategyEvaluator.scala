package windward.optimalization

import windward.simulation.logical.Strategy
import windward.simulation.logical.domain.sailing.Sailboat
import windward.simulation.logical.domain.sailing.strategy.SailingAction

/**
 * Created by jlaci on 2015. 11. 16..
 */
abstract class StrategyEvaluator[strategy <: Strategy[Sailboat, SailingAction]] {
    def evaluate(states : List[Sailboat]) : Double
}
