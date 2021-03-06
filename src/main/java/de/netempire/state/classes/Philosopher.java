package de.netempire.state.classes;

import de.netempire.state.StatePhilosophersDesk;
import de.netempire.state.logger.MyLogger;

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
                StatePhilosophersDesk.setReport(name);
                // Philosopher is hungry
                state = "hungry";
                // taking right
                right.get();
                right.setId(id);
                // turn left (critical moment)
                sleep(100);
                // taking left
                left.get();
                left.setId(id);
                setLeftF(true);
                while(!hasLeftFork() && !hasRightFork()) {
                    sleep(100);
                }
                state = "eating";
                MyLogger.printOut(name + " hat zwei Gabel. Er kann essen.");
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

    public String getState() {
        return state;
    }
}