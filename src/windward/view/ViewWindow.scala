package windward.view

import javax.swing.{BorderFactory, JFrame}

import windward.simulation.Simulator

import scala.swing.GridBagPanel.Fill
import scala.swing._
import scala.swing.event.ButtonClicked


/**
 * Created by jlaci on 2015. 09. 18..
 */
class ViewWindow extends MainFrame {

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
                val simViewPanel = new SimulationViewPanel(0, 0, 32) {
                    preferredSize = new swing.Dimension(750, 750)
                }
                cs.fill = Fill.Both;
                layout(simViewPanel) = cs;

                //The simulation timeline control panel
                val simTimelinePanel = new FlowPanel() {
                    border = BorderFactory.createTitledBorder("Simulation timeline");

                    val timeStepTF = new TextField() {
                        columns = 10
                        text = simViewPanel.viewSimStep + " / " + Simulator.SIM_END_TIME / Simulator.SIM_TIME_STEP
                    }

                    def simStepChanged = {
                        simViewPanel.repaint()
                        timeStepTF.text = simViewPanel.viewSimStep + " / " + Simulator.SIM_END_TIME / Simulator.SIM_TIME_STEP
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
                                if (simViewPanel.viewSimStep - Simulator.SIM_TIME_STEP >= 0) {
                                    simViewPanel.viewSimStep -= Simulator.SIM_TIME_STEP;
                                    simStepChanged
                                }
                            };
                        }
                    } += timeStepTF += new Button() {
                        text = ">"
                        reactions += {
                            case ButtonClicked(_) => {
                                if (simViewPanel.viewSimStep + Simulator.SIM_TIME_STEP <= Simulator.SIM_END_TIME) {
                                    simViewPanel.viewSimStep += Simulator.SIM_TIME_STEP;
                                    simStepChanged
                                }
                            };
                        };
                    } += new Button {
                        text = "Jump to end"
                        reactions += {
                            case ButtonClicked(_) => {
                                simViewPanel.viewSimStep = Simulator.SIM_END_TIME / Simulator.SIM_TIME_STEP;
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
    }

    size = new Dimension(1280, 960);
    resizable = false;
    peer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

}