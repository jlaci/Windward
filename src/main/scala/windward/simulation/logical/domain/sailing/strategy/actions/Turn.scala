package windward.simulation.logical.domain.sailing.strategy.actions

import windward.simulation.logical.domain.sailing.Sailboat
import windward.simulation.logical.domain.sailing.strategy.SailingAction

/**
 * @author Janoky Laszlo Viktor <janoky.laszlo@bmeautsoft.hu>
 */
class Turn(val degrees : Int) extends SailingAction(1) {

    override def applyOn(sailboat: Sailboat): Sailboat = {
        sailboat
    }

}
