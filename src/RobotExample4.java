
import java.awt.Color;

import org.jointheleague.graphical.robot.Robot;

public class RobotExample4 {
	public static void main(String[] args) throws InterruptedException {
		Robot rob = new Robot();
		rob.setSpeed(2);
		rob.penDown();
		rob.setPenColor(Color.GRAY);
		rob.setWindowColor(Color.WHITE);
		rob.penUp();
		rob.move(-10);
		rob.penDown();
		rob.move(20);
		rob.penUp();
		rob.move(-10);
		rob.turn(90);
		rob.move(-10);
		rob.penDown();
		rob.move(20);
		rob.penUp();
		rob.move(-10);
		rob.turn(-144);
		rob.move(123);
		rob.turn(144);
		rob.setPenWidth(8);
		rob.penDown();
		
		for(int i = 0; i < 10; i++) {
			rob.setRandomPenColor();
			rob.move(200);
			rob.turn(108);
		}
		rob.penUp();
		rob.move(-300);
		Thread.sleep(1000);
		rob.hide();
	}
}
