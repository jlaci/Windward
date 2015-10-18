package windward

import windward.simulation.units.SimUnit
import windward.simulation.{SimulationParameters, Simulator}
import windward.view.ViewWindow


/**
 * Created by jlaci on 2015. 09. 18..
 */
object Application {

    def main(args: Array[String]): Unit = {
        println("Windward starting.")

        val params = new SimulationParameters(100, new SimUnit(128), new SimUnit(128));
        Simulator.init(params);

        val window: ViewWindow = new ViewWindow();
        window.visible = true;

        Simulator.start();

    }
}
