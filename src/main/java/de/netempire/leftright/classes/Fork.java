package de.netempire.leftright.classes;

public class Fork {

    boolean taken;
    int id;

    public void put() {
        // Fork is placed back on the table. -> status: not taken
        taken = false;
    }

    public void get() {
        // Fork is taken from the table. -> status: taken
        taken = true;
    }

    public void setId(int id) {
        this.id = id;
    }

}