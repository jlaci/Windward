package windward.view

import javax.swing.{BorderFactory, JFrame}

import windward.simulation.Simulator
import windward.simulation.units.SimUnit

import scala.swing.GridBagPanel.Fill
import scala.swing._
import scala.swing.event.{ButtonClicked, Key, KeyPressed}


/**
 * Created by jlaci on 2015. 09. 18..
 */
class ViewWindow extends MainFrame {

    var simViewPanel : SimulationViewPanel = null;
    var xCoordTF : TextField = null;
    var yCoordTF : TextField = null;
    var sizeTF : TextField = null;


    def viewDataChanged = {
        xCoordTF.text = "X : " + simViewPanel.x
        yCoordTF.text = "Y : " + simViewPanel.y
        sizeTF.text = "S: " + simViewPanel.viewSize
    }

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
                simViewPanel = new SimulationViewPanel(0, 0, 16) {
                    preferredSize = new swing.Dimension(750, 750)
                }
                cs.fill = Fill.Both;
                layout(simViewPanel) = cs;

                //Data about the sim view panel
                val simViewDataPanel = new FlowPanel() {
                    xCoordTF = new TextField() {
                        columns = 5
                        text = "X : " + simViewPanel.x
                    }

                    yCoordTF = new TextField() {
                        columns = 5
                        text = "Y : " + simViewPanel.y
                    }

                    sizeTF = new TextField() {
                        columns = 5
                        text = "S: " + simViewPanel.viewSize
                    }

                    contents += xCoordTF += yCoordTF += sizeTF

                }
                layout(simViewDataPanel) = cs;

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

        //Event listeners
        listenTo(keys)
        reactions += {
            case KeyPressed(_, Key.Up, _, _) => {
                println("UP");
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
                if(simViewPanel.y < new SimUnit(Simulator.simulationParameters.worldHeight).toCellUnit().toInt() - simViewPanel.viewSize) {
                    simViewPanel.y += 1;
                    viewDataChanged
                    repaint()
                }
            }
            case KeyPressed(_, Key.Right, _, _) => {
                if(simViewPanel.x < new SimUnit(Simulator.simulationParameters.worldWidth).toCellUnit().toInt() - simViewPanel.viewSize) {
                    simViewPanel.x += 1;
                    viewDataChanged
                    repaint()
                }
            }
            case KeyPressed(_, Key.Subtract, _, _) => {
                if(simViewPanel.viewSize < Math.min(32, Math.max(new SimUnit(Simulator.simulationParameters.worldWidth).toCellUnit().toInt(), new SimUnit(Simulator.simulationParameters.worldHeight).toCellUnit().toInt()))) {
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

    }

    size = new Dimension(1280, 960);
    resizable = false;
    peer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



}