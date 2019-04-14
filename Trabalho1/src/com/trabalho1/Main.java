package com.trabalho1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) {
        PeerInfo.name = args[0];
        PeerInfo.port = Integer.parseInt(args[1]);

        KeyPair keyPair = new KeyPair();

        Message broadcastMessage = new Message();
        broadcastMessage.setPublicKey(keyPair.getPublicKey());
        broadcastMessage.setSignedMessage(keyPair.sign(broadcastMessage));

        MulticastPeer multicastPeer = new MulticastPeer("228.5.6.7", broadcastMessage);

        KnownPeers peers = multicastPeer.getListener().getPeers();

        System.out.println("\n******Esperando peers se conectarem******\n");
        synchronized (multicastPeer.getListener()) {
            try {
                multicastPeer.getListener().wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("\n\n\n******Peers conhecidos: " + peers.countPeers() + "******\n\n\n");

        if(!peers.isMasterSet()) {
            System.out.println("Virando mestre...");
            Message masterMessage = new Message();
            masterMessage.setPeerName(PeerInfo.master);
            masterMessage.setPublicKey(keyPair.getPublicKey());
            masterMessage.setSignedMessage(keyPair.sign(masterMessage));
            multicastPeer.createMaster(masterMessage);
        }

        try {
            multicastPeer.getSender().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        multicastPeer.endConnection();
    }
}
