package com.trabalho1;

import java.net.*;
import java.io.*;
import java.util.concurrent.BlockingQueue;

// IP: 228.5.6.7

public class MulticastPeer {

    private Message broadcastMessage;
    private MulticastSocket s;
    private InetAddress group;
    private MulticastListener listener;
    private MulticastSender sender;

    public MulticastPeer(String ip, Message message, BlockingQueue<Message> listenerQueue) {
        this.broadcastMessage = message;

        try {
            group = InetAddress.getByName(ip);
            s = new MulticastSocket(6789);
            s.joinGroup(group);
            listener = new MulticastListener(s, listenerQueue);
            sender = new MulticastSender(s, group, broadcastMessage);

            System.out.println("Iniciando as threads do multicasting...");
            sender.start();
            listener.start();

        } catch(SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch(IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }

    public MulticastListener getListener() {
        return listener;
    }

    public MulticastSender getSender() {
        return sender;
    }

    public void endConnection() {
        System.out.println("Encerrando conexoes...");
        sender.killThread(true);
        try {
            sender.join();
        } catch (Exception e) {

        }

        this.getListener().interrupt();
        try {
            s.leaveGroup(group);
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
