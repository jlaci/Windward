package windward.simulation.physical

import windward.simulation.physical.weather.WeatherGenerator
import windward.simulation.physical.world.World
import windward.simulation.units.{SimUnit, SimulationUnits}

/**
 * Created by jlaci on 2015. 09. 18..
 */
object WorldBuilder {

    def createEmptyWorld(width: SimUnit, height: SimUnit): World = {
        new World(width, height);
    }

    def createWorldWithUniformWind(width: SimUnit, height: SimUnit, windDirection: Int, windSpeed: Int) : World = {
        new World(width, height, WeatherGenerator.initUniformWind(width, height, windDirection, windSpeed))
    }

    def createWorldWithRandomWind(width: SimUnit, height: SimUnit): World = {
        new World(width, height, WeatherGenerator.initRandomWind(width, height, 0, SimulationUnits.maxWindSpeed.toInt));
    }

    def createWorldWitGradientWind(width: SimUnit, height: SimUnit, windDirection: Int, startingSpeed: Int): World = {
        new World(width, height, WeatherGenerator.initGradientWind(width, height, windDirection, startingSpeed));
    }

}
