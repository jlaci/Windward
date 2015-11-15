package windward.simulation.logical.domain.sailing.polar

import com.github.tototoshi.csv.CSVReader
import windward.simulation.units.SimulationUnits

/**
 * Created by jlaci on 2015. 10. 13..
 */
object PolarCurveUtility {

    def createTestPolarCurve() : PolarCurve = {
        createSemiRealisticPolarCurve()
    }

    def loadPolarCurveForFile(filePath : String) : PolarCurve = {
        //Init the polar data array
        val polarData = new Array[Array[Float]](SimulationUnits.maxWindSpeed.toInt)
        for (windSpeed <- 0 until SimulationUnits.maxWindSpeed.toInt) {
            polarData(windSpeed) = new Array[Float](360);
        }

        val reader = CSVReader.open(filePath);

        //AWA; TWS; Max speed
        reader.foreach(line => {
            polarData(Integer.valueOf(line(1)))(Integer.valueOf(line(0))) = line(2).toFloat
        })

        new PolarCurve(polarData)
    }

    def createSemiRealisticPolarCurve() : PolarCurve = {
        val windwardCoefficientA = 0.3f
        val baseEfficiency = 0.7f
        val efficiencyDampening = 0.95f;

        val polarData = new Array[Array[Float]](SimulationUnits.maxWindSpeed.toInt)

        for (windSpeed <- 0 until SimulationUnits.maxWindSpeed.toInt) {
            polarData(windSpeed) = new Array[Float](360);
            for (windDirection <- 0 until 360) {
                if(windDirection < 15 || windDirection > 345) {
                    polarData(windSpeed)(windDirection) = 0.5f;
                } else {
                    val efficiency = baseEfficiency * Math.pow(efficiencyDampening, windSpeed)
                    polarData(windSpeed)(windDirection) = (1.9438 * (windSpeed  - (windSpeed * windwardCoefficientA * Math.abs(Math.cos(Math.toRadians(windDirection))))) * efficiency).toFloat

                }
            }
        }

        new PolarCurve(polarData)
    }

    def createUniformPolarCurve() : PolarCurve = {
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
