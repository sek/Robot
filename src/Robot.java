
//7/24/15
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

public class Robot{
	
	private static final int WIDTH = 1780;
	private static final int HEIGHT = 1024;

	private boolean isVisible;
	private boolean penDown;
	private boolean isSparkling;
	
	private float xPos;
	private float yPos;
	private int angle;
	private int newAngle;
	private int speed;
	private int moveDistance;
	private int distanceMoved;
	
	private int sx;
	private int sy;
	private int tx;
	private int ty;
	private int penSize;
	
	private Color penColor;
	
	private long startTime;

	private RobotImage rImage;
	
	private Line currentLine;
	private ArrayList<Line> lines;

	private static int count = 0;
	private static Color windowColor;
	private static RobotWindow window;
	
	public Robot(){
		this(WIDTH / 2, HEIGHT / 2);
	}
	
	public Robot(String fileName)
	{
		this(WIDTH / 2, HEIGHT / 2, fileName);
	}
	
	public Robot(int x, int y, String fileName)
	{
		xPos = x;
		yPos = y;
		angle = 0;
		speed = 1;
		newAngle = 0;
		moveDistance = 0;
		distanceMoved = 0;
		startTime = 0;
		
		sx = 0;
		sy = 0;
		tx = 0;
		ty = 0;
		penSize = 1;
		penColor = Color.BLACK;
		
		isVisible = true;
		penDown = false;
		isSparkling = false;
		
		rImage = new RobotImage((int)xPos, (int)yPos);
		
		currentLine = new Line(0, 0, 0, 0, 0, Color.BLACK);
		lines = new ArrayList<Line>();
		
		if(count == 0)
		{
			windowColor = new Color(100, 100, 100);
			window = new RobotWindow(WIDTH, HEIGHT, windowColor);
		}
		
		count++;
		changeRobot(fileName);
		window.addRobot(this);
	}
	
	public Robot(int x, int y)
	{
		xPos = x;
		yPos = y;
		angle = 0;
		speed = 1;
		newAngle = 0;
		moveDistance = 0;
		distanceMoved = 0;
		startTime = 0;
		
		sx = 0;
		sy = 0;
		tx = 0;
		ty = 0;
		penSize = 1;
		penColor = Color.BLACK;
		
		isVisible = true;
		penDown = false;
		
		rImage = new RobotImage((int)xPos, (int)yPos);
		
		currentLine = new Line(0, 0, 0, 0, 0, Color.BLACK);
		lines = new ArrayList<Line>();
		
		if(count == 0)
		{
			windowColor = new Color(100, 100, 100);
			window = new RobotWindow(WIDTH, HEIGHT, windowColor);
		}
		
		count++;
		loadDefaultRobot();
		window.addRobot(this);
	}
	
	public void setPenColor(int r, int g, int b)
	{
		if(r > 255)
		{
			r = 255;
		}
		else if(r < 0)
		{
			r = 0;
		}
		
		if(g > 255)
		{
			g = 255;
		}
		else if(g < 0)
		{
			g = 0;
		}
		
		if(b > 255)
		{
			b = 255;
		}
		else if(b < 0)
		{
			b = 0;
		}
		
		penColor = new Color(r, g, b);
	}
	
	public static void setWindowColor(Color c)
	{
		window.setWinColor(c);
	}
	
	public static void setWindowColor(int r, int g, int b)
	{
		if(r < 0)
		{
			r = 0;
		}
		else if(r > 255)
		{
			r = 255;
		}
		
		if(g < 0)
		{
			g = 0;
		}
		else if(g > 255)
		{
			g = 255;
		}
		
		if(b < 0)
		{
			b = 0;
		}
		else if(b > 255)
		{
			b = 255;
		}
		
		window.setWinColor(new Color(r, g, b));
	}
	
	public void setPenWidth(int size)
	{
		if(size < 0)
		{
			size = 0;
		}
		else if(size > 10)
		{
			size = 10;
		}
		
		penSize = size;
	}
	
	public void clear()
	{
		lines.clear();
		window.update(this);
	}
	
	public void sparkle()
	{
		isSparkling = true;
	}
	
	public void unSparkle()
	{
		isSparkling = false;
	}
	
