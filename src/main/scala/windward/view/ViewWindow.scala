package windward.view

import java.awt.event.{FocusEvent, FocusListener}
import java.text.DecimalFormat
import javax.swing.SpringLayout.Constraints
import javax.swing.{JLabel, BorderFactory, JFrame}

import windward.simulation.Simulator
import windward.simulation.physical.{PhysicsUtility, PhysicalSimulator}

import scala.swing.GridBagPanel.Fill
import scala.swing._
import scala.swing.event.{ButtonClicked, Key, KeyPressed}


/**
 * Created by jlaci on 2015. 09. 18..
 */
class ViewWindow extends MainFrame {

    var simViewPanel : SimulationViewPanel = null
    var viewXCoordTF : TextField = null
    var viewYCoordTF : TextField = null
    var viewSizeTF : TextField = null

    var boatXCoordTF : TextField = null
    var boatYCoordTF : TextField = null
    var boatSpeedTF : TextField = null
    var boatHeadingTF : TextField = null
    var boatSailTF : TextField = null
    var trueWindSpeedTF : TextField = null
    var trueWindDirTF : TextField = null
    var appWindSpeedTF : TextField = null
    var appWindDirTF : TextField = null

    def getSailboat() = {
        Simulator.sailboats(simViewPanel.viewSimStep)(0)
    }

    def getTrueWindSpeed = {
        val sailboat = getSailboat()
        sailboat.getAverageWindSpeed(sailboat.getEffectingCells(Simulator.worldState(simViewPanel.viewSimStep)))
    }

    def getTrueWindDir = {
        val sailboat = getSailboat()
        sailboat.getRelativeWindDirection(sailboat.getAverageWindDirection(sailboat.getEffectingCells(Simulator.worldState(simViewPanel.viewSimStep))))
    }

    def getAppWindSpeed = {
        val sailboat = getSailboat()
        sailboat.getApparentWindSpeed(sailboat.getAverageWindDirection(sailboat.getEffectingCells(Simulator.worldState(simViewPanel.viewSimStep))), getTrueWindSpeed)
    }

    def getAppWindDir = {
        val sailboat = getSailboat()
        sailboat.getApparentWindDirection(getTrueWindSpeed, getTrueWindDir)
    }

    def viewDataChanged = {
        viewXCoordTF.text = "X : " + simViewPanel.x
        viewYCoordTF.text = "Y : " + simViewPanel.y
        viewSizeTF.text = "S: " + simViewPanel.viewSize
    }

    val formatter = new DecimalFormat("#.##")

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
                simViewPanel = new SimulationViewPanel(Simulator.simulationParameters.worldWidth.toCellUnit().toInt() / 2, Simulator.simulationParameters.worldHeight.toCellUnit().toInt() / 2, 16) {
                    preferredSize = new swing.Dimension(750, 750)
                }
                cs.anchor = GridBagPanel.Anchor.Center
                layout(simViewPanel) = cs;

