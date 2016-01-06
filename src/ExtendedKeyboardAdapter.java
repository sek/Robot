
import java.awt.event.KeyEvent;

import org.jointheleague.graphical.robot.KeyboardAdapter;
import org.jointheleague.graphical.robot.Robot;

public class ExtendedKeyboardAdapter extends KeyboardAdapter {

	
	public ExtendedKeyboardAdapter(Robot robot) {
		super(robot);
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
