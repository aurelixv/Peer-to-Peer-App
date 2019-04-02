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
            while(true) {
                byte[] buffer = new byte[1000];
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                s.receive(messageIn);
                if((new String(messageIn.getData())) != "exit") {
                    System.out.println("Received:" + new String(messageIn.getData()).trim());
                }
                else {
                    break;
                }
            }
        } catch (Exception e){}
    }
}
