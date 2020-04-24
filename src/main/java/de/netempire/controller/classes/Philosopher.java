package de.netempire.controller.classes;

import de.netempire.controller.ControllerPhilosophersDesk;
import de.netempire.controller.logger.MyLogger;

import static java.lang.Thread.sleep;

public class Philosopher implements Runnable {

    String name;
    int id;
    Fork right, left;
    private volatile boolean exit = false;
    int eatingTime;

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
                MyLogger.log(name + " philosphiert.");
                sleep(1000);
                MyLogger.log(name + " hat Hunger.");
                ControllerPhilosophersDesk.report = name;
                // Philosopher is hungry
                ControllerPhilosophersDesk.satedPhilosophers.acquire();
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
            ControllerPhilosophersDesk.satedPhilosophers.release();
            i--;
        }
    }

    public void stop(){
        exit = true;
    }

    public void setEatingTime(int eatingTime) {
        this.eatingTime = eatingTime;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Fork getRight() {
        return right;
    }

    public Fork getLeft() {
        return left;
    }
}