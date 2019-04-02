package com.trabalho1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MessageSender extends Thread{

    private MulticastSocket s;
    private InetAddress group;

    public MessageSender(MulticastSocket s, InetAddress group) {
        this.s = s;
        this.group = group;
    }

    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while(true) {
                String str = reader.readLine();

                if(str.equals("exit")) {
                    break;
                }

                DatagramPacket messageOut = new DatagramPacket(str.getBytes(), str.length(), group, 6789);
                s.send(messageOut);
            }

        } catch (Exception e) {
            System.out.println("Erro na thread sender " + e);
        }
    }
}
