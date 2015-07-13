import java.awt.Color;
import java.awt.Graphics2D;

public class Robot{
	
	private static final int WIDTH = 1080;
	private static final int HEIGHT = 760;

	private float xPos;
	private float yPos;
	private int angle;
	private int newAngle;
	private int speed;
	private int moveDistance;
	private int distanceMoved;
	
	private float lastX;
	private float lastY;
	
	private long startTime;

	private RobotImage rImage;
	
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
		lastX = xPos;
		lastY = yPos;
		startTime = 0;
		
		rImage = new RobotImage((int)xPos, (int)yPos);
		
		if(count == 0)
		{
			window = new RobotWindow(WIDTH, HEIGHT);
		}
		
		count++;
		window.addRobot(this);
	}
	
	public void draw(Graphics2D g)
	{
		rImage.draw(g);
		g.setColor(Color.BLUE);
		g.fillOval((int)xPos - 2, (int)yPos - 2, 4, 4);
	}
	
	public void setLocation(int x, int y)
	{
		xPos = x;
		yPos = y;
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
			
			distanceMoved += speed;
		}
		else if(distanceMoved > moveDistance)
		{
			float cos = (float)Math.cos(Math.toRadians(-angle));
			float sin = (float)Math.sin(Math.toRadians(-angle));
			float nextX = speed * sin;
			float nextY = speed * cos;
			xPos += nextX;
			yPos += nextY;
			
			distanceMoved -= speed;
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
		
		lastX = xPos;
		lastY = yPos;
		
		rImage.x = (int)xPos;
		rImage.y = (int)yPos;
	}
	
	public void move(int distance)
	{
		distanceMoved = 0;
		moveDistance = distance;
		
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
