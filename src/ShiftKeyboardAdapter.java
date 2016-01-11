
import java.awt.event.KeyEvent;

import org.jointheleague.graphical.robot.KeyboardAdapter;
import org.jointheleague.graphical.robot.Robot;

public class ShiftKeyboardAdapter extends KeyboardAdapter {

	private final boolean shiftDown;

	/**
	 * Constructor.
	 * 
	 * @param robot
	 *            The Robot instance that the KeyboardAdapter controls.
	 */
	public ShiftKeyboardAdapter(Robot robot, boolean shiftDown) {
		super(robot);
		this.shiftDown = shiftDown;
	}

	/**
	 * No actions are specified, but that can be overridden in extensions of
	 * this class.
	 */
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.isShiftDown() == shiftDown) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				setMovingForward(true);
				break;
			case KeyEvent.VK_DOWN:
				setMovingBackward(true);
				break;
			case KeyEvent.VK_LEFT:
				setTurningLeft(true);
				break;
			case KeyEvent.VK_RIGHT:
				setTurningRight(true);
				break;
			default:
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.isShiftDown() == shiftDown) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				setMovingForward(false);
				break;
			case KeyEvent.VK_DOWN:
				setMovingBackward(false);
				break;
			case KeyEvent.VK_LEFT:
				setTurningLeft(false);
				break;
			case KeyEvent.VK_RIGHT:
				setTurningRight(false);
				break;
			default:
			}
		}
	}

}
