package windward.simulation.logical.domain.sailing.strategy

import windward.simulation.logical.Action
import windward.simulation.logical.domain.sailing.Sailboat

/**
 * @author Janoky Laszlo Viktor <janoky.laszlo@bmeautsoft.hu>
 */
abstract class SailingAction(val timeCost : Int) extends Action[Sailboat] {

}