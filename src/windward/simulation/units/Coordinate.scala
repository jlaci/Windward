package windward.simulation.units

/**
 * @author Janoky Laszlo Viktor <janoky.laszlo@bmeautsoft.hu>
 */
class Coordinate[unitType] (val x : unitType, val y : unitType) {


}

object Coordinate {

    def SimCoords(x : SimUnit, y : SimUnit) : Coordinate[SimUnit] = {
        new Coordinate[SimUnit](x, y);
    }
}
