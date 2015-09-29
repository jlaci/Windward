package windward.simulation.units

/**
 * Created by jlaci on 2015. 09. 29..
 */
class CellUnit(val value : Int) extends AnyVal {

    def toInt() : Int = {
        value
    }

    def +(x : CellUnit) : CellUnit = {
        new CellUnit(value + x.value)
    }

    def ==(x : CellUnit) : scala.Boolean = {
        value == x.value
    }

    def !=(x : CellUnit) : scala.Boolean = {
        value != x.value
    }

    def <(x : CellUnit) : scala.Boolean = {
        value < x.value
    }

    def <=(x : CellUnit) : scala.Boolean = {
        value <= x.value
    }

    def >(x : CellUnit) : scala.Boolean = {
        value > x.value
    }

    def >=(x : CellUnit) : scala.Boolean = {
        value >= x.value
    }

}
