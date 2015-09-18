package view

import java.awt.Color
import javax.swing.JFrame

import physical.world.{World}

import scala.swing.{Dimension, Frame}

/**
 * Created by jlaci on 2015. 09. 18..
 */
class ViewWindow (world : World) extends Frame{

  //Members
  val viewPanel : ViewPanel = new ViewPanel(convertData(world));

  //Default constructor
  contents = viewPanel;
  size = new Dimension(640, 640);
  peer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


  def dataChanged() : Unit = {
    viewPanel.data = convertData(world);
  }

  private def convertData(world: World) : Array[Array[Color]] = {
    val result : Array[Array[Color]] = new Array[Array[Color]](world.height);

    for(rowIndex <- 0 until world.cells.length) {
      result(rowIndex) = new Array[Color](world.cells(rowIndex).length);
      for(columnIndex <- 0 until world.cells(rowIndex).length) {
        result(rowIndex)(columnIndex) = calculateColor(world.cells(rowIndex)(columnIndex).windSpeed);
      }
    }

    result;
  }


  private def calculateColor(windSpeed : Int) : Color = {
    new Color(Math.min(windSpeed * 10,255), Math.min(windSpeed * 10,255), Math.min(windSpeed * 10,255));
  }
}
