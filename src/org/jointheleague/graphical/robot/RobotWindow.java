package org.jointheleague.graphical.robot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class RobotWindow extends JPanel
{
	/**
	 * 
	 */
	private static final Color			DEFAULT_WINDOW_COLOR	= new Color(
																		0xdcdcdc);
	private static final int			MARGIN					= 10;
	private static final RobotWindow	INSTANCE				= new RobotWindow(
																		DEFAULT_WINDOW_COLOR);

	private Color						winColor;

	private ArrayList<Robot>			robotList;
	private Timer						ticker;

	private BufferedImage				leagueLogo;

	private boolean						guiHasBeenBuilt				= false;

	private RobotWindow(Color c)
	{
		winColor = c;
		robotList = new ArrayList<Robot>();
		try
		{
			leagueLogo = ImageIO.read(this.getClass().getResourceAsStream(
					"league_logo.png"));
		} catch (IOException e)
		{
			System.err.println("Cannot load background image.");
		}
	}

	private void buildGui()
	{
		JFrame frame = new JFrame();
		frame.add(this);
		frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setVisible(true);
	}

	public static RobotWindow getInstance()
	{
		return INSTANCE;
	}

	void addRobot(final Robot r, final int... pos)
	{
		SwingUtilities.invokeLater(new Runnable()
		{

			public void run()
			{
				if (!guiHasBeenBuilt)
				{
					buildGui();
					ticker = new Timer(1000 / 30, r);
					ticker.start();
					guiHasBeenBuilt = true;
				} else
				{
					ticker.addActionListener(r);
				}
				robotList.add(r);
				if (pos.length == 0)
				{
					r.moveTo(getWidth() / 2, getHeight() / 2);
				} else
				{
					r.moveTo(pos[0], pos[1]);
				}

			}

		});
	}

	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(winColor);
		g2.fillRect(0, 0, getWidth(), getHeight());
		int imgX = getWidth() - leagueLogo.getWidth() - MARGIN;
		int imgY = MARGIN;
		g2.drawImage(leagueLogo, imgX, imgY, null);

		for (Robot r : robotList)
		{
			r.draw(g2);
		}
	}

	public void setWinColor(Color c)
	{
		winColor = c;
		repaint();
	}

}
