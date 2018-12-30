package examples;

import org.jointheleague.graphical.robot.Robot;
import org.jointheleague.graphical.robot.RobotWindow;

import java.awt.*;
import java.awt.geom.Ellipse2D;

;

public class RobotExample12 {

    public static void main(String[] args) {

        Robot rob = new Robot();
        RobotWindow window = RobotWindow.getInstance();
        Shape ellipse = new Ellipse2D.Float(50, 50, window.getWidth() - 100, window.getHeight() - 100);
        rob.setSpeed(10);
        rob.setPenWidth(20);
        rob.setPenColor(Color.BLUE);
        rob.sleep(1000);
        rob.penDown();
        rob.followPath(ellipse.getPathIterator(null));
        rob.penUp();
        rob.moveTo(window.getWidth() / 2F, window.getHeight() / 2F, false, false);
    }

}