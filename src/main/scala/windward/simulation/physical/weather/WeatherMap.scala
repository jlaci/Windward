package windward.simulation.physical.weather

import windward.simulation.units.CellUnit

/**
 * @author Janoky Laszlo Viktor <janoky.laszlo@bmeautsoft.hu>
 */
class WeatherMap(var speed: Array[Array[Float]], var direction: Array[Array[Int]]) {

    def getDirection(x: CellUnit, y : CellUnit) : Int = {
        direction(x.value)(y.value)
    }

    def getSpeed(x: CellUnit, y : CellUnit) : Float = {
        speed(x.value)(y.value)
    }
}
