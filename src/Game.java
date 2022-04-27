
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable{
    private Handler handler;
    private final MultiKeyListener listener;
    /**
     *
     */
    private static final long serialVersionUID = -2599171697168126036L;

    private Thread thread;
    private boolean running = false;


    public static final int X_DIMENSION = 1400, Y_DIMENSION = 800,  TICKS = 60, ADD = 6;
    public static final double ROTATION = Math.PI / 80;

    public static void main(String[] args){
        new Game();
    }

    public Game(){
        handler = new Handler(X_DIMENSION, Y_DIMENSION);
        listener = new MultiKeyListener(handler);
        new Window(X_DIMENSION, Y_DIMENSION, "Polar Coord", this, listener);
    }

    public synchronized void start(){
        thread = new Thread(this);
        thread.start();
        running = true;
        run();
    }

    public synchronized void stop(){
        try {
            thread.join();
            running = false;
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void run(){
        long lastTime = System.nanoTime();
        double tickCount = TICKS;
        double ns = 1000000000 / tickCount;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        while(running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1){
                tick();
                delta--;
            }
            if (running)
                render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                System.out.println("Fps: " + frames);
                frames = 0;
            }
        }
        stop();
    }

    private void tick(){
        handler.tick();
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();


        handler.render(g);

        g.dispose();
        bs.show();
    }

}

