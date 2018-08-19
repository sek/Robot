package org.jointheleague.graphical.robot;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface RobotInterface {

    /**
     * Changes the image of the Robot
     *
     * @param im a BufferedImage containing the robot image. It does not need
     *           to be to scale since it will be scaled to the appropriate
     *           size.
     */
    void changeRobot(BufferedImage im);

    /**
     * Changes the image of the Robot
     *
     * @param urlName The URL of the image that specifies the Robot's image.
     */
    void changeRobot(String urlName);

    int getPenWidth();

    /**
     * Sets the pen size.
     *
     * @param size the new pen size given as an integer between 1 and 10.
     */
    void setPenWidth(int size);

    Color getPenColor();

    /**
     * Sets the pen color
     *
     * @param color the new pen color
     */
    void setPenColor(Color color);

    /**
     * Sets the pen color given the red, green and blue components of the new
     * color. The components are specified as an integer between 0 and 255.
     *
     * @param r the red component of the new color
     * @param g the green component of the new color
     * @param b the blue component of the new color
     */
    void setPenColor(int r, int g, int b);

    /**
     * Sets the pen color to a random color.
     */
    void setRandomPenColor();

    /**
     * Removes all lines drawn by this Robot.
     */
    void clearLines();

    /**
     * Makes the image of the Robot small
     */
    void miniaturize();

    /**
     * Makes the image of the Robot big
     */
    void expand();

    /**
     * Makes the Robot sparkle.
     */
    void sparkle();

    /**
     * Removes sparkles
     */
    void unSparkle();

    /**
     * Make the Robot invisible. The Robot is visible initially.
     */
    void hide();

    /**
     * Makes the Robot visible
     */
    void show();

    /**
     * Makes the robot move a given distance. A negative distance makes the
     * robot move backwards.
     *
     * @param distance the distance to move in units of points
     */
    void move(int distance);

    /**
     * Makes the Robot move one step. If the sgn is negative, the Robot moves
     * one step backwards. The step size depends on the Robot's speed. This
     * method is intended to be used by a KeyboardAdapter.
     *
     * @param sgn if positive the Robot moves forward, if negative the Robot
     *            moves backwards.
     * @throws InterruptedException     if interrupted before making the turn (which is very unlikely
     *                                  to happen).
     * @throws IllegalArgumentException if sgn is 0.
     */
    void microMove(int sgn) throws InterruptedException;

    /**
     * Makes the Robot turn in place a given number of degrees. If the argument
     * is positive, the Robot turn clockwise, if negative the Robot turns
     * counter-clockwise.
     *
     * @param degrees The number of degrees to turn.
     */
    void turn(int degrees);

    /**
     * Makes the Robot turn in place a small angle. If the argument is positive,
     * the Robot turn clockwise, if negative the Robot turns counter-clockwise.
     * The angle turn is dependent on the Robot's speed.
     *
     * @param sgn The sign of the direction to turn.
     * @throws InterruptedException     if interrupted before making the turn (which is very unlikely
     *                                  to happen).
     * @throws IllegalArgumentException if the sgn is 0.
     */
    void microTurn(int sgn) throws InterruptedException;

    /**
     * Waits a given number of milliseconds.
     *
     * @param millis the number of milliseconds to wait
     */
    void sleep(int millis);

    /**
     * Move the Robot to a given position. The Robot does not draw any line.
     *
     * @param x the x-coordinate of the new position
     * @param y the y-coordinate of the new position
     */
    void moveTo(int x, int y);

    /**
     * Lifts the pen, i.e., the Robot stops drawing lines.
     */
    void penUp();

    /**
     * Set the pen down, i.e., the Robot traces its movements with lines.
     */
    void penDown();

    /**
     * Sets the speed of the Robot.
     *
     * @param speed the speed specified as a number between 1 and 100.
     */
    void setSpeed(int speed);

    /**
     * Adds a {@link KeyboardAdapter} to the robot. It is possible to add more
     * than one KeyboardAdapter, each controlling a different Robot. If two
     * KeyboardAdapters controlling the same Robot are added, the last one added
     * <em>replaces</em> the first.
     *
     * @param adapter the KeyboardAdapter
     * @see KeyboardAdapter
     */
    void addKeyboardAdapter(KeyboardAdapter adapter);
}
