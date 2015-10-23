package windward.simulation.logical.domain.sailing.polar

/**
 * Created by jlaci on 2015. 10. 13..
 */
class PolarCurve (val polarData : Array[Array[Int]]) {

    def getMaxSpeed(trueWindSpeed : Int, trueWindDirection : Int): Int = {
        polarData(trueWindSpeed)(trueWindDirection);
    }
}
