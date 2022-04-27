import java.awt.*;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class Window extends Canvas {

    private static final long serialVersionUID = -7259108873705494293L;

    public Window (int x, int y, String title, Game game, MultiKeyListener listener){
        JFrame frame = new JFrame(title);

        /*
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        */
        frame.setPreferredSize(new Dimension(x, y));
        frame.setMaximumSize(new Dimension(x, y));
        frame.setMinimumSize(new Dimension(x, y));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(listener);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(game);
        frame.setVisible(true);
        game.start();
    }
}


