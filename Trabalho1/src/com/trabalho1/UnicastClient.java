package com.trabalho1;

import java.io.IOException;
import java.net.Socket;

public class UnicastClient extends Thread {

    private Socket client;
    private String hostName;
    private int port;
    private boolean kill;

    public UnicastClient(int port) {
        this.hostName = "127.0.0.1";
        this.port = port;
        this.kill = false;
    }

    public void run() {
        try {
            System.out.println("\nEscravo " + PeerInfo.name + " se conectando com o mestre na porta " + port + "...\n");
            client = new Socket(hostName, port);

            while(!kill) {
                // Comunica com o mestre
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void killThread(boolean kill) {
        this.kill = kill;
    }

}
