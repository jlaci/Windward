import physical.Simulator
import view.ViewWindow

/**
 * Created by jlaci on 2015. 09. 18..
 */
object Application {

  def main(args : Array[String]) : Unit = {
    println("Windward starting.")
    Simulator.init();

    val window : ViewWindow = new ViewWindow();
    window.visible = true;

    Simulator.start();

  }
}
