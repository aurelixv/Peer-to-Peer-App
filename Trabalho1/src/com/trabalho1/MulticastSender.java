package com.trabalho1;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;

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
                broadcastMessage.getMessageContent().setTime();
                broadcastMessage.setSignedMessage(
                        keyPair.sign(Arrays.toString(MessageSerializer.encode(broadcastMessage.getMessageContent()))));
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
