import org.jointheleague.graphical.robot.Robot;

public class MyRobot extends Robot {

	public void drawNgon(int numSides, int radius) {
		int angle = 360 / numSides;
		double sin = Math.sin(Math.toRadians(angle / 2.0));
		int sideLen = (int) (2 * radius * Math.abs(sin));
		penUp();
		move(radius);
		turn(90 + angle / 2);
		penDown();
		move(sideLen);
		for (int i = 1; i < numSides; i++) {
			turn(angle);
			move(sideLen);
		}
		penUp();
		turn(-90 + angle / 2);
	}

}
