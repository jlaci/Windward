package windward.simulation.units

/**
 * Created by jlaci on 2015. 09. 29..
 */
class SimUnit (val value : Float) extends AnyVal {

    def ==(x : scala.Byte) : Boolean = {
        value == x;
    }

    def ==(x : scala.Short) : Boolean = {
        value == x;
    }

    def ==(x : scala.Int) : Boolean = {
        value == x;
    }

    def toCellUnit() : CellUnit = {
        new CellUnit((value / SimulationUnits.tileSizeInSimUnits).toInt)
    }

    def toMeters() : Float = {
        value * SimulationUnits.simUnitInMeters
    }

    def toFloat() : Float = {
        value
    }

    def toInt() : Int = {
        value.toInt
    }

}
