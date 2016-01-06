package org.jointheleague.graphical.robot;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * <p>
 * This class is used to control the robot through key presses. The up, down,
 * left, right arrow keys are used to make the robot advance, go backwards, turn
 * left, and turn right respectively. Further keys may be used to control the
 * robot in other ways by extending this class and overriding the
 * {@link #keyPressed(KeyEvent)}, {@link #keyReleased(KeyEvent)}, and
 * {@link #keyTyped(KeyEvent)} methods.
 * </p>
 * <p>
 * A KeyboardAdapter controls the Robot from its own thread. It is possible to
 * have more than one Robot controlled by a KeyboardAdapter.
 * </p>
 * <p>
 * Example of how to use:
 * </p>
 * 
 * <pre>
 * {@code
 * public static void main(String[] args) throws InterruptedException
 * {
 *	Robot rob = new Robot();
 *	rob.addKeyboardAdapter(new KeyboardAdapter(rob));
 * }
 * }
 * </pre>
 * 
 * 
 * @author Erik Colban &copy; 2016
 *
 */
public class KeyboardAdapter implements KeyListener, Runnable {

	/**
	 * The Robot instance that the KeyboardAdapter is attached to.
	 */
	protected Robot robot;
	private volatile boolean movingForward = false;
	private volatile boolean movingBackward = false;
	private volatile boolean turningLeft = false;
	private volatile boolean turningRight = false;

	/**
	 * Constructor.
	 * 
	 * @param robot
	 *            The Robot instance that the KeyboardAdapter is to be attached
	 *            to.
	 */
	public KeyboardAdapter(Robot robot) {
		this.robot = robot;
		new Thread(this).start();
	}

	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {

			try {
				if (movingForward && !movingBackward) {
					robot.microMove(1);
				} else if (movingBackward && !movingForward) {
					robot.microMove(-1);
				} else if (turningRight && !turningLeft) {
					robot.microTurn(1);
				} else if (turningLeft && !turningRight) {
					robot.microTurn(-1);
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * Sets or unsets the Robot in a forward motion.   
	 * 
	 * @param movingForward
	 *            true to set the Robot in forward motion, false to cancel
	 *            forward motion.
	 */
	protected void setMovingForward(boolean movingForward) {
		this.movingForward = movingForward;
	}

	/**
	 * Sets or unsets the Robot in a backward motion.  
	 * 
	 * @param movingBackward
	 *            true to set the Robot in backward motion, false to cancel
	 *            backward motion.
	 */
	protected void setMovingBackward(boolean movingBackward) {
		this.movingBackward = movingBackward;
	}

	/**
	 * Sets or unsets the Robot in a left turning motion.  
	 * 
	 * @param turningLeft
	 *            true to set the Robot in a left turning motion, false to
	 *            cancel the turning motion.
	 */
	protected void setTurningLeft(boolean turningLeft) {
		this.turningLeft = turningLeft;
	}

	/**
	 * Sets or unsets the Robot in a left turning motion.  
	 * 
	 * @param turningRight
	 *            true to set the Robot in a right turning motion, false to
	 *            cancel the turning motion.
	 */
	protected void setTurningRight(boolean turningRight) {
		this.turningRight = turningRight;
	}

	/**
	 * No actions are specified, but that can be overridden in extensions of
	 * this class.
	 */
	@Override
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * Implements the natural movements associated with the up, down, left, and
	 * right arrow keys.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			movingForward = true;
			break;
		case KeyEvent.VK_DOWN:
			movingBackward = true;
			break;
		case KeyEvent.VK_LEFT:
			turningLeft = true;
			break;
		case KeyEvent.VK_RIGHT:
			turningRight = true;
			break;
		default:
		}

	}

	/**
	 * Implements the natural movements associated with the up, down, left, and
	 * right arrow keys.
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			movingForward = false;
			break;
		case KeyEvent.VK_DOWN:
			movingBackward = false;
			break;
		case KeyEvent.VK_LEFT:
			turningLeft = false;
			break;
		case KeyEvent.VK_RIGHT:
			turningRight = false;
			break;
		default:
		}
	}

}
