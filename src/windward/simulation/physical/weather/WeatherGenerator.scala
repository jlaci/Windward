package windward.simulation.physical.weather

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
    def initUniformWind(width: Int, height: Int, windDirection: Int, windSpeed: Int): WeatherMap = {

        val speed: Array[Array[Int]] = new Array[Array[Int]](width);
        val direction: Array[Array[Int]] = new Array[Array[Int]](width);

        for (rowIndex <- 0 until width) {
            speed(rowIndex) = new Array[Int](height);
            direction(rowIndex) = new Array[Int](height);

            for (colIndex <- 0 until height) {
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
    def initRandomWind(width: Int, height: Int, minSpeed: Int, maxSpeed: Int): WeatherMap = {
        val rnd: Random = new Random();
        val speed: Array[Array[Int]] = new Array[Array[Int]](width);
        val direction: Array[Array[Int]] = new Array[Array[Int]](width);

        for (rowIndex <- 0 until width) {
            speed(rowIndex) = new Array[Int](height);
            direction(rowIndex) = new Array[Int](height);

            for (colIndex <- 0 until height) {
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
    def initGradientWind(width: Int, height: Int, windDirection: Int, startingSpeed: Int): WeatherMap = {

        def distance(x: Int, y: Int) = {
            Math.sqrt(Math.pow(0 - x, 2) + Math.pow(0 - y, 2));
        }

        val furthestDistance = distance(width, height);
        val speed: Array[Array[Int]] = new Array[Array[Int]](width);
        val direction: Array[Array[Int]] = new Array[Array[Int]](width);

        for (rowIndex <- 0 until width) {
            speed(rowIndex) = new Array[Int](height);
            direction(rowIndex) = new Array[Int](height);

            for (colIndex <- 0 until height) {
                val strength = (Math.max(1, distance(rowIndex, colIndex)) / furthestDistance);

                direction(rowIndex)(colIndex) = windDirection;
                speed(rowIndex)(colIndex) = Math.max(startingSpeed - (strength * startingSpeed).toInt, 0);
            }
        }
        new WeatherMap(speed, direction);
    }
}
