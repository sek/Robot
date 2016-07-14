
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jointheleague.graphical.robot.Robot;

public class RobotExample3 {
	
	private static Robot rob;

	public static void main(String[] args) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(RobotExample3.class.getResource("robotImage1.jpg"));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		rob = new Robot(image);
		rob.setSpeed(10);
		rob.penDown();
		rob.addKeyboardAdapter(new ExtendedKeyboardAdapter());
		
	}
	
}
