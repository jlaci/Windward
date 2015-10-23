package windward.simulation.units

/**
 * Created by jlaci on 2015. 09. 28..
 */
object SimulationUnits {

    var simUnitInMeters : Float = 0.25f;
    var tileSizeInSimUnits : Float = 16f;
    var timeStepInMilliseconds : Int = 500;
    var windSpeedInMeterPerSec = 0.1f;
    var maxWindSpeed = 30f;

    def simUnitFromMeter(meters : Float) : SimUnit = {
        return new SimUnit((meters / simUnitInMeters).toFloat);
    }

    def simUnitFromMeter(meters : Double) : SimUnit = {
        return new SimUnit((meters / simUnitInMeters).toFloat);
    }

}
