import java.awt.Color;
import java.awt.Graphics2D;

public class RobotImage {
	Pixel[] pix;
	
	public int x;
	public int y;
	public int width;
	public int height;
	
	public RobotImage(int ix, int iy)
	{
		width = 100;
		height = 100;
		x = ix;
		y = iy;
		pix = new Pixel[width * height];
		
		int ctr = 0;
		for(int i = 0; i < width; i++)
		{
			for(int j = 0; j < height; j++)
			{
				pix[ctr++] = new Pixel(i , j);
			}
		}
	}
	
	public void draw(Graphics2D g)
	{
		for(int i = 0; i < pix.length; i++)
		{
			int dx = ((int)pix[i].x + x) - (width / 2);
			int dy = ((int)pix[i].y + y) - (height / 2);
			
			g.setColor(pix[i].getColor());
			g.drawRect(dx, dy, 1, 1);
		}
	}
	
	public void rotate(int angle)
	{
		for(int i = 0; i < pix.length; i++)
		{
			pix[i].rotateAroundPoint(angle, width / 2, height / 2);
		}
	}
	
	public void rotateAroundPoint(int angle, int px, int py)
	{
		for(int i = 0; i < pix.length; i++)
		{
			pix[i].rotateAroundPoint(angle, px, py);
		}
	}
	
	public void setPixel(int index, int x, int y, Color c)
	{
		pix[index] = new Pixel(x, y, c);
	}
}
