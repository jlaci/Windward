package view

import java.awt.Color
import javax.swing.{BorderFactory, JFrame}

import physical.Simulator
import physical.world.World

import scala.swing.GridBagPanel.Fill
import scala.swing._


/**
 * Created by jlaci on 2015. 09. 18..
 */
class ViewWindow extends MainFrame{

	contents = new GridBagPanel {
    val cw = new Constraints();
    cw.weightx = 0.1
    cw.weighty = 0.1


		//The top menu
		val topMenu = new FlowPanel() {
      border = BorderFactory.createTitledBorder("Menu");

      contents += new Button() {
        text = "Simulation view"
      } += new Button() {
        text = "Global settings"
      }

		}
    cw.fill = Fill.Horizontal;
		layout(topMenu) = cw;

		//The main window (everything which is not the top menu)
		val mainPanel = new GridBagPanel() {
			val cm = new Constraints();
      cm.weightx = 0.1
      cm.weighty = 0.1

			//The left side of the main window
			val simPanel = new GridBagPanel() {
        border = BorderFactory.createTitledBorder("Simulation");
				val cs = new Constraints();
        cs.weightx = 0.1
        cs.weighty = 0.1


				//The actual view panel
				val simViewPanel = new SimulationViewPanel(convertData(Simulator.world, 0, 0, 32)) {
					preferredSize = new swing.Dimension(750, 750)
				}
				cs.fill = Fill.Both;
				layout(simViewPanel) = cs;

				//The simulation timeline control panel
				val simTimelinePanel = new FlowPanel() {
					border = BorderFactory.createTitledBorder("Simulation timeline");

          contents += new Button {
            text = "Start"
          } += new Button {
            text = "Stop"
          } += new Button {
            text = "Jump to begining"
          } += new Button() {
            text = "<"
          } += new TextField() {
            columns = 10
          } += new Button() {
            text = ">"
          } += new Button {
            text = "Jump to end"
          }
				}
				cs.fill = Fill.Horizontal;
				cs.gridy = 1;
				layout(simTimelinePanel) = cs;

			}
			cm.gridx = 0;
			cm.gridy = 0;
			cm.gridwidth = 2;
			cm.fill = Fill.Both;
			layout(simPanel) = cm;

			//The right side of the main window
			val simMenu = new TabbedPane() {
				border = BorderFactory.createTitledBorder("Simulation parameters");
			}
			cm.gridx = 2;
			cm.gridwidth = 1;
			layout(simMenu) = cm;

		}
    cw.fill = Fill.Both;
    cw.gridy = 1;
		layout(mainPanel) = cw;
	}

	size = new Dimension(1280, 960);
	resizable=false;
	peer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


	private def convertData(world: World, x : Int, y : Int, size : Int) : Array[Array[(Color, Int)]] = {
		val result : Array[Array[(Color, Int)]] = new Array[Array[(Color, Int)]](size);

    var viewX = x;
    if((world.cells.length - 1) < (x + size)) {
      viewX = world.cells.length - size;
    }

    var viewY = y;
    if((world.cells(0).length - 1) < (y + size)) {
      viewY = world.cells(0).length - size;
    }

		for(rowIndex <- viewX until (viewX + size)) {
			result(rowIndex - viewX) = new Array[(Color, Int)](size);
			for(columnIndex <- viewY until (viewY + size)) {
				val cell = world.cells(rowIndex)(columnIndex);
				result(rowIndex - viewX)(columnIndex - viewY) = (calculateColor(cell.windSpeed), cell.windDirection);
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
