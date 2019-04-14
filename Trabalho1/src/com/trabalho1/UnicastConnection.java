package com.trabalho1;

import java.net.Socket;

public class UnicastConnection extends Thread {

    private Socket client;

    public UnicastConnection(Socket client) {
        this.client = client;
    }

    public void run() {

    }

}
