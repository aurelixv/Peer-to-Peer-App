package com.trabalho1;

import java.net.*;
import java.io.*;
import java.security.Key;
import java.security.PublicKey;

// IP: 228.5.6.7

public class MulticastHandler {

    private Message broadcastMessage;
    private MulticastSocket s;
    private InetAddress group;
    private MulticastListener listener;
    private MulticastSender sender;
    private MulticastSender master;
    private WatchDog watchDog;
    private KeyPair keyPair;

    public MulticastHandler(String ip, Message message, KeyPair keyPair) {
        this.broadcastMessage = message;
        this.keyPair = keyPair;

        try {
            group = InetAddress.getByName(ip);
            s = new MulticastSocket(6789);
            s.joinGroup(group);
            listener = new MulticastListener(s);
            sender = new MulticastSender(s, group, broadcastMessage, 5000, keyPair);

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

    public WatchDog createWatchDog(PublicKey masterPublicKey) {
        watchDog = new WatchDog(s, masterPublicKey);
        watchDog.start();
        return watchDog;
    }

    public void createMaster(Message message) {
        master = new MulticastSender(s, group, message, 1000, keyPair);
        master.start();
    }

    public MulticastSender getSender() {
        return sender;
    }

    public void killMaster(boolean kill) {
        if(kill) {
            try {
                master.killThread(true);
                master.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void killListener(boolean kill) {
        if(kill) {
            try {
                listener.killThread(true);
                listener.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void killSender(boolean kill) {
        if(kill) {
            try {
                sender.killThread(true);
                sender.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void endConnection() {
        System.out.println("Encerrando conexoes...");

        if(listener != null) {
            System.out.println("Matando thread multicast listener...");
            killListener(true);
        }

        if(sender != null) {
            System.out.println("Matando thread multicast sender...");
            killSender(true);
        }

        if(master != null) {
            System.out.println("Matando thread multicast master...");
            killMaster(true);
        }

        try {
            s.leaveGroup(group);
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
