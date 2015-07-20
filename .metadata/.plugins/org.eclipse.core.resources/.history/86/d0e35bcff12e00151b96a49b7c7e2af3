import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class RobotWindow extends JPanel{
	private int width;
	private int height;
	private JFrame window;
	
	ArrayList<Robot> robotList;
	
	public RobotWindow(int w, int h)
	{
		width = w;
		height = h;
		window = new JFrame();
		window.add(this);
		window.setSize(w, h);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		
		robotList = new ArrayList<Robot>();
	}
	
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setColor(Color.GRAY);
		g2.fillRect(0, 0, width, height);
		
		for(Robot r : robotList)
		{
			r.draw(g2);
		}
	}
	
	public void addRobot(Robot r)
	{
		robotList.add(r);
		update(r);
	}
	
	public void update(Robot r)
	{
		r.update();
		repaint();
	}
}
