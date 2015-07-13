
public class Vector2D {
	protected float x;
	protected float y;
	
	public Vector2D()
	{
		this(0, 0);
	}
	
	public Vector2D(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void rotate(int angle)
	{
		float cos = (float)Math.cos(Math.toRadians(angle));
		float sin = (float)Math.sin(Math.toRadians(angle));
		float nx = (x * cos) - (y * sin);
		float ny = (x * sin) + (y * cos);
		
		x = nx;
		y = ny;
	}
	
	public void rotateAroundPoint(int angle, float px, float py)
	{
		float cos = (float)Math.cos(Math.toRadians(angle));
		float sin = (float)Math.sin(Math.toRadians(angle));
		float originX = px - x;
		float originY = y - py;
		float nx = (originX * cos) - (originY * sin);
		float ny = (originX * sin) + (originY * cos);
		
		x = px - nx;
		y = py + ny;
	}
}
