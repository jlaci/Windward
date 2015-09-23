import physical.WorldBuilder
import physical.weather.WeatherGenerator
import view.ViewWindow

/**
 * Created by jlaci on 2015. 09. 18..
 */
object Application {

  def main(args : Array[String]) : Unit = {
    println("Windward starting.")

    val world = WorldBuilder.createEmptyWorld(32, 32);
    //WeatherGenerator.initUniformWind(world, 0, 6);
    //WeatherGenerator.initRandomWind(world, 6, 30);
    WeatherGenerator.initGradientWind(world, 45, 300);

    val window : ViewWindow = new ViewWindow(world);
    window.visible = true;

  }
}
