package windward.simulation.units

import breeze.linalg.DenseVector

/**
 * @author Janoky Laszlo Viktor <janoky.laszlo@bmeautsoft.hu>
 */
class Coordinate[unitType] (val x : unitType, val y : unitType) {

}

object Coordinate {

    def VectorFromSimCoords(coords : Coordinate[SimUnit]) : DenseVector[Double] = {
        DenseVector[Double](coords.x.value,coords.y.value)
    }

    def SimCoordsFromVector(v : DenseVector[Double]) : Coordinate[SimUnit] = {
        new Coordinate[SimUnit](new SimUnit(v.data(0)), new SimUnit(v.data(1)))
    }

    def SimCoordsFromInt(x : Int, y : Int) : Coordinate[SimUnit] = {
        new Coordinate[SimUnit](new SimUnit(x), new SimUnit(y))
    }

    def SimCoords(x : SimUnit, y : SimUnit) : Coordinate[SimUnit] = {
        new Coordinate[SimUnit](x, y)
    }
}