	public void draw(Graphics2D g)
	{
		if(penDown)
		{
			currentLine.draw(g);
		}
		
		for(Line l : lines)
		{
			l.draw(g);
		}
		
		if(isVisible)
		{
			rImage.draw(g);
		}
		
		if(penDown)
		{
			g.setColor(penColor);
			g.fillOval((int)(xPos - (penSize / 2)), (int)(yPos - (penSize / 2)) , penSize, penSize);
		}
		
		if(isSparkling)
		{
			Random r = new Random();
			int xDot = r.nextInt(100) - 50;
			int yDot = r.nextInt(100) - 50;
			g.setColor(Color.WHITE);
			g.fillRect((int)(xPos + xDot), (int)(yPos + yDot), 5, 5);
			window.update(this);
		}
	}
	
	public void setLocation(int x, int y)
	{
		xPos = x;
		yPos = y;
		update();
	}
	
	public void show()
	{
		isVisible = true;
		update();
	}

	public void hide()
	{
		isVisible = false;
		update();
	}
	
	public void update()
	{
		if(distanceMoved < moveDistance)
		{
			float cos = (float)Math.cos(Math.toRadians(-angle));
			float sin = (float)Math.sin(Math.toRadians(-angle));
			int difference = moveDistance - distanceMoved;
			
			if(difference < speed)
			{
				float nextX = difference * sin;
				float nextY = difference * cos;
				xPos -= nextX;
				yPos -= nextY;
				distanceMoved += difference;
			}
			else
			{
				float nextX = speed * sin;
				float nextY = speed * cos;
				xPos -= nextX;
				yPos -= nextY;
				distanceMoved += speed;
			}
			
			tx = (int)xPos;
			ty = (int)yPos;
			
			if(penDown)
			{
				currentLine = new Line(sx, sy, tx, ty, penSize, penColor);
			}
			
			if(moveDistance == distanceMoved)
			{
				lines.add(currentLine);
			}
		}
		else if(distanceMoved > moveDistance)
		{
			float cos = (float)Math.cos(Math.toRadians(-angle));
			float sin = (float)Math.sin(Math.toRadians(-angle));
			int difference = distanceMoved - moveDistance;
			
			if(difference < speed)
			{
				float nextX = difference * sin;
				float nextY = difference * cos;
				xPos += nextX;
				yPos += nextY;
				distanceMoved -= (distanceMoved - moveDistance);
			}
			else
			{
				float nextX = speed * sin;
				float nextY = speed * cos;
				xPos += nextX;
				yPos += nextY;
				distanceMoved -= speed;
			}
			
			tx = (int)xPos;
			ty = (int)yPos;
			
			if(penDown)
			{
				currentLine = new Line(sx, sy, tx, ty, penSize, penColor);
			}
			
			if(moveDistance == distanceMoved)
			{
				lines.add(currentLine);
			}
		}
		
		if(angle < newAngle)
		{
			rImage.rotate(-1);
			angle++;
			
			
		}
		else if(angle > newAngle)
		{		
			rImage.rotate(1);
			angle--;
		}
		
		rImage.x = (int)xPos;
		rImage.y = (int)yPos;
	}

	public void move(int distance)
	{
		distanceMoved = 0;
		moveDistance = distance;
		
		sx = (int)xPos;
		sy = (int)yPos;
		tx = (int)xPos;
		ty = (int)yPos;
		
		if(penDown)
		{
			currentLine = new Line(sx, sy, tx, ty, penSize, penColor);
		}
		
		startTime = System.currentTimeMillis();
		
		while(distanceMoved != moveDistance)
		{
			if((System.currentTimeMillis() - startTime) > (1000 / 60 ))
			{
				window.update(this);
				startTime = System.currentTimeMillis();
			}
		}
	}
	
	public void moveTo(int x, int y)
	{
		xPos = x;
		yPos = y;
		window.update(this);
	}
	
	public void turn(int degrees)
	{
		newAngle = angle + degrees;
		
		startTime = System.currentTimeMillis();
		
		while(angle != newAngle)
		{
			if((System.currentTimeMillis() - startTime) > (10 - speed))
			{
				window.update(this);
				startTime = System.currentTimeMillis();
			}
		}
	}
	
