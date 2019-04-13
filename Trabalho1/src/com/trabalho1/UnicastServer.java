package com.trabalho1;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class UnicastServer extends Thread{

    private ServerSocket serverSocket;

    public UnicastServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void run() {
        try {
            Socket client = serverSocket.accept();
            InetAddress addr = client.getInetAddress();
            int clientPort = client.getPort();
            System.out.println("Conexao com IP: " + addr + " Porta: " + clientPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
