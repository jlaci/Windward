package windward.simulation.logical.domain.sailing

import windward.simulation.logical.domain.sailing.polar.PolarCurveUtility
import windward.simulation.logical.domain.sailing.sail.{Sail, SailType}
import windward.simulation.logical.domain.sailing.strategy.actions.Turn
import windward.simulation.logical.domain.sailing.strategy.{DirectLineStrategy, SailingAction}
import windward.simulation.units.{Coordinate, SimUnit, SimulationUnits}

/**
 * Created by jlaci on 2015. 10. 13..
 */
object SailboatGenerator {

    def getTestSailboat(startPosX : SimUnit, startPosY : SimUnit, startHeading : Int) : Sailboat = {
        val sails = new Array[Sail](1)
        sails(0) = new Sail(SailType.Standard, PolarCurveUtility.createTestPolarCurve());

        val params = new SailboatParams(SimulationUnits.simUnitFromMeter(10), SimulationUnits.simUnitFromMeter(14), 30, 3000, sails);

        val strategy = new DirectLineStrategy(Coordinate.SimCoordsFromInt(100, 100));
        val possibleActions = List(new Turn(params.turnSpeed));
        val ongoingActions = List.empty[(SailingAction, Int)];

        new Sailboat(startPosX, startPosY, params, startHeading, 0 , 0, strategy, possibleActions, ongoingActions)
    }
}
