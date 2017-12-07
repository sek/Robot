
import org.jointheleague.graphical.robot.Robot;

import java.awt.Color;

public class RobotExample1
{

	public static void main(String[] args)
	{
		
		Robot rob = new Robot(125, 125);
		
		
		rob.setSpeed(10);
		rob.miniaturize();
		rob.penDown();
		rob.setPenColor(Color.RED);
		rob.sleep(1000);
		
		int[] moves = { 260, 150, 260, 150 };
		for (int i = 0; i < moves.length; i++)
		{
			rob.turn(90);
			rob.move(moves[i]);
		}
		rob.turn(90);
		rob.penUp();
		rob.move(185);
		rob.turn(-90);
		rob.setSpeed(2);
		rob.move(-75);
	}
}