import org.jointheleague.graphical.robot.Robot;
import java.awt.Color;

public class RobotExample {

    public static void main(String[] args) {
        Robot june = new Robot(200, 200, "june");
        Robot vic = new Robot(125, 125);
        vic.setSpeed(10);
        vic.miniturize();
        june.expand();
        vic.penDown();
        vic.setPenColor(Color.RED);
        vic.turn(90);
        vic.move(260);
        vic.turn(90);
        vic.move(150);
        vic.turn(90);
        vic.move(260);
        vic.turn(90);
        vic.move(150);
        vic.turn(90);
        vic.move(185);
        vic.turn(-90);
        vic.setSpeed(1);
        vic.penUp();
        vic.move(-75);
        vic.changeRobot("vic");
        vic.expand();
        vic.sparkle();
        june.sparkle();

    }
}