                //The simulation timeline control panel
                val simTimelinePanel = new FlowPanel() {
                    border = BorderFactory.createTitledBorder("Simulation timeline");

                    val timeStepTF = new TextField() {
                        columns = 10
                        text = simViewPanel.viewSimStep + " / " + Simulator.simulationParameters.endTime
                    }

                    def simStepChanged = {
                        simViewPanel.repaint()
                        timeStepTF.text = simViewPanel.viewSimStep + " / " + Simulator.simulationParameters.endTime
                        boatXCoordTF.text = Simulator.sailboats(simViewPanel.viewSimStep)(0).posX.toFloat().toString
                        boatYCoordTF.text = Simulator.sailboats(simViewPanel.viewSimStep)(0).posY.toFloat().toString
                        boatSpeedTF.text = formatter.format(PhysicsUtility.knotsFromMeterPreSecond(Simulator.sailboats(simViewPanel.viewSimStep)(0).speed)) + " kts"
                        boatHeadingTF.text = Simulator.sailboats(simViewPanel.viewSimStep)(0).heading.toString
                        boatSailTF.text = Simulator.sailboats(simViewPanel.viewSimStep)(0).params.sails(Simulator.sailboats(simViewPanel.viewSimStep)(0).activeSail).sailType.toString
                        trueWindSpeedTF.text = formatter.format(getTrueWindSpeed) + " m/s"
                        trueWindDirTF.text = formatter.format(getTrueWindDir)
                        appWindSpeedTF.text = formatter.format(getAppWindSpeed) + " m/s"
                        appWindDirTF.text = formatter.format(getAppWindDir)
                    }

                    contents += new Button {
                        text = "Start"
                    } += new Button {
                        text = "Stop"
                    } += new Button {
                        text = "Jump to beginning"
                        reactions += {
                            case ButtonClicked(_) => {
                                simViewPanel.viewSimStep = 0;
                                simStepChanged
                            };
                        }
                    } += new Button() {
                        text = "<"
                        reactions += {
                            case ButtonClicked(_) => {
                                if (simViewPanel.viewSimStep > 0) {
                                    simViewPanel.viewSimStep -= 1;
                                    simStepChanged
                                }
                            };
                        }
                    } += timeStepTF += new Button() {
                        text = ">"
                        reactions += {
                            case ButtonClicked(_) => {
                                if (simViewPanel.viewSimStep < Simulator.simulationParameters.endTime) {
                                    simViewPanel.viewSimStep += 1;
                                    simStepChanged
                                }
                            };
                        };
                    } += new Button {
                        text = "Jump to end"
                        reactions += {
                            case ButtonClicked(_) => {
                                simViewPanel.viewSimStep = Simulator.simulationParameters.endTime;
                                simStepChanged
                            };
                        }
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
            //Data about the sim view panel
            val dataPanel = new GridBagPanel() {
                border = BorderFactory.createTitledBorder("Simulation data");

                val dpc = new Constraints()
                dpc.anchor = GridBagPanel.Anchor.North
                dpc.fill = GridBagPanel.Fill.Horizontal
                dpc.weightx = 1
     
                val viewDataPanel = new GridBagPanel() {
                    border = BorderFactory.createTitledBorder("View");

                    val vdpc = new Constraints()
                    vdpc.grid = (2, 3)
                    vdpc.fill = GridBagPanel.Fill.Horizontal
                    vdpc.weightx = 0.5;

                    //X Coordinate
                    val xCoordLabel = new Label("View X:") {
                        horizontalAlignment = Alignment.Left
                    }
                    vdpc.gridx = 0
                    vdpc.gridy = 0
                    layout(xCoordLabel) = vdpc

                    viewXCoordTF = new TextField() {
                        columns = 5
                        text = simViewPanel.x.toString
                    }
                    vdpc.gridx = 1
                    layout(viewXCoordTF) = vdpc

                    //Y coordinate
                    val yCoordLabel = new Label("View Y:") {
                        horizontalAlignment = Alignment.Left
                    }
                    vdpc.gridx = 0
                    vdpc.gridy = 1
                    layout(yCoordLabel) = vdpc

                    viewYCoordTF = new TextField() {
                        columns = 5
                        text = simViewPanel.y.toString
                    }
                    vdpc.gridx = 1
                    layout(viewYCoordTF) = vdpc


                    //Size
                    val sizeLabel = new Label("View size:") {
                        horizontalAlignment = Alignment.Left
                    }
                    vdpc.gridx = 0
                    vdpc.gridy = 2
                    layout(sizeLabel) = vdpc

                    viewSizeTF = new TextField() {
                        columns = 5
                        text = simViewPanel.viewSize.toString
                    }
                    vdpc.gridx = 1
                    layout(viewSizeTF) = vdpc
                }
                dpc.gridy = 0
                layout(viewDataPanel) = dpc

                val boatDataPanel = new GridBagPanel() {
                    border = BorderFactory.createTitledBorder("Boat");

                    val bdpc = new Constraints()
                    bdpc.grid = (2, 9)
                    bdpc.fill = GridBagPanel.Fill.Horizontal
                    bdpc.weightx = 0.5;

                    //X Coordinate
                    val xCoordLabel = new Label("Boat X:") {
                        horizontalAlignment = Alignment.Left
                    }
                    bdpc.gridx = 0
                    bdpc.gridy = 0
                    layout(xCoordLabel) = bdpc

                    boatXCoordTF = new TextField() {
                        columns = 5
                        text = Simulator.sailboats(simViewPanel.viewSimStep)(0).posX.toFloat().toString
                    }
                    bdpc.gridx = 1
                    layout(boatXCoordTF) = bdpc

                    //Y coordinate
                    val yCoordLabel = new Label("Boat Y:") {
                        horizontalAlignment = Alignment.Left
                    }
                    bdpc.gridx = 0
                    bdpc.gridy = 1
                    layout(yCoordLabel) = bdpc

                    boatYCoordTF = new TextField() {
                        columns = 5
                        text = Simulator.sailboats(simViewPanel.viewSimStep)(0).posY.toFloat().toString
                    }
                    bdpc.gridx = 1
                    layout(boatYCoordTF) = bdpc


                    //Speed
                    val speedLabel = new Label("Speed:") {
                        horizontalAlignment = Alignment.Left
                    }
                    bdpc.gridx = 0
                    bdpc.gridy = 2
                    layout(speedLabel) = bdpc

                    boatSpeedTF = new TextField() {
                        columns = 5
                        text = formatter.format(PhysicsUtility.knotsFromMeterPreSecond(Simulator.sailboats(simViewPanel.viewSimStep)(0).speed)) + " kts"
                    }
                    bdpc.gridx = 1
                    layout(boatSpeedTF) = bdpc

                    //Heading
                    val headingLabel = new Label("Heading:") {
                        horizontalAlignment = Alignment.Left
                    }
                    bdpc.gridx = 0
                    bdpc.gridy = 3
                    layout(headingLabel) = bdpc

                    boatHeadingTF = new TextField() {
                        columns = 5
                        text = Simulator.sailboats(simViewPanel.viewSimStep)(0).heading.toString
                    }
                    bdpc.gridx = 1
                    layout(boatHeadingTF) = bdpc

                    //Sail
                    val sailLabel = new Label("Sail:") {
                        horizontalAlignment = Alignment.Left
                    }
                    bdpc.gridx = 0
                    bdpc.gridy = 4
                    layout(sailLabel) = bdpc

                    boatSailTF = new TextField() {
                        columns = 5
                        text = Simulator.sailboats(simViewPanel.viewSimStep)(0).params.sails(Simulator.sailboats(simViewPanel.viewSimStep)(0).activeSail).sailType.toString
                    }
                    bdpc.gridx = 1
                    layout(boatSailTF) = bdpc

                    //True Windspeed
                    val windSpeedLabel = new Label("TWS:") {
                        horizontalAlignment = Alignment.Left
                    }
                    bdpc.gridx = 0
                    bdpc.gridy = 5
                    layout(windSpeedLabel) = bdpc

                    trueWindSpeedTF = new TextField() {
                        columns = 5
                        text = formatter.format(getTrueWindSpeed) + " m/s"
                    }
                    bdpc.gridx = 1
                    layout(trueWindSpeedTF) = bdpc

                    //Relative wind direction
                    val windDirLabel = new Label("TWD:") {
                        horizontalAlignment = Alignment.Left
                    }
                    bdpc.gridx = 0
                    bdpc.gridy = 6
                    layout(windDirLabel) = bdpc

                    trueWindDirTF = new TextField() {
                        columns = 5
                        text = formatter.format(getTrueWindDir)
                    }
                    bdpc.gridx = 1
                    layout(trueWindDirTF) = bdpc

                    //Apparent Windspeed
                    val apparentWindSpeedLabel = new Label("AWS:") {
                        horizontalAlignment = Alignment.Left
                    }
                    bdpc.gridx = 0
                    bdpc.gridy = 7
                    layout(apparentWindSpeedLabel) = bdpc

                    appWindSpeedTF = new TextField() {
                        columns = 5
                        text = formatter.format(getAppWindSpeed) + " m/s"
                    }
                    bdpc.gridx = 1
                    layout(appWindSpeedTF) = bdpc

                    //Apparent wind angle
                    val apparentWindDirLabel = new Label("AWA:") {
                        horizontalAlignment = Alignment.Left
                    }
                    bdpc.gridx = 0
                    bdpc.gridy = 8
                    layout(apparentWindDirLabel) = bdpc

                    appWindDirTF = new TextField() {
                        columns = 5
                        text = formatter.format(getAppWindDir)
                    }
                    bdpc.gridx = 1
                    layout(appWindDirTF) = bdpc

                }
                dpc.gridy = 1
                layout(boatDataPanel) = dpc

                val emptySpaceFiller = new Panel {}
                dpc.fill = GridBagPanel.Fill.Both
                dpc.gridy = 2
                dpc.weighty = 100
                layout(emptySpaceFiller) = dpc
            }
            cm.gridx = 2;
            cm.gridwidth = 1;
            layout(dataPanel) = cm;

        }
        cw.fill = Fill.Both;
        cw.gridy = 1;
        layout(mainPanel) = cw;

        //Event listeners
        listenTo(keys)
        reactions += {
            case KeyPressed(_, Key.Up, _, _) => {
                if (simViewPanel.y > 0) {
                    simViewPanel.y -= 1;
                    viewDataChanged
                    repaint()
                }
            }
            case KeyPressed(_, Key.Left, _, _) => {
                if(simViewPanel.x > 0) {
                    simViewPanel.x -= 1;
                    viewDataChanged
                    repaint()
                }
            }
            case KeyPressed(_, Key.Down, _, _) => {
                if(simViewPanel.y < Simulator.simulationParameters.worldHeight.toCellUnit().toInt() - simViewPanel.viewSize) {
                    simViewPanel.y += 1;
                    viewDataChanged
                    repaint()
                }
            }
            case KeyPressed(_, Key.Right, _, _) => {
                if(simViewPanel.x < Simulator.simulationParameters.worldWidth.toCellUnit().toInt() - simViewPanel.viewSize) {
                    simViewPanel.x += 1;
                    viewDataChanged
                    repaint()
                }
            }
            case KeyPressed(_, Key.Subtract, _, _) => {
                if(simViewPanel.viewSize < Math.min(32, Math.max(Simulator.simulationParameters.worldWidth.toCellUnit().toInt(), Simulator.simulationParameters.worldHeight.toCellUnit().toInt()))) {
                    simViewPanel.viewSize += 1;
                    viewDataChanged
                    repaint()
                }
            }
            case KeyPressed(_, Key.Add, _, _) => {
                if(simViewPanel.viewSize > 1) {
                    simViewPanel.viewSize -= 1;
                    viewDataChanged
                    repaint()
                }
            }
        }
        focusable = true
        requestFocus

        peer.addFocusListener(new FocusListener {

            override def focusGained(e: FocusEvent): Unit = {

            }

            override def focusLost(e: FocusEvent): Unit = {
                requestFocus()
            }
        })

    }

    size = new Dimension(1080, 960);
    resizable = false;
    peer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}