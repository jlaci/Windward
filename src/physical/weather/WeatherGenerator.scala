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

  def initGradientWind(world : World, windDirection : Int, startingSpeed : Int): Unit = {

    def distance(x : Int, y : Int) = {
      Math.sqrt(Math.pow(0 - x,2) + Math.pow(0 - y,2));
    }

    val furthestDistance = distance(world.cells.length, world.cells(0).length);

    for((row, i) <- world.cells.zipWithIndex) {
      for((cell, j) <- row.zipWithIndex) {
        val strength = (Math.max(1, distance(i, j)) / furthestDistance);

        cell.windDirection = windDirection;
        cell.windSpeed = Math.max(startingSpeed - (strength * startingSpeed).toInt, 0);
        println(i + " - " + j + " : " + cell.windSpeed)
      }
    }

    println(furthestDistance);
  }
}
