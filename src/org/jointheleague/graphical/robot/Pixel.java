package org.jointheleague.graphical.robot;
import java.awt.Color;

public class Pixel extends Vector2D {
	private Color color;
	
	public Pixel()
	{
		this(0, 0);
	}
	
	public Pixel(int x, int y)
	{
		super(x, y);
		color = Color.GREEN;
	}
	
	public Pixel(int x, int y, Color c)
	{
		super(x, y);
		color = c;
	}
	
	public Color getColor()
	{
		return color;
	}

}
