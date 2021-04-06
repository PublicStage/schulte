package com.etheapp.brainserver;

import com.bear.brain.SendResult;
import com.google.gson.Gson;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class AddResultQueue extends Thread {
    public static AddResultQueue instance = new AddResultQueue();

    private AtomicBoolean work = new AtomicBoolean(true);
    private LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private volatile boolean hasTasks = false;

    public static void addItem(String request) {
        instance.queue.add(request);
        instance.hasTasks = true;
    }

    @Override
    public void run() {
        while (work.get()) {
            try {
                String request = queue.take();
                System.out.println("ADD " + request);
                SendResult result = new Gson().fromJson(request, SendResult.class);
                //DBService.writeResult(result);
                //Ranger.instance.addResult(result.uuid, result.result);
                hasTasks = queue.size() > 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    void waitFinish() {
        work.set(false);
        try {
            join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        instance = null;
    }

    public void finishTasks() {
        while (hasTasks) {
            Thread.yield();
        }
    }


}
