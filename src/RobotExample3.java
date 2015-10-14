import org.jointheleague.graphical.robot.Robot;

public class RobotExample3 {
	public static void main(String[] args) {
		Robot rob = new Robot();
		rob.setSpeed(10);
		rob.penDown();
		rob.addKeyboardAdapter(new KeyboardAdapter(rob));
		// uses the KeyboardAdaper defined in this package
		
	}
}
