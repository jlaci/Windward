package windward.simulation.units

/**
 * Created by jlaci on 2015. 09. 29..
 */
class SimUnit (val value : Double) extends AnyVal {

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

    def toMeters() : Double = {
        value * SimulationUnits.simUnitInMeters
    }

    def toFloat() : Float = {
        value.toFloat
    }

    def toInt() : Int = {
        value.toInt
    }

}
