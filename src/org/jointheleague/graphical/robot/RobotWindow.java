package org.jointheleague.graphical.robot;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class RobotWindow extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int width;
	private int height;
	private JFrame window;
	private Color winColor;
	
	ArrayList<Robot> robotList;
	
	public RobotWindow(int w, int h, Color c)
	{
		width = w;
		height = h;
		window = new JFrame();
		window.add(this);
		window.setSize(w, h);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		
		winColor = c;
		
		robotList = new ArrayList<Robot>();
	}
	
	public void setWinColor(Color c)
	{
		winColor = c;
	}
	
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setColor(winColor);
		g2.fillRect(0, 0, width, height);
		
		for(int i = 0; i < robotList.size(); i++)
		{
			Robot r = robotList.get(i);
			r.draw(g2);
		}
	}
	
	public void addRobot(Robot r)
	{
		robotList.add(r);
		update(r);
	}
	
	public void update(Robot r)
	{
		r.update();
		repaint();
	}
}
