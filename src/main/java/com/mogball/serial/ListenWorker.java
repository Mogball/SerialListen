package com.mogball.serial;

import java.util.Timer;
import java.util.TimerTask;

public class ListenWorker {

    private static class Task extends TimerTask {

        @Override
        public void run() {
        }
    }

    public static void run() {
        Timer t = new Timer();
        t.scheduleAtFixedRate(new Task(), 0, 4000);
    }

}
