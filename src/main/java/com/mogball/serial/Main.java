package com.mogball.serial;

public class Main {

    public static void main(String[] args) {
        ListenWorker.run();
        SerialListener listener = new SerialListener();
        listener.begin();
    }
}
