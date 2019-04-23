package com.trabalho1;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.BlockingQueue;

public class ClockSyncAlgorithm extends Thread {

    private BlockingQueue<LocalTime> messageQueue;
    private int connectedPeers;
    private long meanTime;
    private long adjustment;

    ClockSyncAlgorithm(BlockingQueue<LocalTime> messageQueue) {
        this.messageQueue = messageQueue;
        this.connectedPeers = 0;
        this.meanTime = 0;
        this.adjustment = 0;
    }

    public void run() {

        // Esperando escravos se conectarem
        while(getConnectedPeers() < 1) {
            try {
                System.out.println("[ ClockSync ] Esperando escravos se conectarem...");
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while(getConnectedPeers() > 0) {
            int cont = 0;
            long sum = 0;
            while (cont < getConnectedPeers()) {
                try {
                    LocalTime slave = messageQueue.take();
                    sum += slave.getNano();
                    cont += 1;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            LocalTime masterTime = LocalTime.now().plusNanos(adjustment);
            sum += masterTime.getNano();
            meanTime = sum / (cont + 2);
            adjustment = meanTime - masterTime.getNano();
            System.out.println("[ ClockSync ] Ajuste do mestre: " + adjustment);
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


    long getMeanTime() {
        return this.meanTime;
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
