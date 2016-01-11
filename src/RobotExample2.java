
import org.jointheleague.graphical.robot.KeyboardAdapter;
import org.jointheleague.graphical.robot.Robot;

public class RobotExample2
{

	public static void main(String[] args) throws InterruptedException
	{
		Robot rob = new Robot();
		rob.setSpeed(5);
		rob.miniaturize();
		Robot.addKeyboardAdapter(new KeyboardAdapter(rob));
	}
}
