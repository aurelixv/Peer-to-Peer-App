package com.trabalho1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.TimeUnit;

public class MulticastSender extends Thread{

    private MulticastSocket s;
    private InetAddress group;
    private Message broadcastMessage;

    public MulticastSender(MulticastSocket s, InetAddress group, Message message) {
        this.s = s;
        this.group = group;
        this.broadcastMessage = message;
    }

    public void run() {
        try {
            System.out.println("Thread Sender iniciada com sucesso.");
            //BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while(true) {
                //System.out.print("Digite a mensagem a ser enviada: ");
                //String str = reader.readLine();

                byte[] message = MessageSerializer.encode(broadcastMessage);
                DatagramPacket messageOut = new DatagramPacket(message, message.length, group, 6789);
                s.send(messageOut);

//                if(str.equals("exit")) {
//                    break;
//                }

                this.sleep(5000);
            }
        } catch (Exception e) {
            System.out.println("Erro na thread sender " + e);
        }
    }
}
