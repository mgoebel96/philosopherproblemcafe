package de.netempire.puttingback.classes;

import de.netempire.puttingback.PutBackPhilosophersDesk;
import de.netempire.puttingback.logger.MyLogger;

import static java.lang.Thread.sleep;

public class Philosopher implements Runnable {

    String name;
    int id;
    Fork right, left;
    boolean rightF, leftF;
    String state;
    private volatile boolean exit = false;
    private int eatingTime;

    public Philosopher(String name, int id, Fork right, Fork left){
        this.name = name;
        this.id = id;
        this.right = right;
        this.left = left;
    }

    public void run() {
        int i = 30;
        while( i > 0 && !exit) {
            try {
                // Philosopher is thinking
                MyLogger.printOut (name + " philosphiert.");
                state = "wait";
                sleep(1000);
                MyLogger.printOut (name + " hat Hunger.");
                PutBackPhilosophersDesk.setReport(name);
                // Philosopher is hungry
                state = "hungry";
                while(!hasLeftFork() && !hasRightFork()) {
                    // taking right
                    if(!right.isTaken()) {
                        right.get();
                        right.setId(id);
                        setRightF(true);
                    }
                    // turn left (critical moment)
                    sleep(100);
                    // taking left
                    if(!left.isTaken()) {
                        left.get();
                        left.setId(id);
                        setLeftF(true);
                    } else {
                        if(right.getId()==id) {
                            right.put();
                        }
                    }
                    sleep(100);
                }
                state = "eating";
                MyLogger.printOut(name + " hat zwei Gabeln. Er kann essen.");
                // holding two forks -> can eat now
                sleep(eatingTime);
            } catch (InterruptedException e) {
                MyLogger.printOut (e.getMessage());
            }
            right.setId(-1);
            left.setId(-1);
            right.put();
            left.put();
            i--;
        }
    }

    public boolean hasRightFork() {
        return rightF;
    }

    public void setRightF(boolean rightF) {
        this.rightF = rightF;
    }

    public boolean hasLeftFork() {
        return leftF;
    }

    public void setLeftF(boolean leftF) {
        this.leftF = leftF;
    }

    public void stop(){
        exit = true;
    }

    public void setEatingTime(int eatingTime) {
        this.eatingTime = eatingTime;
    }
}