package com.PeertoPeerApp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class UnicastServer extends Thread{

    private ServerSocket serverSocket;
    private KeyPair keyPair;
    private BlockingQueue<LocalTime> messageQueue;
    private ClockSyncAlgorithm clockSyncAlgorithm;

    UnicastServer(KeyPair keyPair) {
        try {
            serverSocket = new ServerSocket(PeerInfo.port);
            this.keyPair = keyPair;
            this.messageQueue = new LinkedBlockingQueue<>();
            this.clockSyncAlgorithm = new ClockSyncAlgorithm(messageQueue);
            System.out.println("\n[ UnicastServer ] Servidor unicast mestre inicializado.\n");
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
                System.out.println("\n[ UnicastServer ] Conexao com IP: " + addr + " Porta: " + clientPort + "\n");
                new UnicastConnection(client, keyPair, messageQueue, clockSyncAlgorithm).start();
                clockSyncAlgorithm.incrementConnectedPeers();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
