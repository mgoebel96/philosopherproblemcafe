package de.netempire.leftright.classes;

import de.netempire.leftright.LeftRightPhilosophersDesk;
import de.netempire.leftright.logger.MyLogger;

import static java.lang.Thread.sleep;

public class PhilosopherRightly implements Runnable {

    String name;
    int id;
    Fork right, left;
    int eatingTime;
    private volatile boolean exit = false;

    public PhilosopherRightly(String name, int id, Fork right, Fork left){
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
                MyLogger.log(name + " philosphiert.");
                sleep(1000);
                MyLogger.log(name + " hat Hunger.");
                LeftRightPhilosophersDesk.report = name;
                // Philosopher is hungry
                // taking right
                right.get();
                right.setId(id);
                // turn left (critical moment)
                sleep(100);
                // taking left
                left.get();
                left.setId(id);
                MyLogger.log(name + " hat zwei Gabeln. Er kann essen.");
                // holding two forks -> can eat now
                sleep(eatingTime);
            } catch (InterruptedException e) {
                MyLogger.log(e.getMessage());
            }
            right.setId(-1);
            left.setId(-1);
            right.put();
            left.put();
            i--;
        }
    }

    public void stop(){
        exit = true;
    }

    public void setEatingTime(int eatingTime) {
        this.eatingTime = eatingTime;
    }
}