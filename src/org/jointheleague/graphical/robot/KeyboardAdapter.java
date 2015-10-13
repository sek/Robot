package org.jointheleague.graphical.robot;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardAdapter implements KeyListener, Runnable {

	private final Robot robot;
	private final Thread keyboardThread;
	private volatile boolean running = true;
	private volatile boolean movingForward = false;
	private volatile boolean movingBackward = false;
	private volatile boolean turningLeft = false;
	private volatile boolean turningRight = false;

	public KeyboardAdapter(Robot robot) {
		this.robot = robot;
		this.keyboardThread = new Thread(this);
		keyboardThread.start();
//		this.run();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public synchronized void keyPressed(KeyEvent e) {
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

	@Override
	public synchronized void keyReleased(KeyEvent e) {
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

	@Override
	public void run() {
		while (running) {
			if (movingForward && !movingBackward) {
				robot.microMove(1);
			} else if (movingBackward && !movingForward) {
				robot.microMove(-1);
			} else if (turningRight && !turningLeft) {
				robot.microTurn(1);
			} else if (turningLeft && !turningRight) {
				robot.microTurn(-1);
			}
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				running = false;
			}
		}
	}

	public void stop() {
		this.running = false;
	}

}
