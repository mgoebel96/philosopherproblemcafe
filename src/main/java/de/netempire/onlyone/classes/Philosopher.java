package de.netempire.onlyone.classes;

import de.netempire.onlyone.OnlyOnePhilosophersDesk;
import de.netempire.onlyone.logger.MyLogger;

import static java.lang.Thread.sleep;

public class Philosopher implements Runnable {

    String name;
    Fork right, left;
    private volatile boolean exit = false;
    private int eatingTime;


    public Philosopher(String name, Fork right, Fork left){
        this.name = name;
        this.right = right;
        this.left = left;
    }

    public void run() {
        int i = 30;
        while( i > 0 && !exit) {
            try {
                // Philosopher is thinking
                MyLogger.printOut (name + " philosphiert.");
                sleep(1000);
                MyLogger.printOut (name + " hat Hunger.");
                // Philosopher is hungry
                OnlyOnePhilosophersDesk.eatingPhilosopher.acquire();
                // taking right
                right.get();
                // turn left (critical moment)
                sleep(1000);
                // taking left
                left.get();
                MyLogger.printOut (name + " hat zwei Gabeln. Er kann essen.");
                // holding two forks -> can eat now
                sleep(eatingTime);
            } catch (InterruptedException e) {
                MyLogger.printOut (e.getMessage());
            }
            OnlyOnePhilosophersDesk.eatingPhilosopher.release();
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