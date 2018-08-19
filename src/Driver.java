import org.jointheleague.graphical.robot.KeyboardAdapter;
import org.jointheleague.graphical.robot.Robot;
import org.jointheleague.graphical.robot.RobotInterface;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Driver implements RobotInterface {

    private final Robot robot;

    public Driver(Robot robot) {
        super();
        this.robot = robot;
    }

    @Override
    public void setSpeed(int speed) {
        robot.setSpeed(speed);
    }

    @Override
    public void addKeyboardAdapter(KeyboardAdapter adapter) {
        robot.addKeyboardAdapter(adapter);
    }

    @Override
    public void turn(int degrees) {
        robot.turn(degrees);
    }

    @Override
    public void microTurn(int sgn) throws InterruptedException {
        robot.microTurn(sgn);
    }

    @Override
    public void sleep(int millis) {
        robot.sleep(millis);
    }

    @Override
    public void penUp() {
        robot.penUp();
    }

    @Override
    public void penDown() {
        robot.penDown();
    }

    @Override
    public void move(int distance) {
        robot.move(distance);
    }

    @Override
    public void microMove(int sgn) throws InterruptedException {
        robot.microMove(sgn);
    }

    @Override
    public void moveTo(int x, int y) {
        robot.moveTo(x, y);
    }

    @Override
    public void changeRobot(BufferedImage im) {
        robot.changeRobot(im);
    }

    @Override
    public void changeRobot(String urlName) {
        robot.changeRobot(urlName);
    }

    @Override
    public int getPenWidth() {
        return robot.getPenWidth();
    }

    @Override
    public void setPenWidth(int size) {
        robot.setPenWidth(size);
    }

    @Override
    public Color getPenColor() {
        return robot.getPenColor();
    }

    @Override
    public void setPenColor(Color color) {
        robot.setPenColor(color);
    }

    @Override
    public void setPenColor(int r, int g, int b) {
        robot.setPenColor(r, g, b);
    }

    @Override
    public void setRandomPenColor() {
        robot.setRandomPenColor();
    }

    @Override
    public void clearLines() {
        robot.clearLines();
    }

    @Override
    public void miniaturize() {
        robot.miniaturize();
    }

    @Override
    public void expand() {
        robot.expand();
    }

    @Override
    public void sparkle() {
        robot.sparkle();
    }

    @Override
    public void unSparkle() {
        robot.unSparkle();
    }

    @Override
    public void hide() {
        robot.hide();
    }

    @Override
    public void show() {
        robot.show();
    }

    /**
     * Draws a regular polygon with center at the robot's current position
     *
     * @param numSides the number of sides of the polygon
     * @param radius   the radius of the circumscribed circle.
     */
    public void drawPolygon(int numSides, int radius) {
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
