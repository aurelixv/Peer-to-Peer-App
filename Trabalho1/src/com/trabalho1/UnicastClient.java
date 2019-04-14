package com.trabalho1;

import java.io.IOException;
import java.net.Socket;

public class UnicastClient extends Thread {

    private Socket client;
    private String hostName;
    private int port;

    public UnicastClient(int port) {
        this.hostName = "127.0.0.1";
        this.port = port;
        try {
            System.out.println("\nEscravo " + PeerInfo.name + " se conectando com o mestre na porta " + port + "...\n");
            client = new Socket(hostName, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            System.out.println();
            client = new Socket(hostName, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
