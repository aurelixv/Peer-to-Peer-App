package com.PeertoPeerApp;

import java.net.DatagramPacket;
import java.net.MulticastSocket;
import static com.PeertoPeerApp.MessageSerializer.*;

public class MulticastListener extends Thread {

    private MulticastSocket s;
    private KnownPeers peers;
    private boolean kill;

    MulticastListener(MulticastSocket s) {
        this.s = s;
        this.peers = new KnownPeers();
        this.kill = false;
    }

    public void run() {
        try {
            System.out.println("[ MulticastListener ] Thread Listener iniciada com sucesso.");

            while(!kill) {
                byte[] buffer = new byte[1000];
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                s.receive(messageIn);
                Message message = (Message)decode(messageIn.getData());

                if(message.getMessageContent().getPeerPort() != PeerInfo.port) {
                    peers.verifyPeer(message.getMessageContent());
                }

                if(peers.countPeers() >= 3) {
                    synchronized (this) {
                        this.notify();
                    }
                }

            }
        } catch (Exception e){
            System.out.println("[ MulticastListener ] Erro na thread listener " + e);
            e.printStackTrace();
        }
    }

    void killThread(boolean kill) {
        this.kill = kill;
    }

    KnownPeers getPeers() {
        return this.peers;
    }

}
