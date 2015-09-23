package view

import java.awt.Color
import javax.swing.{BorderFactory, JFrame}

import physical.world.World

import scala.swing.GridBagPanel.Fill
import scala.swing._


/**
 * Created by jlaci on 2015. 09. 18..
 */
class ViewWindow (world : World) extends MainFrame{

  val viewPanel : ViewPanel = new ViewPanel(convertData(world));

  contents = new GridBagPanel(){
    viewPanel.preferredSize = new Dimension(900, 900);

    val c = new Constraints;

    c.gridx = 0
    c.gridy = 0
    c.weightx = 0.8;
    layout(viewPanel) = c;

    c.gridx = 1;
    c.weightx = 0.2;
    c.fill = Fill.Both
    layout(new FlowPanel() {
      border = BorderFactory.createTitledBorder("Menu");
    }) = c;
  }

  size = new Dimension(1280, 960);
  resizable=false;
  peer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


  def dataChanged() : Unit = {
    viewPanel.windData = convertData(world);
  }

  private def convertData(world: World) : Array[Array[(Color, Int)]] = {
    val result : Array[Array[(Color, Int)]] = new Array[Array[(Color, Int)]](world.height);

    for(rowIndex <- 0 until world.cells.length) {
      result(rowIndex) = new Array[(Color, Int)](world.cells(rowIndex).length);
      for(columnIndex <- 0 until world.cells(rowIndex).length) {
        val cell = world.cells(rowIndex)(columnIndex);
        result(rowIndex)(columnIndex) = (calculateColor(cell.windSpeed), cell.windDirection);
      }
    }

    result;
  }

  private def calculateColor(windSpeed : Int) : Color = {
    //new Color(Math.min(windSpeed,255), Math.min(windSpeed,255), Math.min(windSpeed,255));
    //Windspeed is valid from 0 to 300
    Color.getHSBColor(windSpeed / 300f, 0.5f, 1);

  }
}
