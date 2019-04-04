package com.trabalho1;

import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class MulticastListener extends Thread {

    private MulticastSocket s;

    public MulticastListener(MulticastSocket s) {
        this.s = s;
    }

    public void run() {
        try {
            System.out.println("Thread Listener iniciada com sucesso.");
            while(true) {
                byte[] buffer = new byte[1000];
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                s.receive(messageIn);
                Message message = (Message) MessageSerializer.decode(messageIn.getData());

                System.out.println(message.getPeerName() + " na porta " + message.getPeerPort());
                System.out.println("Chave publica: " + message.getPublicKey().toString());
            }
        } catch (Exception e){
            System.out.println("Erro na thread listener " + e);
        }
    }
}
