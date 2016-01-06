package org.jointheleague.graphical.robot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.SwingUtilities;

/**
 * <p>
 * This class is used to show a robot inside a window. If no RobotWindow exists
 * when instantiating a Robot, a window is create and the Robot placed inside
 * the window. If a RobtWindow already exists, the new Robot is placed inside
 * the existing RobotWindow.
 * </p>
 * <p>
 * A Robot is controlled by calling its {@link #move(int)}, {@link #turn(int)},
 * {@link #microMove(int)}, and {@link #microTurn(int)} methods. These methods
 * should be called from the same thread, which is typically the main thread,
 * but different Robots may be controlled from different threads, thereby
 * allowing the Robots to move simultaneously.
 * </p>
 * <p>
 * A Robot also has state, e.g., visible or hidden, pen size, pen up or down,
 * etc. This state may be modified by calling the respective setter methods.
 * </p>
 * 
 * @author David Dunn &amp; Erik Colban &copy; 2016
 *
 */
public class Robot implements ActionListener {

	private static final int MAXI_IMAGE_SIZE = 100;
	private static final int MINI_IMAGE_SIZE = 25;
	protected static final int TICK_LENGTH = 40; // in milliseconds

	private int speed;
	private boolean penDown;
	private int penSize;
	private Color penColor;

	private static class Pos {
		private final float x;
		private final float y;

		Pos(float x, float y) {
			this.x = x;
			this.y = y;
		}
	}

	private volatile Pos pos;

	private volatile int angle;
	private volatile boolean isVisible;
	private volatile boolean isSparkling;
	private volatile Line currentLine;

	private volatile boolean isMini = false;
	private Image maxiImage;
	private Image miniImage;
	private volatile Image image;

	private ArrayList<Line> lines; // only referenced from the EDT

	private RobotWindow window;

	private enum TimeQuantum {
		TICK
	}

	private BlockingQueue<TimeQuantum> leakyBucket = new ArrayBlockingQueue<>(1);

	public Robot() {
		this("rob");
	}

	/**
	 * Instantiates a new default Robot at the position provided.
	 * 
	 * @param xPos
	 *            the x-coordinate of the Robot's center
	 * @param yPos
	 *            the y-coordinate of the Robot's center
	 */
	public Robot(int xPos, int yPos) {
		this("rob", xPos, yPos);
	}

	/**
	 * Instantiates a new Robot whose image is specified by the filename at the
	 * center of the RobotWindow.
	 * 
	 * @param fileName
	 *            The file name without extension of a file in robi format that
	 *            specifies the Robot's image.
	 */
	public Robot(String fileName) {
		this(RobotImage.loadRobi(fileName));
	}

