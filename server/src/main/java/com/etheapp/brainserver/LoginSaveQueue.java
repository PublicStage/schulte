package com.etheapp.brainserver;

import java.util.concurrent.LinkedBlockingQueue;

public class LoginSaveQueue extends Thread {
    private final String STOP = "stop";
    public static LoginSaveQueue instance = new LoginSaveQueue();

    private LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();

    public static void addItem(String uuid) {
        instance.queue.add(uuid);
    }

    @Override
    public void run() {
        Log.info("LoginSaveQueue thread started");
        String uuid;
        try {
            while (!((uuid = queue.take()).equals(STOP))) {
                //DBService.getInstance().writeLogin(uuid);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.info("LoginSaveQueue thread stopped");

    }


    void waitFinish() {
        queue.add(STOP);
        try {
            join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        instance = null;
    }


}
