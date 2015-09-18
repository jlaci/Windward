package physical

/**
 * Created by jlaci on 2015. 09. 18..
 */
class World (val width : Int, val height : Int, val cells : Array[Array[Cell]]) {

  for(rowIndex <- 0 to cells.length) {
    for(columnIndex <- 0 to cells(rowIndex).length) {
      cells(rowIndex)(columnIndex) = new Cell(rowIndex, columnIndex, 0, 0);
    }
  }

  def this(width : Int, height : Int) = {
    this(width, height, new Array[Array[Cell]](height));
    for(rowIndex <- 0 to cells.length) {
      cells(rowIndex) = new Array[Cell](width);
    }
  }

}
