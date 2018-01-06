import org.jointheleague.graphical.robot.KeyboardAdapter;

import java.awt.event.KeyEvent;

public class WasdKeyboardAdapter extends KeyboardAdapter {

    /*
    Note: There is a bug on MacOS that causes key pressed events on WASD to be omitted.
    See: https://bugs.openjdk.java.net/browse/JDK-8167263,
    and: http://mail.openjdk.java.net/pipermail/awt-dev/2017-December/013435.html
     */


    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                setMovingForward(true);
                break;
            case KeyEvent.VK_A:
                setTurningLeft(true);
                break;
            case KeyEvent.VK_S:
                setMovingBackward(true);
                break;
            case KeyEvent.VK_D:
                setTurningRight(true);
                break;
            default:
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                setMovingForward(false);
                break;
            case KeyEvent.VK_A:
                setTurningLeft(false);
                break;
            case KeyEvent.VK_S:
                setMovingBackward(false);
                break;
            case KeyEvent.VK_D:
                setTurningRight(false);
                break;
            default:
        }
    }

}
