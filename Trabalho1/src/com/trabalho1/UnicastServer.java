package com.trabalho1;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class UnicastServer extends Thread{

    private ServerSocket serverSocket;

    public UnicastServer() {
        try {
            serverSocket = new ServerSocket(PeerInfo.port);
            System.out.println("\nServidor unicast mestre inicializado.\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while(true) {
                Socket client = serverSocket.accept();
                InetAddress addr = client.getInetAddress();
                int clientPort = client.getPort();
                System.out.println("\nConexao com IP: " + addr + " Porta: " + clientPort + "\n");
                new UnicastConnection(client).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
