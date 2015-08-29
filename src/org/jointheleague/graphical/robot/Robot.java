package org.jointheleague.graphical.robot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.SwingUtilities;

public class Robot implements ActionListener {

    private static final int MAXI_IMAGE_SIZE = 100;
    private static final int MINI_IMAGE_SIZE = 25;
    private boolean          isVisible;
    private boolean          penDown;
    private boolean          isSparkling;

    private float            xPos;
    private float            yPos;
    private int              angle;
    private int              speed;

    private int              penSize;
    private Color            penColor;

    private Image            maxiImage;
    private Image            miniImage;
    private Image            image;
    private boolean          isMini          = false;

    private ArrayList<Line>  lines;
    private Line             currentLine;

    private RobotWindow      window;

    private boolean          goAhead;

    public Robot() {

        this(0, 0);
        moveTo(window.getWidth() / 2, window.getHeight() / 2);
    }

    public Robot(String fileName)
    {
        this(0, 0, fileName);
        moveTo(window.getWidth() / 2, window.getHeight() / 2);
    }

    public Robot(int x, int y)
    {
        this(x, y, "rob");
    }

    public Robot(int x, int y, String fileName)
    {
        xPos = x;
        yPos = y;
        angle = 0;
        speed = 1;

        penSize = 1;
        penColor = Color.BLACK;

        isVisible = true;
        penDown = false;
        isSparkling = false;

        image = RobotImage.loadRobi(fileName);
        maxiImage = image.getScaledInstance(MAXI_IMAGE_SIZE, MAXI_IMAGE_SIZE, Image.SCALE_SMOOTH);
        miniImage = image.getScaledInstance(MINI_IMAGE_SIZE, MINI_IMAGE_SIZE, Image.SCALE_SMOOTH);
        isMini = false;
        image = maxiImage;

        lines = new ArrayList<Line>();

        window = RobotWindow.getInstance();
        window.addRobot(this);
    }

    public void draw(Graphics2D g)
    {
        for (int i = 0; i < lines.size(); i++)
        {
            Line l = lines.get(i);
            l.draw(g);
        }

        if (penDown && currentLine != null)// draws under robot
        {
            currentLine.draw(g);
        }

        if (isVisible)
        {
            AffineTransform cached = g.getTransform();
            g.translate(xPos, yPos);
            g.rotate(Math.toRadians(angle));
            int offset = -(isMini ? MINI_IMAGE_SIZE : MAXI_IMAGE_SIZE) / 2;
            g.drawImage(image, offset, offset, null);
            g.setTransform(cached);
        }

        if (penDown)// draws over robot
        {
            g.setColor(penColor);
            int pSize = penSize + 3;
            int newX = (int) (xPos - (pSize / 2));
            int newY = (int) (yPos - (pSize / 2));
            g.fillOval(newX, newY, pSize, pSize);
        }

        if (isSparkling)
        {
            Random r = new Random();
            int xDot = r.nextInt(100) - 50;
            int yDot = r.nextInt(100) - 50;
            g.setColor(Color.WHITE);
            g.fillRect((int) (xPos + xDot), (int) (yPos + yDot), 5, 5);
        }
    }

    public void changeRobot(String fileName)
    {
        image = RobotImage.loadRobi(fileName);
        maxiImage = image.getScaledInstance(MAXI_IMAGE_SIZE, MAXI_IMAGE_SIZE, Image.SCALE_SMOOTH);
        miniImage = image.getScaledInstance(MINI_IMAGE_SIZE, MINI_IMAGE_SIZE, Image.SCALE_SMOOTH);
        image = isMini ? miniImage : maxiImage;
        window.repaint();
    }

    public void setPenColor(Color c)
    {
        penColor = c;
    }

    public void setPenColor(int r, int g, int b)
    {
        r = Math.min(Math.max(0, r), 255);
        g = Math.min(Math.max(0, g), 255);
        b = Math.min(Math.max(0, b), 255);

        penColor = new Color(r, g, b);
    }

