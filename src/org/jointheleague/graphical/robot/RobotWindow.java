package org.jointheleague.graphical.robot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class RobotWindow extends JPanel implements Runnable {
    /**
	 * 
	 */
    private static final long  serialVersionUID     = 1L;
    private static final Color DEFAULT_WINDOW_COLOR = new Color(220, 220, 220);
    // public static final int WIDTH = 1780;
    // public static final int HEIGHT = 1024;
    private static int         MARGIN               = 10;
    private static RobotWindow INSTANCE;

    private Color              winColor;

    private ArrayList<Robot>   robotList;
    private Timer              ticker;

    private BufferedImage      leagueLogo;

    private RobotWindow(Color c)
    {
        winColor = c;
        robotList = new ArrayList<Robot>();
    }

    public void run() {
        JFrame frame = new JFrame();
        frame.add(this);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);

        try {
            leagueLogo = ImageIO.read(this.getClass().getResourceAsStream("league_logo.png"));
        } catch (IOException e)
        {
            System.err.println("Cannot load background image.");
        }
        frame.setVisible(true);
    }

    public void setWinColor(Color c)
    {
        winColor = c;
        repaint();
    }

    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(winColor);
        g2.fillRect(0, 0, getWidth(), getHeight());
        int imgX = getWidth() - leagueLogo.getWidth() - MARGIN;
        int imgY = MARGIN;
        g2.drawImage(leagueLogo, imgX, imgY, null);

        for (Robot r : robotList)
        {
            r.draw(g2);
        }
    }

    public void addRobot(final Robot r)
    {
        SwingUtilities.invokeLater(new Runnable() {
            
            public void run() {
                robotList.add(r);
                if (ticker == null) {
                    ticker = new Timer(1000 / 30, r);
                    ticker.start();
                } else {
                    ticker.addActionListener(r);
                }
            }
        });
    }

    public static synchronized RobotWindow getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RobotWindow(DEFAULT_WINDOW_COLOR);
            SwingUtilities.invokeLater(INSTANCE);
        }
        return INSTANCE;
    }

}
