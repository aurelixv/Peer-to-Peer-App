package com.trabalho1;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PrivateKey;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class UnicastServer extends Thread{

    private ServerSocket serverSocket;
    private PrivateKey masterPrivateKey;
    private BlockingQueue<Integer> messageQueue;
    private ClockSyncAlgorithm clockSyncAlgorithm;

    public UnicastServer(PrivateKey masterPrivateKey) {
        try {
            serverSocket = new ServerSocket(PeerInfo.port);
            this.masterPrivateKey = masterPrivateKey;
            this.messageQueue = new LinkedBlockingQueue<>();
            this.clockSyncAlgorithm = new ClockSyncAlgorithm(messageQueue);
            System.out.println("\nServidor unicast mestre inicializado.\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            clockSyncAlgorithm.start();
            while(true) {
                Socket client = serverSocket.accept();
                InetAddress addr = client.getInetAddress();
                int clientPort = client.getPort();
                System.out.println("\nConexao com IP: " + addr + " Porta: " + clientPort + "\n");
                new UnicastConnection(client, masterPrivateKey, messageQueue, clockSyncAlgorithm).start();
                clockSyncAlgorithm.incrementConnectedPeers();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