	private int getFileSize(InputStream f)
	{
		int ctr = 0;
		try 
		{
			while(f.read() != -1)
			{
				ctr++;
			}
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, "There was an error loading your file.");
			System.out.println("getFileSize");
		}
		
		return ctr;
	}
	
	private int getFileSize(FileInputStream f)
	{
		int ctr = 0;
		try 
		{
			while(f.read() != -1)
			{
				ctr++;
			}
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, "There was an error loading your file.");
			System.out.println("getFileSize");
		}
		
		return ctr;
	}
	
	private byte[] readToBuffer(InputStream fis, int fileSize)
	{
		byte[] buf = new byte[fileSize];
				
		try 
		{
			for(int i = 0; i < fileSize; i++)
			{
				buf[i] = (byte)fis.read();
			}
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, "There was an error loading your file.");
			System.out.println("readToBuffer");
		}
		
		return buf;
	}
	
	private byte[] readToBuffer(FileInputStream fis, int fileSize)
	{
		byte[] buf = new byte[fileSize];
				
		try 
		{
			for(int i = 0; i < fileSize; i++)
			{
				buf[i] = (byte)fis.read();
			}
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, "There was an error loading your file.");
			System.out.println("readToBuffer");
		}
		
		return buf;
	}
	
	public void penUp()
	{
		penDown = false;
	}
	
	public void penDown()
	{
		penDown = true;
	}
	
	private void loadPixels(byte[] buf)
	{
		try
		{
			int ctr = 0;
			
			for(int i = 0; i < buf.length;)
			{
				byte[] num = new byte[4];
				
				num[0] = buf[i++];
				num[1] = buf[i++];
				num[2] = buf[i++];
				num[3] = buf[i++];
				
				int x = ByteBuffer.wrap(num).getInt();
				
				num[0] = buf[i++];
				num[1] = buf[i++];
				num[2] = buf[i++];
				num[3] = buf[i++];
				
				int y = ByteBuffer.wrap(num).getInt();
				
				num[0] = buf[i++];
				num[1] = buf[i++];
				num[2] = buf[i++];
				num[3] = buf[i++];
				
				int r = ByteBuffer.wrap(num).getInt();
				
				num[0] = buf[i++];
				num[1] = buf[i++];
				num[2] = buf[i++];
				num[3] = buf[i++];
				
				int g = ByteBuffer.wrap(num).getInt();
				
				num[0] = buf[i++];
				num[1] = buf[i++];
				num[2] = buf[i++];
				num[3] = buf[i++];
				
				int b = ByteBuffer.wrap(num).getInt();
				
				Color c;
				
				if(r == 220 &&
				   g == 220 &&
				   b == 220)
				{
					c = new Color (0, 0, 0, 0);
				}
				else
				{
					c = new Color(r, g, b);
				}
				
				rImage.setPixel(ctr++, x, y, c);			
			}
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "There was an error loading your file.");
			System.out.println("loadPixels");
		}
	}
	
	public void loadDefaultRobot()
	{
		String fileName = "rob.robi";
		try
		{
			InputStream is = this.getClass().getResourceAsStream(fileName);
			int fileSize = getFileSize(is);
			is.close();
			
			is = this.getClass().getResourceAsStream(fileName);
			byte[] buffer = readToBuffer(is, fileSize);
			is.close();
			
			loadPixels(buffer);
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "There was an error loading your file.");
			System.out.println("loadDefaultRobot");
		}
	}
	
	public void changeRobot(String fileName)
	{
		fileName += ".robi";
		try 
		{
			FileInputStream fis =  new FileInputStream(fileName);
			int fileSize = getFileSize(fis);
			fis.close();
			
			fis = new FileInputStream(fileName);
			byte[] buffer = readToBuffer(fis, fileSize);
			fis.close();
			
			loadPixels(buffer);
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, "There was an error loading your file.");
			System.out.println("changeRobot");
		}
	}
	
	public int getSpeed()
	{
		return speed;
	}
	
	public void setSpeed(int s)
	{
		if(s > 10)
		{
			s = 10;
		}
		else if(s < 1)
		{
			s = 1;
		}
		
		speed = s;
		
	}
	
	public void setAngle(int a)
	{
		int turnAmt = angle - a;
		rImage.rotate(turnAmt);
		angle = a;
	}
}