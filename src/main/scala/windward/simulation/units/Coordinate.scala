package windward.simulation.units

/**
 * @author Janoky Laszlo Viktor <janoky.laszlo@bmeautsoft.hu>
 */
class Coordinate[unitType] (val x : unitType, val y : unitType) {


}

object Coordinate {

    def SimCoordsFromInt(x : Int, y : Int) : Coordinate[SimUnit] = {
        new Coordinate[SimUnit](new SimUnit(x), new SimUnit(y));
    }

    def SimCoords(x : SimUnit, y : SimUnit) : Coordinate[SimUnit] = {
        new Coordinate[SimUnit](x, y);
    }
}
