package windward.simulation.physical.world

import windward.simulation.physical.effects.CellEffect
import windward.simulation.units.CellUnit

/**
 * Created by jlaci on 2015. 09. 18..
 *
 * @param x The X world coordinate of this cell's top left corner.
 * @param y The Y world coordinate of this cell's top left corner.
 * @param windDirection The absolute direction of the wind in degrees, where 0 is north, 180 is south (from where does it blows).
 * @param windSpeed The speed of the wind in 0.1 m/s.
 */
class Cell(val x: CellUnit, val y: CellUnit, val windDirection: Int, val windSpeed: Int, val effects : Array[CellEffect]) {
    def this(x: CellUnit, y: CellUnit, windDirection : Int, windSpeed: Int) = {
        this(x, y, windDirection, windSpeed, null);
    }
}
