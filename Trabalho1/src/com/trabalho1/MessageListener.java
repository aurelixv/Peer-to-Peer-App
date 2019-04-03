package com.trabalho1;

import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class MessageListener extends Thread {

    private MulticastSocket s;

    public MessageListener(MulticastSocket s) {
        this.s = s;
    }

    public void run() {
        try {
            System.out.println("Thread Listener iniciada com sucesso.");
            while(true) {
                byte[] buffer = new byte[1000];
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                s.receive(messageIn);
                String message = new String(messageIn.getData()).trim();

                if(!message.equals("exit")) {
                    System.out.println("Received: " + message);
                }
                else {
                    break;
                }
            }
        } catch (Exception e){
            System.out.println("Erro na thread listener " + e);
        }
    }
}
