package physical

import physical.weather.WeatherGenerator
import physical.world.World

/**
 * Created by jlaci on 2015. 09. 25..
 */
object Simulator {

  var time : Long = 0l;

  val SIM_TIME_STEP = 1;

  val SIM_END_TIME = 100;


  var world : World = null;

  def init(): Unit = {
    world = WorldBuilder.createEmptyWorld(32, 32);
    //WeatherGenerator.initUniformWind(world, 0, 6);
    //WeatherGenerator.initRandomWind(world, 6, 30);
    WeatherGenerator.initGradientWind(world, 45, 300);
  }

  def start() : Unit = {

  }

  def step() : World = {
    if(time < SIM_END_TIME) {
      time += SIM_TIME_STEP
    }
    world;
  }

}
