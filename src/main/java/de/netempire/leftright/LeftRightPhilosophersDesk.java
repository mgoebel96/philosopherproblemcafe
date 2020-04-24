package de.netempire.leftright;

import de.netempire.leftright.classes.Fork;
import de.netempire.leftright.classes.PhilosopherLeftly;
import de.netempire.leftright.classes.PhilosopherRightly;
import de.netempire.leftright.logger.ResultLogger;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LeftRightPhilosophersDesk {

    public static String report;
    static Fork fork1 = new Fork();
    static Fork fork2 = new Fork();
    static Fork fork3 = new Fork();
    static Fork fork4 = new Fork();
    static Fork fork5 = new Fork();
    static PhilosopherRightly platon = new PhilosopherRightly("Platon", 1, fork1, fork2);
    static PhilosopherLeftly aristoteles = new PhilosopherLeftly("Aristoteles",2, fork2, fork3);
    static PhilosopherLeftly herder = new PhilosopherLeftly("Herder", 3, fork3, fork4);
    static PhilosopherLeftly fichte = new PhilosopherLeftly("Fichte", 4, fork4, fork5);
    static PhilosopherLeftly schlegel = new PhilosopherLeftly("Schlegel", 5, fork5, fork1);
    static Thread platonThread = new Thread(platon);
    static Thread aristotelesThread = new Thread(aristoteles);
    static Thread schlegelThread = new Thread(schlegel);
    static Thread fichteThread = new Thread(fichte);
    static Thread herderThread = new Thread(herder);
    static Date start = Calendar.getInstance().getTime();
    static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    static Runnable controller;

    public static void main(String[] args) {
        LeftRightPhilosophersDesk.startProcess();
    }

    private static void startProcess() {
        initialize();
        start();
    }

    public static void initialize(){
        platon.setEatingTime(750);
        aristoteles.setEatingTime(1000);
        herder.setEatingTime(300);
        platon.setEatingTime(1500);
        platon.setEatingTime(500);

        controller = () -> {
            if(!platonThread.isAlive() && !herderThread.isAlive() && !aristotelesThread.isAlive() && !fichteThread.isAlive() && !schlegelThread.isAlive()){
                platon.stop();
                herder.stop();
                platon.stop();
                aristoteles.stop();
                schlegel.stop();
                executor.shutdown();
                System.out.println("Der Abend wird beendet.");
                ResultLogger.log("Die Philosophen haben " + computeDuration(start, Calendar.getInstance().getTime()) + " Sekunden zusammen am Tisch gesessen.");
            }
        };
    }

    public static void start(){
        platonThread.start();
        aristotelesThread.start();
        schlegelThread.start();
        fichteThread.start();
        herderThread.start();
        executor.scheduleAtFixedRate(controller, 0, 4, TimeUnit.SECONDS);
    }

    public static int computeDuration(Date to, Date from) {
        long difference = from.getTime() - to.getTime();
        return (int) (difference/1000);
    }
}