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
            System.out.println("Thread Sender iniciada com sucesso.");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while(true) {
                //System.out.print("Digite a mensagem a ser enviada: ");
                String str = reader.readLine();

                DatagramPacket messageOut = new DatagramPacket(str.getBytes(), str.length(), group, 6789);
                s.send(messageOut);

                if(str.equals("exit")) {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Erro na thread sender " + e);
        }
    }
}
