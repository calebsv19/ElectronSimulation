import java.awt.*;
import java.util.Vector;

public class Charge {
    private int size;
    private double x;
    private double y;
    private double xVel;
    private double yVel;
    private double xAcc;
    private double yAcc;
    private boolean charge; // true = +charge & false = -charge
    private boolean locked;

    public static final int MAX_VEL = 1;


    public Charge(int x, int y, int size){
        this(x, y, size, true, false);
    }

    public Charge(int x, int y, int size, boolean charge){
        this(x, y, size, charge, false);
    }

    public Charge(int x, int y, int size, boolean charge, boolean locked){
        this.x = x;
        this.y = y;
        this.charge = charge;
        this.locked = locked;
        this.size = size;


    }

    public void render(Graphics g, int width, int height){
        int subtract = 0;
        if (locked){
            subtract = 70;
        }

        if (!charge){
            g.setColor(new Color(255 - subtract, 85 - subtract, 100 - subtract));
        } else {
            g.setColor(new Color(85 - subtract, 100 - subtract, 255 - subtract));
        }
        g.fillOval((int) x - size / 2, (int) y - size / 2, size, size);
    }


    public void clamp(int width, int height){
        if (x < size ) {
            xVel = -xVel;
            x = size;
        } else if (x > width - size) {
            xVel = -xVel;
            x = width - size;
        } else if (y < size) {
            yVel = -yVel;
            y = size;
        } else if (y > height - size) {
            yVel = -yVel;
            y = height - size;
        }

        if (Math.abs(xVel) > MAX_VEL){
            xVel = xVel / Math.abs(xVel) * MAX_VEL;
        } else if (Math.abs(yVel) > MAX_VEL){
            yVel = yVel / Math.abs(yVel) * MAX_VEL;
        }
    }


    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }

    public void setXVel(double xVel){
        this.xVel = xVel;
    }

    public void setYVel(double yVel){
        this.yVel = yVel;
    }

    public void setXAcc(double xAcc){
        this.xAcc = xAcc;
    }

    public void setYAcc(double yAcc){
        this.yAcc = yAcc;
    }

    public void setLocked(boolean locked){
        this.locked = locked;
    }


    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public double getXVel(){
        return xVel;
    }

    public double getYVel(){
        return yVel;
    }

    public double getXAcc(){
        return xAcc;
    }

    public double getYAcc(){
        return yAcc;
    }

    public boolean getLocked(){
        return locked;
    }

    public boolean getCharge(){
        return charge;
    }

    public double[] getDistance(Charge charge){
        return new double[]{this.x - charge.getX(), this.y - charge.getY()};
    }
}

