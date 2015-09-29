package windward.simulation.physical.weather

import windward.simulation.units.SimUnit

import scala.util.Random

/**
 * Created by jlaci on 2015. 09. 18..
 */
object WeatherGenerator {

    /**
     * Creates a weather map with uniform wind.
     * @param width
     * @param height
     * @param windDirection
     * @param windSpeed
     * @return
     */
    def initUniformWind(width: SimUnit, height: SimUnit, windDirection: Int, windSpeed: Int): WeatherMap = {

        val speed: Array[Array[Int]] = new Array[Array[Int]](width.toCellUnit.toInt);
        val direction: Array[Array[Int]] = new Array[Array[Int]](width.toCellUnit.toInt);

        for (rowIndex <- 0 until width.toInt) {
            speed(rowIndex) = new Array[Int](height.toCellUnit.toInt);
            direction(rowIndex) = new Array[Int](height.toCellUnit.toInt);

            for (colIndex <- 0 until height.toCellUnit.toInt) {
                speed(rowIndex)(colIndex) = windSpeed;
                direction(rowIndex)(colIndex) = windDirection;
            }
        }

        new WeatherMap(speed, direction);
    }

    /**
     * Creates a weather map with wind in random direction and speed in every cell.
     * @param width
     * @param height
     * @param minSpeed
     * @param maxSpeed
     * @return
     */
    def initRandomWind(width: SimUnit, height: SimUnit, minSpeed: Int, maxSpeed: Int): WeatherMap = {
        val rnd: Random = new Random();
        val speed: Array[Array[Int]] = new Array[Array[Int]](width.toCellUnit.toInt);
        val direction: Array[Array[Int]] = new Array[Array[Int]](width.toCellUnit.toInt);

        for (rowIndex <- 0 until width.toCellUnit.toInt) {
            speed(rowIndex) = new Array[Int](height.toCellUnit.toInt);
            direction(rowIndex) = new Array[Int](height.toCellUnit.toInt);

            for (colIndex <- 0 until height.toCellUnit.toInt) {
                speed(rowIndex)(colIndex) = rnd.nextInt(maxSpeed - minSpeed) + minSpeed;;
                direction(rowIndex)(colIndex) = rnd.nextInt(360);;
            }
        }

        new WeatherMap(speed, direction);
    }

    /**
     * Creates a weather map with gradient wind.
     * @param width
     * @param height
     * @param windDirection
     * @param startingSpeed
     * @return
     */
    def initGradientWind(width: SimUnit, height: SimUnit, windDirection: Int, startingSpeed: Int): WeatherMap = {

        def distance(x: Int, y: Int) = {
            Math.sqrt(Math.pow(0 - x, 2) + Math.pow(0 - y, 2));
        }

        val furthestDistance = distance(width.toCellUnit.toInt, height.toCellUnit.toInt);
        val speed: Array[Array[Int]] = new Array[Array[Int]](width.toCellUnit.toInt);
        val direction: Array[Array[Int]] = new Array[Array[Int]](width.toCellUnit.toInt);

        for (rowIndex <- 0 until width.toCellUnit.toInt) {
            speed(rowIndex) = new Array[Int](height.toCellUnit.toInt);
            direction(rowIndex) = new Array[Int](height.toCellUnit.toInt);

            for (colIndex <- 0 until height.toCellUnit.toInt) {
                val strength = (Math.max(1, distance(rowIndex, colIndex)) / furthestDistance);

                direction(rowIndex)(colIndex) = windDirection;
                speed(rowIndex)(colIndex) = Math.max(startingSpeed - (strength * startingSpeed).toInt, 0);
            }
        }
        new WeatherMap(speed, direction);
    }
}
