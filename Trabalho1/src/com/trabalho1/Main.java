package com.trabalho1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) {
        //MessageSerializer.debug();

        PeerInfo.name = args[0];
        PeerInfo.port = Integer.parseInt(args[1]);

        KnownPeers peers = new KnownPeers();

        KeyPair keyPair = new KeyPair();

        Message broadcastMessage = new Message();
        broadcastMessage.setPublicKey(keyPair.getPublicKey());
        broadcastMessage.setSignedMessage(keyPair.sign(broadcastMessage));

        BlockingQueue<Message> listenerQueue = new LinkedBlockingQueue<>();

        MulticastPeer multicastPeer = new MulticastPeer("228.5.6.7", broadcastMessage, listenerQueue);

        while(peers.countPeers() < 3) {
            try {
                peers.verifyPeer(listenerQueue.take());
            } catch (Exception e) {
                System.out.println("Erro na fila de mensagens da thread listener. " + e);
                e.printStackTrace();
            }

        }

        System.out.println("\n\n\n******Peers conhecidos: " + peers.countPeers() + "******\n\n\n");

        if(peers.isMasterSet() == false) {
            System.out.println("Virando mestre...");
            Message masterMessage = new Message();
            masterMessage.setPeerName("Mestre");
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
