package windward.simulation.physical

import windward.simulation.physical.weather.WeatherGenerator
import windward.simulation.physical.world.World
import windward.simulation.units.SimUnit

/**
 * Created by jlaci on 2015. 09. 18..
 */
object WorldBuilder {

    def createEmptyWorld(width: SimUnit, height: SimUnit): World = {
        new World(width, height);
    }

    def createWorldWithRandomWind(width: SimUnit, height: SimUnit): World = {
        new World(width, height, WeatherGenerator.initRandomWind(width, height, 0, 300));
    }

    def createWorldWitGradientWind(width: SimUnit, height: SimUnit, windDirection: Int, startingSpeed: Int): World = {
        new World(width, height, WeatherGenerator.initGradientWind(width, height, windDirection, startingSpeed));
    }

}
