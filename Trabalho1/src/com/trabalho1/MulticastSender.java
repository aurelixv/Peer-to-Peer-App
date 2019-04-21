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
    private KeyPair keyPair;

    MulticastSender(MulticastSocket s, InetAddress group, Message message, int timer, KeyPair keyPair) {
        this.kill = false;
        this.s = s;
        this.group = group;
        this.broadcastMessage = message;
        this.timer = timer;
        this.keyPair = keyPair;
    }

    public void run() {
        try {
            System.out.println("[ MulticastSender ] Thread Sender iniciada com sucesso.");
            while(!kill) {
                broadcastMessage.setTime();
                broadcastMessage.setSignedMessage(keyPair.sign(broadcastMessage.getBroadcastMessage()));
                byte[] message = MessageSerializer.encode(broadcastMessage);
                DatagramPacket messageOut = new DatagramPacket(message, message.length, group, 6789);
                s.send(messageOut);

                sleep(timer);
            }
        } catch (Exception e) {
            System.out.println("[ MulticastSender ] Erro na thread sender " + e);
            e.printStackTrace();
        }
    }

    void killThread(boolean kill) {
        this.kill = kill;
    }

}
