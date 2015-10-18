package windward.simulation.logical

import windward.simulation.logical.domain.sailing.Sailboat
import windward.simulation.physical.world.World

/**
 * @author Janoky Laszlo Viktor <janoky.laszlo@bmeautsoft.hu>
 */
object LogicalSimulator {

    def step(world : World, sailboats: Array[Sailboat]): Array[Sailboat] = {
        val result = new Array[Sailboat](sailboats.length)

        for((sailboat, i) <- sailboats.zipWithIndex) {
            result(i) = sailboat.step(world)
        }

        result
    }
}
