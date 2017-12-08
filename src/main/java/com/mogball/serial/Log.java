package com.mogball.serial;

public class Log {

    public static void info(String message) {
        System.out.println(message);
    }

    public static void error(Exception e) {
        e.printStackTrace();
    }

}
