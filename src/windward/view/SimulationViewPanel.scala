package windward.view

import java.awt._

import windward.simulation.Simulator
import windward.simulation.logical.domain.sailing.Sailboat
import windward.simulation.units.SimulationUnits

import scala.collection.mutable
import scala.swing.{Graphics2D, Panel}

/**
 * Created by jlaci on 2015. 09. 18..
 */
class SimulationViewPanel(var x: Int, var y: Int, var viewSize: Int) extends Panel {

    var viewSimStep: Int = 0;

    override def paintComponent(g: Graphics2D) {

        val windData = getWindData(x, y, viewSize);
        val sailboats = getSailboats(x, y, viewSize);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY);

        val minDimension = Math.min(g.getClipBounds.width.toFloat, g.getClipBounds.height.toFloat);

        val dx = minDimension / windData.length
        val dy = minDimension / windData.map(_.length).max

        for {
            x <- 0 until windData.length
            y <- 0 until windData(x).length
            x1 = (x * dx).toInt
            y1 = (y * dy).toInt
            x2 = ((x + 1) * dx).toInt
            y2 = ((y + 1) * dy).toInt
        } {
            drawWindPower(x1, y1, x2, y2, g, windData(x)(y) _1);
            drawWindDirection(x1, y1, x2, y2, g, windData(x)(y) _2);
        }

        for (sailboat <- sailboats) {
            drawSailboat(sailboat, g);
        }

    }

    def drawSailboat(sailboat: Sailboat, g : Graphics2D): Unit = {
        val minDimension = Math.min(g.getClipBounds.width.toFloat, g.getClipBounds.height.toFloat);
        val oneCellUnitOnScreen = minDimension / viewSize;

        g.setColor(Color.cyan)
        val width = sailboat.length.toCellUnit().toInt() * oneCellUnitOnScreen;
        val height =  (sailboat.length.toCellUnit().toInt()/2) * oneCellUnitOnScreen;

        g.fillRect(sailboat.posX.toCellUnit().toInt(), sailboat.posY.toCellUnit().toInt(),  width.toInt, height.toInt);
    }

    def drawWindPower(x1: Int, y1: Int, x2: Int, y2: Int, g: Graphics2D, color: Color): Unit = {
        g.setColor(color);
        g.fillRect(x1, y1, x2 - x1, y2 - y1);
    }

    def drawWindDirection(x1: Int, y1: Int, x2: Int, y2: Int, g: Graphics2D, windDirection: Int): Unit = {
        g.setColor(Color.BLACK)
        val lineCoords = calculateWindDirection(windDirection, (x1, y1, x2, y2));
        drawArrowPolygon(lineCoords._1, lineCoords._2, lineCoords._3, lineCoords._4, g);
    }

    def drawArrowPolygon(x1: Int, y1: Int, x2: Int, y2: Int, g: Graphics2D): Unit = {

        val oldTransform = g.getTransform;

        val dx = x2 - x1;
        val dy = y2 - y1;
        val angle = Math.atan2(dy, dx);
        val len = Math.sqrt(dx * dx + dy * dy);
        val at = java.awt.geom.AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(java.awt.geom.AffineTransform.getRotateInstance(angle));
        g.transform(at);

        // Draw horizontal arrow starting in (0, 0)
        g.drawLine(0, 0, len.toInt, 0);
        val polygon = new Polygon();
        polygon.addPoint(len.toInt, 0);
        polygon.addPoint((len - WIND_INDICATOR_ARROW_SIZE).toInt, -WIND_INDICATOR_ARROW_SIZE.toInt);
        polygon.addPoint((len - WIND_INDICATOR_ARROW_SIZE).toInt, WIND_INDICATOR_ARROW_SIZE.toInt);
        polygon.addPoint(len.toInt, 0);
        g.fillPolygon(polygon);
        g.setTransform(oldTransform);
    }

    def calculateWindDirection(windDirection: Int, tileCoords: (Int, Int, Int, Int)): (Int, Int, Int, Int) = {
        //Create a line in the center of the tile
        val width = tileCoords._3 - tileCoords._1;
        val height = tileCoords._4 - tileCoords._2;

        val cx1 = tileCoords._1;
        val cy1 = tileCoords._2 + height / 2;
        val cx2 = tileCoords._3;
        val cy2 = cy1;

        //Transform origin to the center of the tile
        val xOffset = cx1 + (width / 2);
        val yOffset = cy1;

        val ox1 = cx1 - xOffset;
        val oy1 = cy1 - yOffset;
        val ox2 = cx2 - xOffset;
        val oy2 = cy2 - yOffset;

        //Rotate and scale
        val x1 = (ox1 * Math.cos(windDirection) - oy1 * Math.sin(windDirection)) * WIND_INDICATOR_SIZE;
        val y1 = (ox1 * Math.sin(windDirection) + oy1 * Math.cos(windDirection)) * WIND_INDICATOR_SIZE;

        val x2 = (ox2 * Math.cos(windDirection) - oy2 * Math.sin(windDirection)) * WIND_INDICATOR_SIZE;
        val y2 = (ox2 * Math.sin(windDirection) + oy2 * Math.cos(windDirection)) * WIND_INDICATOR_SIZE;

        //Transform back and return
        ((x1 + xOffset).toInt, (y1 + yOffset).toInt, (x2 + xOffset).toInt, (y2 + yOffset).toInt);
    }

    private def getWindData(x: Int, y: Int, size: Int): Array[Array[(Color, Int)]] = {
        val result: Array[Array[(Color, Int)]] = new Array[Array[(Color, Int)]](size);

        var viewX = x;
        if ((Simulator.worldState(viewSimStep).width.toCellUnit.toInt) < (x + size)) {
            viewX = Math.max(Simulator.worldState(viewSimStep).width.toCellUnit.toInt - size, 0);
        }

        var viewY = y;
        if ((Simulator.worldState(viewSimStep).height.toCellUnit.toInt) < (y + size)) {
            viewY = Math.max(Simulator.worldState(viewSimStep).height.toCellUnit.toInt - size, 0);
        }

        for (rowIndex <- viewX until (viewX + size)) {
            result(rowIndex - viewX) = new Array[(Color, Int)](size);
            for (columnIndex <- viewY until (viewY + size)) {
                val cell = Simulator.worldState(viewSimStep).cells(rowIndex)(columnIndex);
                result(rowIndex - viewX)(columnIndex - viewY) = (calculateColor(cell.windSpeed), cell.windDirection);
            }
        }

        result;
    }

    private def getSailboats(x: Int, y: Int, size: Int) : Array[Sailboat] = {
        val result = mutable.MutableList[Sailboat]();

        for(sailboat <- Simulator.sailboats(viewSimStep)) {
            val sailboatX = sailboat.posX.toCellUnit().toInt();
            val sailboatY = sailboat.posY.toCellUnit().toInt();

            if(sailboatX >= x && sailboatX < (x + size) && sailboatY >= y && sailboatY < (y + size)) {
                result += sailboat;
            }
        }

        result.toArray
    }

    private def calculateColor(windSpeed: Int): Color = {
        Color.getHSBColor(windSpeed / SimulationUnits.maxWindSpeed, 0.5f, 1);
    }
}
