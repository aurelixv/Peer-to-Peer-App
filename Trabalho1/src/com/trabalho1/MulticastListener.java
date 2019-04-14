package com.trabalho1;

import java.net.DatagramPacket;
import java.net.MulticastSocket;
import static com.trabalho1.MessageSerializer.*;

public class MulticastListener extends Thread {

    private MulticastSocket s;
    private KnownPeers peers;
    private boolean kill;

    public MulticastListener(MulticastSocket s) {
        this.s = s;
        this.peers = new KnownPeers();
        this.kill = false;
    }

    public void run() {
        try {
            System.out.println("Thread Listener iniciada com sucesso.");

            while(!kill) {
                byte[] buffer = new byte[1000];
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                s.receive(messageIn);
                Message message = decode(messageIn.getData());

                if(message.getPeerPort() != PeerInfo.port) {
                    peers.verifyPeer(message);
                }

                if(peers.countPeers() >= 3) {
                    synchronized (this) {
                        this.notify();
                    }
                }

//                System.out.println(message.getPeerName() + " na porta " + message.getPeerPort());
//                System.out.println("Assinatura valida: " + MessageSignature.verify(message.getMessage(),
//                        message.getSignedMessage(),
//                        message.getPublicKey()));
            }
        } catch (Exception e){
            System.out.println("Erro na thread listener " + e);
            e.printStackTrace();
        }
    }

    public void killThread(boolean kill) {
        this.kill = kill;
    }

    public KnownPeers getPeers() {
        return this.peers;
    }

}
