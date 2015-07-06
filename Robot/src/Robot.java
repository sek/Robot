import java.awt.Color;
import java.awt.Graphics;

public class Robot{
	
	private static final int WIDTH = 760;
	private static final int HEIGHT = 540;
	
	private int xPos;
	private int yPos;
	private int angle;
	private int speed;
	private int moveDistance;
	
	private double angleRad;
	
	private long startTime;
	
	private int xPoints[] = {0, 0, 0};
	private int yPoints[] = {0, 0, 0};
	
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
		
		startTime = 0;
		
		xPoints[0] = xPos - 25;
		xPoints[1] = xPos;
		xPoints[2] = xPos + 25;
		
		yPoints[0] = yPos + 25;
		yPoints[1] = yPos - 25;
		yPoints[2] = yPos + 25;
		
		if(count == 0)
		{
			window = new RobotWindow(WIDTH, HEIGHT);
		}
		
		count++;
		window.addRobot(this);
	}
	
	public void draw(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.fillPolygon(xPoints, yPoints, 3);
		g.setColor(Color.RED);
		g.fillOval(xPos - 2, yPos - 2, 4, 4);
	}
	
	public void setLocation(int x, int y)
	{
		xPos = x;
		yPos = y;
	}
	
	public void update()
	{
		if(xPos < moveDistance)
		{
			xPos += speed;
		}
		else if(xPos > moveDistance)
		{
			xPos -= speed;
		}
		
		double sin = Math.sin(angleRad);
		double cos = Math.sin(angleRad);
		
		xPoints[0] = (int)(((xPos - 25) * cos) - ((yPos + 25) * sin));
		xPoints[1] = (int)(((xPos) * cos) - ((yPos - 25) * sin));
		xPoints[2] = (int)(((xPos + 25) * cos) - ((yPos + 25) * sin));
		
		yPoints[0] = (int)(((xPos - 25) * sin) - ((yPos + 25) * cos));
		yPoints[1] = (int)(((xPos) * sin) - ((yPos - 25) * cos));
		yPoints[2] = (int)(((xPos + 25) * sin) - ((yPos + 25) * cos));
	}
	
	public void move(int distance)
	{
		moveDistance = xPos + distance;
		
		startTime = System.currentTimeMillis();
		
		while(xPos != moveDistance)
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
		angle = degrees;
		angleRad = Math.toRadians(angle);
	}
}
