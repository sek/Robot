package org.jointheleague.graphical.robot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.swing.SwingUtilities;

public class Robot implements ActionListener
{

	private static final int	MAXI_IMAGE_SIZE	= 100;
	private static final int	MINI_IMAGE_SIZE	= 25;
	private boolean				isVisible;

	private float				xPos;
	private float				yPos;
	private int					angle;
	private int					speed;

	private boolean				penDown;
	private int					penSize;
	private Color				penColor;

	private Image				maxiImage;
	private Image				miniImage;
	private Image				image;
	private boolean				isMini			= false;

	private boolean				isSparkling;

	private ArrayList<Line>		lines;
	private Line				currentLine;

	private RobotWindow			window;

	private enum TimeQuantum
	{
		TICK
	}

	private BlockingQueue<TimeQuantum>	leakyBucket	= new ArrayBlockingQueue<>(
															1);

	public Robot(int x, int y)
	{
		this("rob", x, y);
	}

	/**
	 * 
	 * @param fileName
	 *            the name of the file containing the Robot image, without the
	 *            "robi" extension.
	 * @param pos
	 *            if not empty, has two elements x and y
	 */
	public Robot(String fileName, int... pos)
	{
		if (!(pos.length == 0 || pos.length == 2))
		{
			throw new IllegalArgumentException(
					"Must include x and y coordinates only or no coordinates");
		}
		angle = 0;
		speed = 1;

		penSize = 1;
		penColor = Color.BLACK;

		isVisible = true;
		penDown = false;
		isSparkling = false;

		image = RobotImage.loadRobi(fileName);
		maxiImage = image.getScaledInstance(MAXI_IMAGE_SIZE, MAXI_IMAGE_SIZE,
				Image.SCALE_SMOOTH);
		miniImage = image.getScaledInstance(MINI_IMAGE_SIZE, MINI_IMAGE_SIZE,
				Image.SCALE_SMOOTH);
		isMini = false;
		image = maxiImage;

		lines = new ArrayList<Line>();
		window = RobotWindow.getInstance();
		window.addRobot(this, pos);
	}

	void draw(Graphics2D g)
	{
		for (Line l : lines)
		{
			l.draw(g);
		}

		if (penDown && currentLine != null)// draws under robot
		{
			currentLine.draw(g);
		}

		// first cache the standard coordinate system
		AffineTransform cached = g.getTransform();
		// align the coordinate system with the center of the robot:
		g.translate(xPos, yPos);
		g.rotate(Math.toRadians(angle));

		if (isVisible)
		{
			int offset = -(isMini ? MINI_IMAGE_SIZE : MAXI_IMAGE_SIZE) / 2;
			g.drawImage(image, offset, offset, null);
		}

		if (penDown)// draws over robot
		{
			g.setColor(penColor);
			int pSize = penSize + 3;
			g.fillOval(-pSize / 2, -pSize / 2, pSize, pSize);
		}

		if (isSparkling)
		{
			if (isMini)
			{
				double s = (double) MINI_IMAGE_SIZE / MAXI_IMAGE_SIZE;
				g.scale(s, s);
			}
			Random r = new Random();
			int xDot = r.nextInt(MAXI_IMAGE_SIZE - 4) - MAXI_IMAGE_SIZE / 2;
			int yDot = r.nextInt(MAXI_IMAGE_SIZE - 4) - MAXI_IMAGE_SIZE / 2;
			g.setColor(Color.WHITE);
			g.fillRect(xDot, yDot, 5, 5);
		}
		g.setTransform(cached); // restore the standard coordinate system
	}

	public void changeRobot(String fileName)
	{
		image = RobotImage.loadRobi(fileName);
		maxiImage = image.getScaledInstance(MAXI_IMAGE_SIZE, MAXI_IMAGE_SIZE,
				Image.SCALE_SMOOTH);
		miniImage = image.getScaledInstance(MINI_IMAGE_SIZE, MINI_IMAGE_SIZE,
				Image.SCALE_SMOOTH);
		image = isMini ? miniImage : maxiImage;
	}

