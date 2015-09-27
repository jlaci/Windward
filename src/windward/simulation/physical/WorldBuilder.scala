package windward.simulation.physical

import windward.simulation.physical.weather.WeatherGenerator
import windward.simulation.physical.world.World

/**
 * Created by jlaci on 2015. 09. 18..
 */
object WorldBuilder {

    def createEmptyWorld(width: Int, height: Int): World = {
        new World(width, height);
    }

    def createWorldWithRandomWind(width: Int, height: Int): World = {
        new World(width, height, WeatherGenerator.initRandomWind(32, 32, 0, 300));
    }

    def createWorldWitGradientWind(width: Int, height: Int, windDirection: Int, startingSpeed: Int): World = {
        new World(width, height, WeatherGenerator.initGradientWind(width, height, windDirection, startingSpeed));
    }

}
