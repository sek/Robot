package org.jointheleague.graphical.robot;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
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
	
	private ArrayList<Robot> robotList;
	
	//image fields
	private static final int IMG_WIDTH = 723;
	private static final int IMG_HEIGHT = 362;
	private static int imgX;
	private static int imgY;
	private static BufferedImage leagueLogo;
	
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
		
		robotList = new ArrayList<Robot>();
		
		winColor = c;
		
		imgX = (width / 2) - (IMG_WIDTH / 2);
		imgY = (height / 2) - (IMG_HEIGHT / 2);
		
		try {
			leagueLogo = ImageIO.read(this.getClass().getResourceAsStream("league_logo.png"));
		} catch (IOException e) 
		{
			System.err.println("Cannot load background image.");
		}
	}
	
	public void setWinColor(Color c)
	{
		winColor = c;
		repaint();
	}

	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setColor(winColor);
		g2.fillRect(0, 0, width, height);
		g2.drawImage(leagueLogo, imgX, imgY, IMG_WIDTH, IMG_HEIGHT, null);
		
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
