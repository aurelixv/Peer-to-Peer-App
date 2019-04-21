package com.trabalho1;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.BlockingQueue;

public class ClockSyncAlgorithm extends Thread {

    private BlockingQueue<LocalTime> messageQueue;
    private int connectedPeers;
    private double adjustment;

    ClockSyncAlgorithm(BlockingQueue<LocalTime> messageQueue) {
        this.messageQueue = messageQueue;
        this.connectedPeers = 0;
        this.adjustment = 0.0;
    }

    public void run() {

        // Esperando escravos se conectarem
        while(connectedPeers < 1) {
            try {
                System.out.println("[ ClockSync ] Esperando escravos se conectarem...");
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while(true) {
            int cont = 0;
            double sum = 0;
            while (cont < getConnectedPeers()) {
                try {
                    LocalTime slave = messageQueue.take();
                    sum += Duration.between(slave, LocalTime.now()).getSeconds();
                    cont += 1;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            adjustment = sum / (cont + 2);
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (this) {
                this.notifyAll();
            }
        }
    }


    double getAdjustment() {
        return this.adjustment;
    }

    synchronized void incrementConnectedPeers() {
        this.connectedPeers += 1;
    }

    synchronized void decrementConnectedPeers() {
        if(connectedPeers > 0) {
            this.connectedPeers -= 1;
        }
    }

    private synchronized int getConnectedPeers() {
        return this.connectedPeers;
    }

}
