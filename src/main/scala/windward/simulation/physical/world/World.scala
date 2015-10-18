package windward.simulation.physical.world

import windward.simulation.physical.weather.WeatherMap
import windward.simulation.units.{CellUnit, SimUnit}

/**
 * Created by jlaci on 2015. 09. 18..
 */
class World(val width: SimUnit, val height: SimUnit, val cells: Array[Array[Cell]]) {

    def this(width: SimUnit, height: SimUnit, weatherMap: WeatherMap) = {
        this(width, height, new Array[Array[Cell]](height.toCellUnit.toInt));
        for (rowIndex <- 0 until cells.length) {
            cells(rowIndex) = new Array[Cell](width.toCellUnit.toInt);
            for (columnIndex <- 0 until cells(rowIndex).length) {
                cells(rowIndex)(columnIndex) = new Cell(new CellUnit(rowIndex * CELL_SIZE), new CellUnit(columnIndex * CELL_SIZE), weatherMap.direction(rowIndex)(columnIndex), weatherMap.speed(rowIndex)(columnIndex));
            }
        }
    }

    def this(width: SimUnit, height: SimUnit) = {
        this(width, height, new Array[Array[Cell]](height.toCellUnit.toInt));
        for (rowIndex <- 0 until cells.length) {
            cells(rowIndex) = new Array[Cell](width.toCellUnit.toInt);
            for (columnIndex <- 0 until cells(rowIndex).length) {
                cells(rowIndex)(columnIndex) = new Cell(new CellUnit(rowIndex * CELL_SIZE), new CellUnit(columnIndex * CELL_SIZE), 0, 0);
            }
        }
    }

    def getCellsInRadius(x : CellUnit, y : CellUnit, r: CellUnit): List[Cell] = {
        def distance(x0: Int, y0: Int, x1: Int, y1: Int) = {
            Math.sqrt(Math.pow(x0 - x.toInt, 2) + Math.pow(y0 - y.toInt, 2));
        }

        val xStart = Math.max(0, x.toInt - r.toInt);
        val yStart = Math.max(0, y.toInt - r.toInt);

        val xEnd = Math.min(width.toCellUnit.toInt, (x + r).toInt);
        val yEnd = Math.min(width.toCellUnit.toInt, (y + r).toInt);

        var result = List[Cell]();
        for (rowIndex <- xStart until xEnd) {
            for (columnIndex <- yStart until yEnd) {
                val cell = cells(rowIndex)(columnIndex);
                if(distance(x.toInt, y.toInt, cell.x.toInt, cell.y.toInt) >= r.toInt) {
                    result ::= cell;
                }
            }
        }

        result;
    }
}
