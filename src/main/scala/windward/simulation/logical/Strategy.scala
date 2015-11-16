package windward.simulation.logical

import windward.simulation.physical.world.World

/**
 * @author Janoky Laszlo Viktor <janoky.laszlo@bmeautsoft.hu>
 */
abstract class Strategy[actorType <: Actor, actionType <: Action[actorType]] {

    def step(actor : actorType, world: World) : List[actionType]

    def copyStrategy() : Strategy[actorType, actionType]
}
