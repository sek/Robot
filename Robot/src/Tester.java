import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Tester extends JPanel implements ActionListener{
	
	JFrame window;
	Timer timer;
	
	Image i;
	
	public static void main(String[] args) {
		new Tester();
	}
	
	public Tester()
	{
		window = new JFrame();
		window.add(this);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(1000, 1000);
		window.setVisible(true);
		
		i = new Image(500, 500);
		
		timer = new Timer(1000 / 60, this);
		timer.start();
	}
	
	public void paint(Graphics g)
	{
		i.draw(g);
	}
	
	public void update()
	{
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		update();
		repaint();
		// TODO Auto-generated method stub
		
	}
}
