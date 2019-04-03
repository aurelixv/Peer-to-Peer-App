package com.trabalho1;

import java.net.*;
import java.io.*;

// IP: 228.5.6.7

public class MulticastPeer {
    public static void main(String args[]) {



        Message msg = new Message();
        msg.setPeerName("aurelio");
        msg.setPeerPort(112233);

        System.out.println(MessageSerializer.encode(msg));
        System.exit(1);

        // args give message contents and destination multicast group (e.g. "")
        MulticastSocket s = null;
        try {
            InetAddress group = InetAddress.getByName("228.5.6.7");
            s = new MulticastSocket(6789);
            s.joinGroup(group);
            MessageListener listener = new MessageListener(s);
            MessageSender sender = new MessageSender(s, group);

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
