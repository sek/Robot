import org.jointheleague.graphical.robot.Robot;

public class Driver {

	private final Robot robot;

	public Driver(Robot robot) {
		super();
		this.robot = robot;
	}

	public void setSpeed(int speed) {
		robot.setSpeed(speed);
	}

	public void turn(int degrees) {
		robot.turn(degrees);
	}

	public void penDown() {
		robot.penDown();
	}

	public void moveTo(int x, int y) {
		robot.moveTo(x, y);
	}

	public void setRandomPenColor() {
		robot.setRandomPenColor();
	}

	public void hide() {
		robot.hide();
	}

	/**
	 * Draws a regular polygon with center at the robot's current position
	 * 
	 * @param numSides
	 *            the number of sides of the polygon
	 * @param radius
	 *            the radius of the circumscribed circle.
	 */
	public void drawPolygon(int numSides, int radius) {
		int angle = 360 / numSides;
		double sin = Math.sin(Math.toRadians(angle / 2.0));
		int sideLen = (int) (2 * radius * Math.abs(sin));
		robot.penUp();
		robot.move(radius);
		robot.turn(90 + angle / 2);
		robot.penDown();
		robot.move(sideLen);
		for (int i = 1; i < numSides; i++) {
			robot.turn(angle);
			robot.move(sideLen);
		}
		robot.penUp();
		robot.turn(-90 + angle / 2);
	}

}
