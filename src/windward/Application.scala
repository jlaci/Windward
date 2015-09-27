package windward

import windward.simulation.{SimulationParameters, Simulator}
import windward.view.ViewWindow


/**
 * Created by jlaci on 2015. 09. 18..
 */
object Application {

    def main(args: Array[String]): Unit = {
        println("Windward starting.")

        val params = new SimulationParameters(32, 32, 1);
        Simulator.init(params);

        val window: ViewWindow = new ViewWindow();
        window.visible = true;

        Simulator.start();

    }
}
