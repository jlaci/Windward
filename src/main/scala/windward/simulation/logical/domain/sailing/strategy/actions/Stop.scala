package windward.simulation.logical.domain.sailing.strategy.actions

import windward.simulation.logical.domain.sailing.Sailboat
import windward.simulation.logical.domain.sailing.strategy.SailingAction

/**
 * @author Janoky Laszlo Viktor <janoky.laszlo@bmeautsoft.hu>
 */
class Stop() extends SailingAction(1) {

    override def applyOn(sailboat: Sailboat): Sailboat = {
        new Sailboat(sailboat.posX, sailboat.posY, sailboat.params, sailboat.heading, 0, -1, sailboat.strategy, sailboat.possibleActions, sailboat.ongoingActions);
    }

}
