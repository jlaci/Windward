package windward.view

import java.awt._
import java.awt.geom.GeneralPath

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
        val oneSimUnitOnScreen = oneCellUnitOnScreen / SimulationUnits.tileSizeInSimUnits;


        val width = sailboat.params.length.toFloat() * oneSimUnitOnScreen;
        val length =  (width / 2).toInt;

        val oldTransform = g.getTransform;
        val angle = Math.toRadians(sailboat.heading);

        val transform = java.awt.geom.AffineTransform.getTranslateInstance((sailboat.posX.toFloat * oneSimUnitOnScreen) - (x * oneCellUnitOnScreen), sailboat.posY.toFloat * oneSimUnitOnScreen - (y * oneCellUnitOnScreen));
        val rotate = java.awt.geom.AffineTransform.getRotateInstance(angle);
        val inverseTranslate = java.awt.geom.AffineTransform.getTranslateInstance(-sailboat.posX.toFloat * oneSimUnitOnScreen, -sailboat.posY.toFloat * oneSimUnitOnScreen);
        transform.concatenate(rotate);
        transform.concatenate(inverseTranslate)
        g.transform(transform);


        val polygon = new GeneralPath();
        polygon.moveTo(((sailboat.posX.toFloat() * oneSimUnitOnScreen) - length/2).toInt, (sailboat.posY.toFloat() * oneSimUnitOnScreen + width/2).toInt);
        polygon.lineTo(((sailboat.posX.toFloat() * oneSimUnitOnScreen) + length/2).toInt, (sailboat.posY.toFloat() * oneSimUnitOnScreen + width/2).toInt);
        polygon.lineTo((sailboat.posX.toFloat() * oneSimUnitOnScreen).toInt, (sailboat.posY.toFloat() * oneSimUnitOnScreen - width/2).toInt);
        polygon.closePath();

        g.setColor(Color.gray)
        g.fill(polygon);

        g.setColor(Color.black)
        g.draw(polygon)

        g.setTransform(oldTransform);
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

        val windDirectionRad = Math.toRadians(windDirection + 90);

        //Rotate and scale
        val x1 = (ox1 * Math.cos(windDirectionRad) - oy1 * Math.sin(windDirectionRad)) * WIND_INDICATOR_SIZE;
        val y1 = (ox1 * Math.sin(windDirectionRad) + oy1 * Math.cos(windDirectionRad)) * WIND_INDICATOR_SIZE;

        val x2 = (ox2 * Math.cos(windDirectionRad) - oy2 * Math.sin(windDirectionRad)) * WIND_INDICATOR_SIZE;
        val y2 = (ox2 * Math.sin(windDirectionRad) + oy2 * Math.cos(windDirectionRad)) * WIND_INDICATOR_SIZE;

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
            val sailboatX = sailboat.position.x.toCellUnit().toInt();
            val sailboatY = sailboat.position.y.toCellUnit().toInt();

            if(sailboatX >= x && sailboatX < (x + size) && sailboatY >= y && sailboatY < (y + size)) {
                result += sailboat;
            }
        }

        result.toArray
    }

    private def calculateColor(windSpeed: Float): Color = {
        Color.getHSBColor(((windSpeed + 10) / (SimulationUnits.maxWindSpeed + 10)), 0.5f, 1);
    }
}
