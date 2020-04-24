package de.netempire.puttingback.classes;

public class Fork {

    private boolean taken;
    private int id;

    public synchronized void put() {
        // Fork is placed back on the table. -> status: not taken
        taken = false;
    }

    public synchronized void get() {
        // Fork is taken from the table. -> status: taken
        taken = true;
    }

    public boolean isTaken() {
        return taken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}