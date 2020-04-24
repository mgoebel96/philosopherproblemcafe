package de.netempire.leftright.logger;

public class MyLogger {

    private MyLogger() {
        throw new IllegalStateException("Logger class");
    }

    public static synchronized void log(String printText){
        System.out.println(printText);
    }
}