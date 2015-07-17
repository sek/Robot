import java.awt.Color;
import java.awt.Graphics2D;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Robot{
	
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 1024;

	private boolean isVisible;
	private boolean penDown;
	
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
	
	private static RobotWindow window;
	
	public Robot(){
		this(WIDTH / 2, HEIGHT / 2);
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
			window = new RobotWindow(WIDTH, HEIGHT);
		}
		
		count++;
		changeRobot("rover.robi");
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
	
	
	public void setPenSize(int size)
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
			float nextX = speed * sin;
			float nextY = speed * cos;
			xPos -= nextX;
			yPos -= nextY;
			
			if(moveDistance - distanceMoved < speed)
			{
				distanceMoved += (moveDistance - distanceMoved);
			}
			else
			{
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
			float nextX = speed * sin;
			float nextY = speed * cos;
			xPos += nextX;
			yPos += nextY;
			
			if(distanceMoved  - moveDistance < speed)
			{
				distanceMoved += (distanceMoved - moveDistance);
			}
			else
			{
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
			if((System.currentTimeMillis() - startTime) > (1000 / 60))
			{
				window.update(this);
				startTime = System.currentTimeMillis();
			}
		}
	}
	
	public void turn(int degrees)
	{
		newAngle = angle + degrees;
		
		startTime = System.currentTimeMillis();
		
		while(angle != newAngle)
		{
			if((System.currentTimeMillis() - startTime) > (1000 / (60 * speed)))
			{
				window.update(this);
				startTime = System.currentTimeMillis();
			}
		}
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
	
	public void changeRobot(String fileName)
	{
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
			System.out.println("loadImage");
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
			speed = 10;
		}
		else if(s < 1)
		{
			speed = 1;
		}
		else
		{
			speed = s;
		}
	}
}
