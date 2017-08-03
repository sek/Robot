package org.jointheleague.graphical.robot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Package private singleton class that defines the window in which the Robots
 * move around.
 * 
 * @author David Dunn &amp; Erik Colban &copy; 2016
 *
 */
@SuppressWarnings("serial")
class RobotWindow extends JPanel {

	private static final int WINDOW_HEIGHT = 600;
	private static final int WINDOW_WIDTH = 900;
	private static final Color DEFAULT_WINDOW_COLOR = new Color(0xdcdcdc);
	private static final int MARGIN = 10;
	private static final RobotWindow INSTANCE = new RobotWindow(DEFAULT_WINDOW_COLOR);

	private Color winColor;

	private ArrayList<Robot> robotList;
	private Timer ticker;

	private BufferedImage leagueLogo;
	private boolean usingCustomImage;
	
	private boolean guiHasBeenBuilt = false;

	private RobotWindow(Color c) {
		winColor = c;
		robotList = new ArrayList<Robot>();
		try {
			leagueLogo = ImageIO.read(this.getClass().getResourceAsStream("res/league_logo.png"));
		} catch (IOException e) {
			System.err.println("Cannot load background image.");
		}
		usingCustomImage = false;
	}

	private void buildGui() {
		JFrame frame = new JFrame();
		setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		frame.add(this);
		// frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.pack();
		frame.setVisible(true);
		setFocusable(true);
	}

	/**
	 * Returns the single instance of the RobotWindow
	 * 
	 * @return the single instance
	 */
	static RobotWindow getInstance() {
		return INSTANCE;
	}

	void addRobot(final Robot r) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				public void run() {
					if (!guiHasBeenBuilt) {
						buildGui();
						ticker = new Timer(Robot.TICK_LENGTH, r);
						ticker.start();
						guiHasBeenBuilt = true;
					} else {
						ticker.addActionListener(r);
					}
					robotList.add(r);
				}

			});
		} catch (InvocationTargetException | InterruptedException e) {

		}
	}

	/**
	 * Set the RobotWindow's background Color
	 * @param c
	 */
	void setWinColor(Color c) {
		winColor = c;
		repaint();
	}

	/**
	 * Set the RobotWindow's background Image
	 * @param imageLocation
	 */
	void setBackgroundImage(String imageLocation) {
		try {
			leagueLogo = ImageIO.read(this.getClass().getResourceAsStream("../../../../" + imageLocation));
		} catch (IOException e) {
			System.err.println("Cannot load background image.");
		}
		usingCustomImage = true;
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(winColor);
		g2.fillRect(0, 0, getWidth(), getHeight());
		if(usingCustomImage){
			g2.drawImage(leagueLogo, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, null);
		}else{
			int imgX = getWidth() - leagueLogo.getWidth() - MARGIN;
			int imgY = MARGIN;
			g2.drawImage(leagueLogo, imgX, imgY, null);
		}
		
		

		for (Robot r : robotList) {
			r.draw(g2);
		}
	}

}
