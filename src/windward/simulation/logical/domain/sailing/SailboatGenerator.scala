package windward.simulation.logical.domain.sailing

import windward.simulation.logical.domain.sailing.polar.PolarCurveUtility
import windward.simulation.logical.domain.sailing.sail.{SailType, Sail}
import windward.simulation.units.{CellUnit, SimulationUnits, SimUnit}

/**
 * Created by jlaci on 2015. 10. 13..
 */
object SailboatGenerator {

    def getTestSailboat(startPosX : SimUnit, startPosY : SimUnit, startHeading : Int) : Sailboat = {
        val sails = new Array[Sail](1)
        sails(0) = new Sail(SailType.Standard, PolarCurveUtility.createTestPolarCurve());

        new Sailboat(startPosX, startPosY, SimulationUnits.simUnitFromMeter(10), SimulationUnits.simUnitFromMeter(14), startHeading, 0 , sails)
    }
}
