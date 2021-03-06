package org.jointheleague.graphical.robot.curves;

import org.jointheleague.graphical.robot.Robot;

import java.awt.*;
import java.awt.geom.Path2D;

final public class Close implements Segment {

    private final float startX;
    private final float startY;
    private final float[] ctrlPoints;

    /**
     * Constructor
     *
     * @param x          x-coordinate of the segment's starting point
     * @param y          y-coordinate of the segment's starting point
     * @param ctrlPoints an array of length at least 2 that
     *                   contains the x- and y-coordinates of the segment's end point.
     * @param lineSize   the line width used to draw the segment
     * @param color      the color used to draw the segment
     */
    public Close(float x, float y, float[] ctrlPoints, int lineSize, Color color) {
        this.startX = x;
        this.startY = y;
        this.ctrlPoints = ctrlPoints;
    }

    @Override
    public Segment subSegment(float t) {
        if (1F <= t) return this;
        Robot.Pos pos = getPos(t);
        return new Line(startX, startY, new float[]{pos.getX(), pos.getY()}, 0, null);
    }

    @Override
    public Robot.Pos getPos(float t) {
        if (t <= 0.0F) return new Robot.Pos(startX, startY);
        if (1.0F <= t) return new Robot.Pos(ctrlPoints[0], ctrlPoints[1]);
        float u = 1F - t;
        return new Robot.Pos(
                u * startX + t * ctrlPoints[0],
                u * startY + t * ctrlPoints[1]);
    }

    @Override
    public double getStartAngle() {
        if (ctrlPoints[0] == startX && ctrlPoints[1] == startY) return Double.NaN;
        return Math.atan2(ctrlPoints[0] - startX, -ctrlPoints[1] + startY);
    }

    @Override
    public double getEndAngle() {
        return getStartAngle();
    }

    @Override
    public double getAngle(float t) {
        return getStartAngle();
    }

    @Override
    public float getSize() {
        double a = ctrlPoints[0] - startX;
        double b = ctrlPoints[1] - startY;
        return (float) Math.sqrt(a * a + b * b);
    }

    @Override
    public Path2D addTo(Path2D path2D) {
        path2D.closePath();
        return path2D;
    }

    @Override
    public Path2D addTo(Path2D path2D, float t) {
        if (1F < t) {
            path2D.closePath();
        } else {
            final Robot.Pos pos1 = getPos(t);
            path2D.lineTo(pos1.getX(), pos1.getY());
        }
        return path2D;
    }
}
