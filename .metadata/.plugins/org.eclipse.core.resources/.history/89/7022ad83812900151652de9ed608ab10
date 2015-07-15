import java.awt.Color;
import java.awt.Graphics;

public class Image {
	
	Vector2D[] pixels;
	public int x;
	public int y;
	public int width;
	public int height;
	
	public Image(int w, int h)
	{
		width = w;
		height = h;
		x = 0;
		y = 0;
		
		pixels = new Vector2D[width * height];
		
		int ctr = 0;
		for(int i = 0; i < width; i++)
		{
			for(int j = 0; j < height; j++)
			{
				pixels[ctr++] = new Vector2D(i, j);
			}
		}
		
		for(int i = 0; i < pixels.length; i++)
		{
			System.out.println(pixels[i].x + " -- " + pixels[i].y);
		}
	}
	
	public void draw(Graphics g)
	{
		for(int i = 0; i < pixels.length; i++)
		{
			if(((int)pixels[i].x + (int)pixels[i].y) % 2 == 0)
			{
				g.setColor(Color.BLUE);
			}
			else
			{
				g.setColor(Color.RED);
			}
			
			pixels[i].rotateAroundPoint(1, 250, 250);
			
			g.drawRect((int)pixels[i].x + x, (int)pixels[i].y + y, 1, 1);
		}
	}
}
