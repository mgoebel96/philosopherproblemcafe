package de.netempire.onlyone.classes;

public class Fork {

    boolean taken;

    void put() {
        // Fork is placed back on the table. -> status: not taken
        taken = false;
    }

    void get() {
        // Fork is taken from the table. -> status: taken
        taken = true;
    }
}