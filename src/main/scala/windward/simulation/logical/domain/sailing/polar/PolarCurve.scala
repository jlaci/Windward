package windward.simulation.logical.domain.sailing.polar

/**
 * Created by jlaci on 2015. 10. 13..
 */
class PolarCurve (val polarData : Array[Array[Float]]) {

    def getMaxSpeed(trueWindSpeed : Int, relativeWindDirection : Int): Float = {
        polarData(trueWindSpeed)(relativeWindDirection + 180)
    }
}
