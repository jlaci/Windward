package windward.simulation.logical

/**
 * @author Janoky Laszlo Viktor <janoky.laszlo@bmeautsoft.hu>
 */
abstract class Action[actorType <: Actor] {
    def applyOn(actor : actorType) : actorType
}
