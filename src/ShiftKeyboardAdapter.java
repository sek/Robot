
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
	 * @param shiftDown
	 *            if true, the Shift key needs to be depressed to react to arrow
	 *            keys; if false, the Shift key must not be depressed to react
	 *            to arrow keys.
	 */
	public ShiftKeyboardAdapter(Robot robot, boolean shiftDown) {
		super(robot);
		this.shiftDown = shiftDown;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.isShiftDown() == shiftDown) {
			super.keyPressed(e);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.isShiftDown() == shiftDown) {
			super.keyReleased(e);
		}
	}

}
