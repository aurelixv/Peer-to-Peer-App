package com.trabalho1;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastSender extends Thread{

    private MulticastSocket s;
    private InetAddress group;
    private Message broadcastMessage;
    private boolean kill;
    private int timer;

    public MulticastSender(MulticastSocket s, InetAddress group, Message message, int timer) {
        this.kill = false;
        this.s = s;
        this.group = group;
        this.broadcastMessage = message;
        this.timer = timer;
    }

    public void run() {
        try {
            System.out.println("Thread Sender iniciada com sucesso.");
            while(!kill) {
                byte[] message = MessageSerializer.encode(broadcastMessage);
                DatagramPacket messageOut = new DatagramPacket(message, message.length, group, 6789);
                s.send(messageOut);

                this.sleep(timer);
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
