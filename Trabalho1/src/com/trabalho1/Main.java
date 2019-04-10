package com.trabalho1;

public class Main {
    public static void main(String[] args) {
        //MessageSerializer.debug();

        KeyPair keyPair = new KeyPair();

        Message broadcastMessage = new Message();
        broadcastMessage.setPeerName(args[1]);
        broadcastMessage.setPeerPort(Integer.parseInt(args[2]));
        broadcastMessage.setPublicKey(keyPair.getPublicKey());
        broadcastMessage.setSignedMessage(keyPair.sign(broadcastMessage));

        MulticastPeer multicastPeer = new MulticastPeer("228.5.6.7", broadcastMessage);
        try {
            multicastPeer.getListener().join();
            multicastPeer.getSender().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            multicastPeer.endConnection();
        }

    }
}
