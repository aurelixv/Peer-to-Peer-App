package com.trabalho1;

import java.io.IOException;
import java.net.Socket;

public class UnicastClient extends Thread {

    private Socket client;
    private String hostName;
    private int port;

    public UnicastClient(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
        try {
            client = new Socket(hostName, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            client = new Socket(hostName, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
