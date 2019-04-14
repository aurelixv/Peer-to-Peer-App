package com.trabalho1;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastSender extends Thread{

    private MulticastSocket s;
    private InetAddress group;
    private Message broadcastMessage;
    private boolean kill;

    public MulticastSender(MulticastSocket s, InetAddress group, Message message) {
        this.kill = false;
        this.s = s;
        this.group = group;
        this.broadcastMessage = message;
    }

    public void run() {
        try {
            System.out.println("Thread Sender iniciada com sucesso.");
            while(!kill) {
                byte[] message = MessageSerializer.encode(broadcastMessage);
                DatagramPacket messageOut = new DatagramPacket(message, message.length, group, 6789);
                s.send(messageOut);

                this.sleep(5000);
            }
        } catch (Exception e) {
            System.out.println("Erro na thread sender " + e);
            e.printStackTrace();
        }
    }

    public void killThread(boolean kill) {
        this.kill = kill;
    }

}
