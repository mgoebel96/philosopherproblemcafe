package de.netempire.controller;

import de.netempire.controller.classes.Fork;
import de.netempire.controller.classes.Philosopher;
import de.netempire.controller.logger.MyLogger;
import de.netempire.controller.logger.ResultLogger;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class ControllerPhilosophersDesk {

    public static final Semaphore satedPhilosophers = new Semaphore(5, true);
    public static String report = "";
    static Fork fork1 = new Fork();
    static Fork fork2 = new Fork();
    static Fork fork3 = new Fork();
    static Fork fork4 = new Fork();
    static Fork fork5 = new Fork();
    static Philosopher platon = new Philosopher("Platon",1, fork1, fork2);
    static Philosopher aristoteles = new Philosopher("Aristoteles",2,fork2, fork3);
    static Philosopher herder = new Philosopher("Herder", 3,fork3, fork4);
    static Philosopher fichte = new Philosopher("Fichte", 4,fork4, fork5);
    static Philosopher schlegel = new Philosopher("Schlegel", 5,fork5, fork1);
    static Thread platonThread = new Thread(platon);
    static Thread aristotelesThread = new Thread(aristoteles);
    static Thread schlegelThread = new Thread(schlegel);
    static Thread fichteThread = new Thread(fichte);
    static Thread herderThread = new Thread(herder);
    static Date start = Calendar.getInstance().getTime();
    static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    static Runnable controller;
    static Philosopher[] philosophers;

    public static void main(String[] args) {
        ControllerPhilosophersDesk.startProcess();
    }

    private static void startProcess() {
        initializePhilosophers();
        initializeController();
        start();
    }

    public static void initializePhilosophers() {
        platon.setEatingTime(750);
        aristoteles.setEatingTime(1000);
        herder.setEatingTime(300);
        fichte.setEatingTime(1500);
        schlegel.setEatingTime(500);
        philosophers = new Philosopher[]{platon, aristoteles, herder, fichte, schlegel};
    }

    public static void initializeController() {
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
            if (ControllerPhilosophersDesk.satedPhilosophers.availablePermits() != 0) return;
            System.out.println("Es haben alle Philosophen hunger!");
            try {
                Optional<Philosopher> lastPhiloso = Arrays.stream(philosophers).filter(p -> p.getName().equals(report)).findFirst();
                int idLastPhiloso = lastPhiloso.map(philosopher -> philosopher.getId() - 1).orElse(0);
                MyLogger.log(report + " legt seine Gabeln wieder auf den Tisch.");
                if(Arrays.asList(philosophers).get(idLastPhiloso).getRight().isTaken()){
                    Arrays.asList(philosophers).get(idLastPhiloso).getRight().put();
                    while (ControllerPhilosophersDesk.satedPhilosophers.availablePermits() < 1) sleep(100);
                    while (Arrays.asList(philosophers).get(idLastPhiloso).getRight().getId() != -1) sleep(100);
                    Arrays.asList(philosophers).get(idLastPhiloso).getRight().get();
                } else {
                    Arrays.asList(philosophers).get(idLastPhiloso).getLeft().put();
                    while (ControllerPhilosophersDesk.satedPhilosophers.availablePermits() < 1) sleep(100);
                    while (Arrays.asList(philosophers).get(idLastPhiloso).getLeft().getId() != -1) sleep(100);
                    Arrays.asList(philosophers).get(idLastPhiloso).getLeft().get();
                }
            } catch (Exception e) {
                e.printStackTrace();
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