    public void setWindowColor(Color c)
    {
        window.setWinColor(c);
    }

    public void setWindowColor(int r, int g, int b)
    {
        r = Math.min(Math.max(0, r), 255);
        g = Math.min(Math.max(0, g), 255);
        b = Math.min(Math.max(0, b), 255);

        window.setWinColor(new Color(r, g, b));
    }

    public void setPenWidth(int size)
    {
        penSize = Math.min(Math.max(1, size), 10);
    }

    private void addLine(final Line line) {
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                lines.add(line);
                
            }
        });
    }
    
    public void clear()
    {
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                lines.clear();
                window.repaint();
            }
            
        });
    }

    public void miniturize()
    {
        image = miniImage;
        isMini = true;
        window.repaint();
    }

    public void expand()
    {
        image = maxiImage;
        isMini = false;
        window.repaint();
    }

    public void sparkle()
    {
        isSparkling = true;
    }

    public void unSparkle()
    {
        isSparkling = false;
    }

    public void show()
    {
        isVisible = true;
        window.repaint();
    }

    public void hide()
    {
        isVisible = false;
        window.repaint();
    }

    public synchronized void move(int distance)
    {
        float sx = xPos;
        float sy = yPos;
        int distanceMoved = 0;
        int sgn = distance < 0 ? -1 : 1;

        double r_angle = Math.toRadians(angle);
        double cos = Math.cos(r_angle);
        double sin = Math.sin(r_angle);

        try
        {
            while (sgn * (distanceMoved - distance) < 0) {
                goAhead = false;
                while (!goAhead)
                {
                    wait(); // wait for tick
                }
                distanceMoved += sgn * speed;
                if (sgn * (distanceMoved - distance) > 0)
                {
                    distanceMoved = distance;
                }
                xPos = (float) (sx + distanceMoved * sin);
                yPos = (float) (sy - distanceMoved * cos);
                if (penDown)
                {
                    currentLine = new Line((int) sx, (int) sy, (int) xPos, (int) yPos, penSize,
                                           penColor);
                }
                window.repaint();
            }
        } catch (InterruptedException e)
        {
        }
        if (currentLine != null)
        {
            addLine(currentLine);
            currentLine = null;
        }
        window.repaint();
    }

    public synchronized void turn(int degrees)
    {
        int degreesTurned = 0;
        int sgn = degrees < 0 ? -1 : 1;
    
        int angle0 = angle;
        try
        {
            while (sgn * (degreesTurned - degrees) < 0)
            {
                goAhead = false;
                while (!goAhead)
                {
                    wait(); // wait for tick
                }
                degreesTurned += sgn * speed;
                if (sgn * (degreesTurned - degrees) > 0)
                {
                    degreesTurned = degrees;
                }
                angle = angle0 + degreesTurned;
                window.repaint();
            }
        } catch (InterruptedException e)
        {
        }
        window.repaint();
    }

    public void moveTo(int x, int y)
    {
        xPos = x;
        yPos = y;
        window.repaint();
    }

    public void setX(int newX)
    {

        xPos = newX;
        window.repaint();

    }

    public void setY(int newY)
    {
        yPos = newY;
        window.repaint();
    }

    public void penUp()
    {
        penDown = false;
    }

    public void penDown()
    {
        penDown = true;
    }

    public void setSpeed(int s)
    {
        speed = Math.min(Math.max(1, s), 10);
    }

    public void setAngle(int a)
    {
        angle = a;
    }

    public void setRandomPenColor()
    {
        Random random = new Random();
        this.penColor = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    public void actionPerformed(ActionEvent arg0)
    {
        if (!goAhead)
        {
            synchronized (this)
            {
                goAhead = true;
                notifyAll();
            }
        } else {
            window.repaint();
        }
    }
}
