package physical.weather

import physical.world.World

import scala.util.Random

/**
 * Created by jlaci on 2015. 09. 18..
 */
object WeatherGenerator {

  def initUniformWind(world : World, windDirection : Int, windSpeed : Int): Unit = {
    for(row <- world.cells) {
      for(cell <- row) {
        cell.windDirection = windDirection;
        cell.windSpeed = windSpeed;
      }
    }
  }

  def initRandomWind(world : World, minSpeed : Int, maxSpeed : Int): Unit = {
    val rnd : Random = new Random();

    for(row <- world.cells) {
      for(cell <- row) {
        cell.windDirection = rnd.nextInt(360);
        cell.windSpeed = rnd.nextInt(maxSpeed - minSpeed) + minSpeed;
      }
    }
  }
}
