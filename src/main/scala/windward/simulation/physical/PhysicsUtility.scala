package windward.simulation.physical

/**
 * Created by jlaci on 2015. 10. 21..
 */
object PhysicsUtility {

    def calculateKineticEnergy(v : Float, m : Float) : Double = {
        0.5 * m * v * v
    }

    def calculateSpeedFromKineticEnergy(energy : Double, m : Float) : Float = {
        Math.sqrt(((energy / 0.5) / m)).toFloat
    }

    def knotsFromMeterPreSecond(ms : Float) : Float = {
        ms * 1.94384449246f
    }
}
