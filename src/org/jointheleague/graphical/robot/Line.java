package org.jointheleague.graphical.robot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Package private immutable class that represents a line drawn by the Robot. 
 * 
 * @author Erik Colban &amp; David Dunn &copy; 2016
 *
 */
final public class Line {

	private final int startX;
	private final int startY;
	private final int endX;
	private final int endY;
	private final int lineSize;
	private final Color color;

	public Line(int startX, int startY, int endX, int endY, int lineSize, Color color) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.lineSize = lineSize + 1;
		this.color = color;
	}

	public void draw(Graphics2D g2) {
		g2.setStroke(new BasicStroke(lineSize));
		g2.setColor(color);
		g2.drawLine(startX, startY, endX, endY);
	}
}
