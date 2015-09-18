package physical.world

/**
 * Created by jlaci on 2015. 09. 18..
 */
class World (val width : Int, val height : Int, val cells : Array[Array[Cell]]) {

  def this(width : Int, height : Int) = {
    this(width, height, new Array[Array[Cell]](height));
    for(rowIndex <- 0 until cells.length) {
      cells(rowIndex) = new Array[Cell](width);
      for(columnIndex <- 0 until cells(rowIndex).length) {
        cells(rowIndex)(columnIndex) = new Cell(rowIndex * CELL_SIZE, columnIndex * CELL_SIZE, 0, 0);
      }
    }
  }
}
