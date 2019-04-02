package com.trabalho1;

import java.net.*;
import java.io.*;

// IP: 228.5.6.7

public class MulticastPeer {
    public static void main(String args[]) {
        // args give message contents and destination multicast group (e.g. "")
        MulticastSocket s = null;
        try {
            InetAddress group = InetAddress.getByName("228.5.6.7");
            s = new MulticastSocket(6789);
            s.joinGroup(group);
            MessageListener listener = new MessageListener(s);
            listener.start();
            MessageSender sender = new MessageSender(s, group);
            sender.start();
            sender.join();

            listener.interrupt();

            System.out.println("aaa");

            s.leaveGroup(group);
        } catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        } catch (IOException e){System.out.println("IO: " + e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if(s != null) s.close();
        }
    }
}

