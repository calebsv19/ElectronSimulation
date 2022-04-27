import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;

public class Handler {
    private List<Charge> charges; // holds charges
    private List<Queue<int[]>> pastCharge;
    private int width;                 // width of frame
    private int height;                // height of frame
    private boolean going;             // state of game, aka running or not
    private int numLocked;             // num of charges locked

    public static final int NUM = 10, TRAIL_NUM = 80, MARGIN = 0, SIDE = 100, SIZE = 20; // num balls

    public static final double CHARGE_VAL = 1.6E-9, K = 8.99E9 ; // physics values
    public static final double RATIO = 7E4;      // modifiable ratio to change pixel distance value


    public Handler(int width, int height){
        this.width = width;
        this.height = height;
        going = true;

        Random r  = new Random();
        charges = new ArrayList<>();
        pastCharge = new ArrayList<>();

        // used to create a certain number of random charges in random location
        randomCharges(r);

        /*
        // sets charged you set, can be used for easy problems
        Charge[] charges = new Charge[]{
                new Charge(100, 100, SIZE, true),
                    new Charge(700, 200, SIZE, false),
                        new Charge(400, 600, SIZE, true),
                            new Charge(600, 600, SIZE, true),
                                new Charge(900, 300, SIZE, false),
                                    new Charge(1200, 300, SIZE, true)};
        selectCharges(charges);

         */

    }


    public void tick(){
        if (going) {
            for (int i = 0; i < charges.size(); i++) {
                charges.get(i).setXAcc(0);
                charges.get(i).setYAcc(0);

                // for each other charge calc force
                for (int j = 0; j < charges.size(); j++) {
                    if (j != i) {
                        double[] distance = charges.get(i).getDistance(charges.get(j));
                        double degree = Math.abs(Math.atan(distance[1] / distance[0]));
                        double dist = (distance[0] * distance[0] + distance[1] * distance[1]) / 10000;

                        // creates charge understanding for each charge pair
                        int charge1;
                        if (charges.get(i).getCharge()) {
                            charge1 = 1;
                        } else {
                            charge1 = -1;
                        }
                        int charge2;
                        if (charges.get(j).getCharge()) {
                            charge2 = 1;
                        } else {
                            charge2 = -1;
                        }

                        // calculates force felt by pair
                        double force = K * CHARGE_VAL * CHARGE_VAL * charge1 * charge2 / dist;


                        // multiplier for judging force direction based on pair connection direction
                        int xMult = 1;
                        if (distance[0] != 0) {
                            xMult = (int) (distance[0] / Math.abs(distance[0]));
                        }
                        int yMult = 1;
                        if (distance[1] != 0) {
                            yMult = (int) (distance[1] / Math.abs(distance[1]));
                        }

                        double xAccel = charges.get(i).getXAcc() + (force * Math.cos(degree) * xMult);
                        double yAccel = charges.get(i).getYAcc() + (force * Math.sin(degree) * yMult);
                        charges.get(i).setXAcc(xAccel);
                        charges.get(i).setYAcc(yAccel);
                    }
                }
            }


            // for each charge change velocity based on current force felt on the charge
            for (int i = 0; i < charges.size(); i++) {
                double xVelo = charges.get(i).getXVel() + charges.get(i).getXAcc();
                charges.get(i).setXVel(xVelo);
                double yVelo = charges.get(i).getYVel() + charges.get(i).getYAcc();
                charges.get(i).setYVel(yVelo);

                if (!charges.get(i).getLocked()) {
                    pastCharge.get(i).add(new int[]{(int) charges.get(i).getX(), (int) charges.get(i).getY()});
                    if (pastCharge.get(i).size() > TRAIL_NUM){
                        pastCharge.get(i).remove();
                    }
                    charges.get(i).setX(charges.get(i).getX() + charges.get(i).getXVel() * RATIO);
                    charges.get(i).setY(charges.get(i).getY() + charges.get(i).getYVel() * RATIO);
                }
            }
        }
    }

    public void render(Graphics g) {
        // draws background with possibility for marker lines
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        for (int w = -SIDE / 2; w < width; w +=SIDE){
            for (int h = -SIDE / 2; h < height; h +=SIDE){
                g.fillRect(MARGIN + w,MARGIN + h,SIDE - 2*MARGIN,SIDE - 2*MARGIN);
            }
        }

        // draws charges trail
        g.setColor(new Color(200, 200,200));
        for (int i = 0; i < pastCharge.size(); i++){
            Queue<int[]> temp = pastCharge.get(i);
            Iterator<int[]> itr = temp.iterator();

            int num = 1;
            int[] color;
            if (charges.get(i).getCharge()){
                color = new int[]{85, 100, 255};
            } else {
                color = new int[]{255, 85, 100};
            }
            while (itr.hasNext()) {
                int[] point = itr.next();
                double ratio = 1.0 * (TRAIL_NUM - num) / TRAIL_NUM;
                g.setColor(new Color(color[0] - (int) (color[0] * ratio),
                                        color[1] - (int) (color[1] * ratio),
                                            color[2] - (int) (color[2] * ratio)));
                int width = (int) ((float) (SIZE) * num / TRAIL_NUM);
                g.fillOval( point[0] - width/2, point[1]  - width/2, // figure this shit oput
                        width, width);
                num++;
            }
        }

        // draws charge and charge color
        for (int i = 0; i < charges.size(); i++){
            charges.get(i).clamp(width, height);
            charges.get(i).render(g, width, height);
        }
    }


    private void randomCharges(Random r){
        for (int i = 0; i < NUM; i++){
            int xCoord = r.nextInt(width - SIZE);
            int yCoord = r.nextInt(height - SIZE);
            Queue<int[]> path = new LinkedList<>();
            path.add(new int[]{(int) xCoord, (int) yCoord});
            charges.add(new Charge(xCoord, yCoord, SIZE, r.nextBoolean()));
            pastCharge.add(path);
        }
    }

    private void selectCharges(Charge[] charges){
        for (int i = 0; i < charges.length; i++){
            int xCoord = (int) charges[i].getX();
            int yCoord = (int) charges[i].getY();
            Queue<int[]> path = new LinkedList<>();
            path.add(new int[]{(int) xCoord, (int) yCoord});
            this.charges.add(new Charge(xCoord, yCoord, SIZE, charges[i].getCharge()));
            pastCharge.add(path);
        }
    }


    public void setGoing(){
        going = !going;
    }

    public void lockAll(boolean lock){
        for (int i = 0; i < charges.size(); i++){
            charges.get(i).setLocked(lock);
        }

        if (lock){
            numLocked = charges.size();
        } else {
            numLocked = 0;
        }
    }

    public void lockAdd(int num){
        if (!(numLocked + num > charges.size())) {
            for (int i = numLocked; i < numLocked + num; i++) {
                charges.get(i).setLocked(true);
            }
            numLocked += num;
        }
    }

    public void lockSubtract(int num){
        if (!(numLocked - num < 0)) {
            for (int i = numLocked; i >= numLocked - num; i--) {
                charges.get(i - 1).setLocked(false);
            }
            numLocked -= num;
        }
    }

    public int getSize(){
        return charges.size();
    }

}