	/**
	 * Instantiates a new Robot at the center of the RobotWindow.
	 * 
	 * @param robotImage
	 *            a BufferedImage containing the robot image. It does not need
	 *            to be to scale since it will be scaled to the appropriate
	 *            size.
	 */
	public Robot(BufferedImage robotImage) {
		this(robotImage, 0, 0);
		final int[] center = new int[2];
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					center[0] = window.getWidth() / 2;
					center[1] = window.getHeight() / 2;
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
		}
		moveTo(center[0], center[1]);
	}

	/**
	 * 
	 * @param fileName
	 *            the name of the file containing the Robot image, without the
	 *            ".robi" extension.
	 * @param xPos
	 *            the initial x-coordinate of the robot
	 * @param yPos
	 *            the initial y-coordinate of the robot
	 */
	public Robot(String fileName, int xPos, int yPos) {
		this(RobotImage.loadRobi(fileName), xPos, yPos);
	}

	/**
	 * 
	 * @param inputImage
	 *            a BufferedImage containing the robot image. It does not need
	 *            to be to scale since it will be scaled to the appropriate
	 *            size.
	 * @param xPos
	 *            the initial x-coordinate of the robot
	 * @param yPos
	 *            the initial y-coordinate of the robot
	 */
	public Robot(BufferedImage inputImage, int xPos, int yPos) {
		angle = 0;
		speed = 1;
		this.pos = new Pos(xPos, yPos);
		penSize = 1;
		penColor = Color.BLACK;

		isVisible = true;
		penDown = false;
		isSparkling = false;

		maxiImage = inputImage.getScaledInstance(MAXI_IMAGE_SIZE, MAXI_IMAGE_SIZE, Image.SCALE_SMOOTH);
		miniImage = inputImage.getScaledInstance(MINI_IMAGE_SIZE, MINI_IMAGE_SIZE, Image.SCALE_SMOOTH);
		image = maxiImage;
		isMini = false;

		lines = new ArrayList<Line>();
		window = RobotWindow.getInstance();
		window.addRobot(this);
	}

	/**
	 * Draws the Robot
	 * 
	 * @param g2
	 *            The graphics object used to draw the Robot.
	 */
	void draw(Graphics2D g2) {
		for (Line l : lines) {
			l.draw(g2);
		}

		if (penDown && currentLine != null) // draws under robot
		{
			currentLine.draw(g2);
		}

		// first cache the standard coordinate system
		AffineTransform cached = g2.getTransform();
		// align the coordinate system with the center of the robot:
		g2.translate(pos.x, pos.y);
		g2.rotate(Math.toRadians(angle));

		if (isVisible) {
			int offset = -(isMini ? MINI_IMAGE_SIZE : MAXI_IMAGE_SIZE) / 2;
			g2.drawImage(image, offset, offset, null);
		}

		if (penDown && isVisible) // draws over robot
		{
			g2.setColor(Color.RED);
			g2.fillOval(-4, -4, 8, 8);
		}

		if (isSparkling) {
			if (isMini) {
				double s = (double) MINI_IMAGE_SIZE / MAXI_IMAGE_SIZE;
				g2.scale(s, s);
			}
			Random r = new Random();
			int xDot = r.nextInt(MAXI_IMAGE_SIZE - 4) - MAXI_IMAGE_SIZE / 2;
			int yDot = r.nextInt(MAXI_IMAGE_SIZE - 4) - MAXI_IMAGE_SIZE / 2;
			g2.setColor(Color.WHITE);
			g2.fillRect(xDot, yDot, 5, 5);
		}
		g2.setTransform(cached); // restore the standard coordinate system
	}

	/**
	 * Changes the image of the Robot
	 * 
	 * @param im
	 *            a BufferedImage containing the robot image. It does not need
	 *            to be to scale since it will be scaled to the appropriate
	 *            size.
	 */
	public void changeRobot(BufferedImage im) {
		Image imMax = im.getScaledInstance(MAXI_IMAGE_SIZE, MAXI_IMAGE_SIZE, Image.SCALE_SMOOTH);
		Image imMin = im.getScaledInstance(MINI_IMAGE_SIZE, MINI_IMAGE_SIZE, Image.SCALE_SMOOTH);
		synchronized (this) {
			maxiImage = imMax;
			miniImage = imMin;
			image = isMini ? miniImage : maxiImage;
		}
	}

	/**
	 * Changes the image of the Robot
	 * 
	 * @param fileName
	 *            The file name without extension of a file in robi format that
	 *            specifies the Robot's image.
	 */
	public void changeRobot(String fileName) {
		changeRobot(RobotImage.loadRobi(fileName));
	}

	/**
	 * Sets the pen size.
	 * 
	 * @param size
	 *            the new pen size given as an integer between 1 and 10.
	 */
	public void setPenWidth(int size) {
		penSize = Math.min(Math.max(1, size), 10);
	}

	/**
	 * Sets the pen color
	 * 
	 * @param color
	 *            the new pen color
	 */
	public void setPenColor(Color color) {
		penColor = color;
	}

	/**
	 * Sets the pen color given the red, green and blue components of the new
	 * color. The components are specified as an integer between 0 and 255.
	 * 
	 * @param r
	 *            the red component of the new color
	 * @param g
	 *            the green component of the new color
	 * @param b
	 *            the blue component of the new color
	 */
	public void setPenColor(int r, int g, int b) {
		r = Math.min(Math.max(0, r), 255);
		g = Math.min(Math.max(0, g), 255);
		b = Math.min(Math.max(0, b), 255);

		penColor = new Color(r, g, b);
	}

	/**
	 * Sets the pen color to a random color.
	 * 
	 */
	public void setRandomPenColor() {
		ThreadLocalRandom rng = ThreadLocalRandom.current();
		int r = rng.nextInt(256);
		int b = rng.nextInt(256);
		int g = rng.nextInt(256);
		penColor = new Color(r, g, b);
	}

	/**
	 * Sets the window's background color
	 * 
	 * @param color
	 *            the new window background color.
	 */
	public void setWindowColor(final Color color) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				window.setWinColor(color);
			}
		});
	}

	/**
	 * Sets the window's background color given the red, green and blue
	 * components of the new color. The components are specified as an integer
	 * between 0 and 255.
	 * 
	 * @param r
	 *            the red component of the new color
	 * @param g
	 *            the green component of the new color
	 * @param b
	 *            the blue component of the new color
	 */
	public void setWindowColor(int r, int g, int b) {
		r = Math.min(Math.max(0, r), 255);
		g = Math.min(Math.max(0, g), 255);
		b = Math.min(Math.max(0, b), 255);

		setWindowColor(new Color(r, g, b));
	}

	private void addLine(final Line line) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				lines.add(line);

			}
		});
	}

	/**
	 * Removes all lines drawn by this Robot.
	 */
	public void clear() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				lines.clear();
			}

		});
	}

	/**
	 * Makes the image of the Robot small
	 */
	public synchronized void miniaturize() {
		image = miniImage;
		isMini = true;
	}

	/**
	 * Makes the image of the Robot big
	 */
	public synchronized void expand() {
		image = maxiImage;
		isMini = false;
	}

	/**
	 * Makes the Robot sparkle.
	 */
	public void sparkle() {
		isSparkling = true;
	}

	/**
	 * Removes sparkles
	 */
	public void unSparkle() {
		isSparkling = false;
	}

	/**
	 * Make the Robot invisible. The Robot is visible initially.
	 */
	public void hide() {
		isVisible = false;
	}

	/**
	 * Makes the Robot visible
	 */
	public void show() {
		isVisible = true;
	}

	/**
	 * Makes the robot move a given distance. A negative distance makes the
	 * robot move backwards.
	 * 
	 * @param distance
	 *            the distance to move in units of points
	 */
	public void move(int distance) {
		float xPos0 = pos.x;
		float yPos0 = pos.y;
		float distanceMoved = 0;
		int sgn = distance < 0 ? -1 : 1;

		double r_angle = Math.toRadians(angle);
		double cos = Math.cos(r_angle);
		double sin = Math.sin(r_angle);

		try {
			while (sgn * (distanceMoved - distance) < 0) {
				leakyBucket.take(); // will block until a TimeQuatum.TICK
									// becomes available
				distanceMoved += sgn * speed;
				if (sgn * (distanceMoved - distance) > 0) {
					distanceMoved = distance;
				}
				float xPos = (float) (xPos0 + distanceMoved * sin);
				float yPos = (float) (yPos0 - distanceMoved * cos);
				pos = new Pos(xPos, yPos);
				if (penDown) {
					Line line = new Line((int) xPos0, (int) yPos0, (int) xPos, (int) yPos, penSize, penColor);
					currentLine = line;
				}
			}
		} catch (InterruptedException e) {
		}
		if (currentLine != null) {
			addLine(currentLine);
			currentLine = null;
		}
	}

	/**
	 * Makes the Robot move one step. If the sgn is negative, the Robot moves
	 * one step backwards. The step size depends on the Robot's speed. This
	 * method is intended to be used by a KeyboardAdapter.
	 * 
	 * @param sgn
	 *            if positive the Robot moves forward, if negative the Robot
	 *            moves backwards.
	 * @throws InterruptedException
	 *             if interrupted before making the move (which is very unlikely
	 *             to happen).
	 * @throws IllegalArgumentException
	 *             if sgn is 0.
	 */
	public void microMove(int sgn) throws InterruptedException {
		if (sgn == 0) {
			throw new IllegalArgumentException("The argument sgn must be non-zero.");
		}
		leakyBucket.take();
		float xPos0 = pos.x;
		float yPos0 = pos.y;

		double rAngle = Math.toRadians(angle);
		double cos = Math.cos(rAngle);
		double sin = Math.sin(rAngle);

		float distanceMoved = sgn * speed;
		float xPos = (float) (xPos0 + distanceMoved * sin);
		float yPos = (float) (yPos0 - distanceMoved * cos);
		pos = new Pos(xPos, yPos);
		if (penDown) {
			Line line = new Line((int) xPos0, (int) yPos0, (int) xPos, (int) yPos, penSize, penColor);
			currentLine = line;
		}
		if (currentLine != null) {
			addLine(currentLine);
			currentLine = null;
		}
	}

	/**
	 * Makes the Robot turn in place a given number of degrees. If the argument
	 * is positive, the Robot turn clockwise, if negative the Robot turns
	 * counter-clockwise.
	 * 
	 * @param degrees
	 *            The number of degrees to turn.
	 */
	public void turn(int degrees) {
		int degreesTurned = 0;
		int sgn = degrees < 0 ? -1 : 1;

		int angle0 = angle;
		try {
			while (sgn * (degreesTurned - degrees) < 0) {
				leakyBucket.take(); // will block until a TimeQuatum.TICK
									// becomes available
				degreesTurned += sgn * speed;
				if (sgn * (degreesTurned - degrees) > 0) {
					degreesTurned = degrees;
				}
				angle = angle0 + degreesTurned;
			}
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Makes the Robot turn in place a small angle. If the argument is positive,
	 * the Robot turn clockwise, if negative the Robot turns counter-clockwise.
	 * The angle turn is dependent on the Robot's speed.
	 * 
	 * @param sgn
	 *            The sign of the direction to turn.
	 * @throws InterruptedException
	 *             if interrupted before making the turn (which is very unlikely
	 *             to happen).
	 * 
	 * @throws IllegalArgumentException
	 *             if the sgn is 0.
	 */
	public void microTurn(int sgn) throws InterruptedException {
		if (sgn == 0) {
			throw new IllegalArgumentException("sgn must be non-zero.");
		}
		leakyBucket.take();
		angle += sgn * speed;
	}

	//
	/**
	 * Waits a given number of milliseconds.
	 * 
	 * @param millis
	 *            the number of milliseconds to wait
	 */
	public void sleep(int millis) {
		try {
			int numTicks = millis / TICK_LENGTH;
			for (int i = 0; i < numTicks; i++) {
				leakyBucket.take();
			}
		} catch (InterruptedException e) {

		}
	}

	/**
	 * Move the Robot to a given position. The Robot does not draw any line.
	 * 
	 * @param x
	 *            the x-coordinate of the new position
	 * 
	 * @param y
	 *            the y-coordinate of the new position
	 */
	public void moveTo(int x, int y) {
		pos = new Pos(x, y);
	}

	/**
	 * Lifts the pen, i.e., the Robot stops drawing lines.
	 */
	public void penUp() {
		penDown = false;
	}

	/**
	 * Set the pen down, i.e., the Robot traces its movements with lines.
	 * 
	 */
	public void penDown() {
		penDown = true;
	}

	/**
	 * Sets the speed of the Robot.
	 * 
	 * @param speed
	 *            the speed specified as a number between 1 and 10.
	 */
	public void setSpeed(int speed) {
		this.speed = Math.min(Math.max(1, speed), 10);
	}

	public void actionPerformed(ActionEvent arg0) {
		leakyBucket.offer(TimeQuantum.TICK);
		window.repaint();
	}

	/**
	 * Adds a KeyboardAdapter to the robot. It is not possible to add more than
	 * one KeyboardAdapter to a Robot.
	 * 
	 * @param adapter
	 *            the KeyboardAdapter
	 */
	public void addKeyboardAdapter(final KeyboardAdapter adapter) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				KeyListener[] listeners = window.getKeyListeners();
				for (int i = 0; i < listeners.length; i++) {
					if (listeners[i] instanceof KeyboardAdapter) {
						KeyboardAdapter a = (KeyboardAdapter) listeners[i];
						if (a.robot == adapter.robot) {
							window.removeKeyListener(a);
							break;
						}
					}
				}
				window.addKeyListener(adapter);
			}
		});
	}
}
