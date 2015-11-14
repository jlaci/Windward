package windward.simulation.logical.domain.sailing.strategy.actions

import windward.simulation.logical.domain.sailing.Sailboat
import windward.simulation.logical.domain.sailing.strategy.SailingAction
import windward.simulation.units.SimulationUnits

/**
 * @author Janoky Laszlo Viktor <janoky.laszlo@bmeautsoft.hu>
 */
class Turn(val degrees : Double) extends SailingAction(1) {

    override def applyOn(sailboat: Sailboat): Sailboat = {

        new Sailboat(sailboat.posX, sailboat.posY, sailboat.params, sailboat.heading + degrees, sailboat.speed, sailboat.activeSail, sailboat.strategy, sailboat.possibleActions, sailboat.ongoingActions);

    }

}
