package windward.simulation.logical.domain.sailing.polar

import windward.simulation.units.SimulationUnits

/**
 * Created by jlaci on 2015. 10. 13..
 */
object PolarCurveUtility {

    def createTestPolarCurve() : PolarCurve = {
        val polarData = new Array[Array[Float]](SimulationUnits.maxWindSpeed.toInt)

        for (windSpeed <- 0 until SimulationUnits.maxWindSpeed.toInt) {
            polarData(windSpeed) = new Array[Float](360);
            for (windDirection <- 0 until 360) {
                if(windDirection < 30 || windDirection > 330) {
                    polarData(windSpeed)(windDirection) = 0;
                } else {
                    polarData(windSpeed)(windDirection) = windSpeed * 0.6f;
                }
            }
        }

        new PolarCurve(polarData)
    }
}
