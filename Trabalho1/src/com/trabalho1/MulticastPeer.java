package com.trabalho1;

import java.net.*;
import java.io.*;

// IP: 228.5.6.7

public class MulticastPeer {

    private Message broadcastMessage;

    public MulticastPeer(String ip, Message message) {
        this.broadcastMessage = message;
        MulticastSocket s = null;

        try {
            InetAddress group = InetAddress.getByName(ip);
            s = new MulticastSocket(6789);
            s.joinGroup(group);
            MulticastListener listener = new MulticastListener(s);
            MulticastSender sender = new MulticastSender(s, group, broadcastMessage);

            System.out.println("Iniciando as threads do multicasting...");
            sender.start();
            listener.start();
            sender.join();
            listener.join();

            System.out.println("Encerrando conexoes...");
            s.leaveGroup(group);
            s.close();
        } catch(SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch(IOException e) {
            System.out.println("IO: " + e.getMessage());
        } catch(InterruptedException e) {
            e.printStackTrace();
        } finally {
            if(s != null) s.close();
        }
    }

}
