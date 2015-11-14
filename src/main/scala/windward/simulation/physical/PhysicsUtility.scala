package windward.simulation.physical

/**
 * Created by jlaci on 2015. 10. 21..
 */
object PhysicsUtility {

    def calculateKineticEnergy(v : Double, m : Double) : Double = {
        0.5 * m * v * v
    }

    def calculateSpeedFromKineticEnergy(energy : Double, m : Double) : Double = {
        Math.sqrt(((energy / 0.5) / m))
    }

    def knotsFromMeterPerSecond(ms : Double) : Double = {
        ms * 1.94384449246f
    }
}
