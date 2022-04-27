import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MultiKeyListener implements KeyListener {
    // Set of currently pressed keys
    private final Set<Integer> pressedKeys = new HashSet<>();
    private Handler handler;

    private static final int ADD = 10, LOCK_AMOUNT = 20;
    private static final double ROTATION = .02;

    public MultiKeyListener(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public synchronized void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
        Point offset = new Point();
        if (!pressedKeys.isEmpty()) {
            for (Iterator<Integer> it = pressedKeys.iterator(); it.hasNext();){
                switch (it.next()) {
                    case KeyEvent.VK_Q:
                        handler.lockAll(true);
                        break;
                    case KeyEvent.VK_A:
                        handler.lockAll(false);
                        break;
                    case KeyEvent.VK_W:
                        handler.lockAdd(LOCK_AMOUNT);
                        break;
                    case KeyEvent.VK_S:
                        handler.lockSubtract(LOCK_AMOUNT);
                        break;


                    case KeyEvent.VK_P:
                        handler.setGoing();
                        break;
                }
            }

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
}
