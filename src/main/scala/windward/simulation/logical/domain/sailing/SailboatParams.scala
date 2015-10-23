package windward.simulation.logical.domain.sailing

import windward.simulation.logical.domain.sailing.sail.Sail
import windward.simulation.units.SimUnit

/**
 * @author Janoky Laszlo Viktor <janoky.laszlo@bmeautsoft.hu>
 */
class SailboatParams(val length : SimUnit, val height: SimUnit, val turnSpeed : Int, val mass : Int, val sails : Array[Sail]) {

    def acceleration(force : Float) : Float = {
        force / mass
    }

    def maxAcceleration() : Float = {
        //TODO: empirical value for displacement mode, around 3000 kg
        5400 / mass;
    }
}