	public void setPenColor(Color c)
	{
		penColor = c;
	}

	public void setPenColor(int r, int g, int b)
	{
		r = Math.min(Math.max(0, r), 255);
		g = Math.min(Math.max(0, g), 255);
		b = Math.min(Math.max(0, b), 255);

		penColor = new Color(r, g, b);
	}

	public void setWindowColor(Color c)
	{
		window.setWinColor(c);
	}

	public void setWindowColor(int r, int g, int b)
	{
		r = Math.min(Math.max(0, r), 255);
		g = Math.min(Math.max(0, g), 255);
		b = Math.min(Math.max(0, b), 255);

		window.setWinColor(new Color(r, g, b));
	}

	public void setPenWidth(int size)
	{
		penSize = Math.min(Math.max(1, size), 10);
	}

	private void addLine(final Line line)
	{
		SwingUtilities.invokeLater(new Runnable()
		{

			@Override
			public void run()
			{
				lines.add(line);

			}
		});
	}

	public void clear()
	{
		SwingUtilities.invokeLater(new Runnable()
		{

			@Override
			public void run()
			{
				lines.clear();
			}

		});
	}

	public void miniaturize()
	{
		image = miniImage;
		isMini = true;
	}

	public void expand()
	{
		image = maxiImage;
		isMini = false;
	}

	public void sparkle()
	{
		isSparkling = true;
	}

	public void unSparkle()
	{
		isSparkling = false;
	}

	public void show()
	{
		isVisible = true;
	}

	public void hide()
	{
		isVisible = false;
	}

	public void move(int distance)
	{
		float xPos0 = xPos;
		float yPos0 = yPos;
		int distanceMoved = 0;
		int sgn = distance < 0 ? -1 : 1;

		double r_angle = Math.toRadians(angle);
		double cos = Math.cos(r_angle);
		double sin = Math.sin(r_angle);

		try
		{
			while (sgn * (distanceMoved - distance) < 0)
			{
				leakyBucket.take(); // will block until a TimeQuatum.TICK
									// becomes available
				distanceMoved += sgn * speed;
				if (sgn * (distanceMoved - distance) > 0)
				{
					distanceMoved = distance;
				}
				xPos = (float) (xPos0 + distanceMoved * sin);
				yPos = (float) (yPos0 - distanceMoved * cos);
				if (penDown)
				{
					currentLine = new Line((int) xPos0, (int) yPos0,
							(int) xPos, (int) yPos, penSize, penColor);
				}
			}
		} catch (InterruptedException e)
		{
		}
		if (currentLine != null)
		{
			addLine(currentLine);
			currentLine = null;
		}
	}

	public void turn(int degrees)
	{
		int degreesTurned = 0;
		int sgn = degrees < 0 ? -1 : 1;

		int angle0 = angle;
		try
		{
			while (sgn * (degreesTurned - degrees) < 0)
			{
				leakyBucket.take(); // will block until a TimeQuatum.TICK
									// becomes available
				degreesTurned += sgn * speed;
				if (sgn * (degreesTurned - degrees) > 0)
				{
					degreesTurned = degrees;
				}
				angle = angle0 + degreesTurned;
			}
		} catch (InterruptedException e)
		{
		}
	}

	public void moveTo(int x, int y)
	{
		xPos = x;
		yPos = y;
	}

	public void setX(int newX)
	{
		xPos = newX;
	}

	public void setY(int newY)
	{
		yPos = newY;
	}

	public void penUp()
	{
		penDown = false;
	}

	public void penDown()
	{
		penDown = true;
	}

	public void setSpeed(int s)
	{
		speed = Math.min(Math.max(1, s), 10);
	}

	public void setAngle(int a)
	{
		angle = a;
	}

	public void setRandomPenColor()
	{
		Random random = new Random();
		this.penColor = new Color(random.nextInt(256), random.nextInt(256),
				random.nextInt(256));
	}

	public void actionPerformed(ActionEvent arg0)
	{
		leakyBucket.offer(TimeQuantum.TICK);
		window.repaint();
	}
}
