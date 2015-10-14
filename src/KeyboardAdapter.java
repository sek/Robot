import java.awt.event.KeyEvent;

import org.jointheleague.graphical.robot.Robot;

public class KeyboardAdapter extends org.jointheleague.graphical.robot.KeyboardAdapter {

	private final Robot robot;
	
	public KeyboardAdapter(Robot robot) {
		super(robot);
		this.robot = robot;
	}
	
	
	@Override
	public void keyTyped(KeyEvent e) {
		switch(e.getKeyChar()) {
		case 'm':
			robot.miniaturize();
			break;
		case 'M': 
			robot.expand();
			break;
		case 'u':
			robot.penUp();
			break;
		case 'd':
			robot.penDown();
			break;
		default:
			super.keyTyped(e);
		}
		
	}
	
}
