package org.jointheleague.graphical.robot;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.swing.JOptionPane;

public class RobotImage {
    private static final int IMG_WIDTH   = 100;
    private static final int IMG_HEIGHT  = 100;

    public static BufferedImage loadDefaultRobi()
    {
        return loadRobi("rob");
    }

    public static BufferedImage loadRobi(String s)
    {
        s += ".robi";
        BufferedImage img = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_ARGB);

        try (InputStream is = RobotImage.class.getResourceAsStream(s))
        {
            byte[] buf = new byte[8000];
            int len;
            while ((len = is.read(buf)) != -1) {
                for (int i = 0; i < len;) {
                    int[] pix = new int[5];
                    
                    for (int j = 0; j < pix.length; j++) {

                        byte[] num = new byte[4];
                        for (int k = 0; k < num.length; k++) {
                            num[k] = buf[i++];
                        }

                        pix[j] = ByteBuffer.wrap(num).getInt();
                    }
                    int x = pix[0];
                    int y = pix[1];
                    int r = pix[2] & 0xff;
                    int g = pix[3] & 0xff;
                    int b = pix[4] & 0xff;
                    int a = r == 220 && g == 220 && b == 220 ? 0 : 255;
                    
                    int rgb = new Color(r, g, b, a).getRGB();
                    img.setRGB(x, y, rgb);
                }

            }

        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "There was an error loading your file.");
            System.out.println("RobotImage: loadDefaultRobot");
            img = loadDefaultRobi();
        }
        return img;
    }

}
