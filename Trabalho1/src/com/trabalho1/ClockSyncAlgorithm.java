package com.trabalho1;

import java.util.concurrent.BlockingQueue;

public class ClockSyncAlgorithm extends Thread {

    private BlockingQueue<Integer> messageQueue;
    private int connectedPeers;
    public int adjustment;

    public ClockSyncAlgorithm(BlockingQueue<Integer> messageQueue) {
        this.messageQueue = messageQueue;
        this.connectedPeers = 0;
        this.adjustment = 0;
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
            int sum = 0;
            while (cont < connectedPeers) {
                try {
                    sum += messageQueue.take();
                    cont += 1;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            adjustment = sum / connectedPeers;
            synchronized (this) {
                this.notifyAll();
            }
        }
    }

    public void incrementConnectedPeers() {
        this.connectedPeers += 1;
    }

    public int getAdjustment() {
        return this.adjustment;
    }

    public void decrementConnectedPeers() {
        if(connectedPeers > 0) {
            this.connectedPeers -= 1;
        }
    }
}
