
import org.jointheleague.graphical.robot.Robot;

import java.awt.Color;

public class RobotExample1
{

	public static void main(String[] args) throws InterruptedException
	{
		Robot june = new Robot("june");
		Robot vic = new Robot(125, 125);
		vic.setSpeed(10);
		vic.miniaturize();
		june.expand();
		vic.penDown();
		vic.setPenColor(Color.RED);
		Thread.sleep(1000);
		int[] moves = new int[]
		{ 260, 150, 260, 150 };
		for (int i = 0; i < moves.length; i++)
		{
			vic.turn(90);
			vic.move(moves[i]);
		}
		vic.turn(90);
		vic.penUp();
		vic.move(185);
		vic.turn(-90);
		vic.setSpeed(2);
		vic.move(-75);
		vic.changeRobot("vic");
		vic.sparkle();
		june.moveTo(200, 200);
		june.sparkle();
		june.setSpeed(5);
		june.turn(-315);
		vic.expand();
		Robot rob = new Robot();
		rob.turn(-720);

	}
}
