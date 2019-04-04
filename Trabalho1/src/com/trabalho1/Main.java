package com.trabalho1;

public class Main {
    public static void main(String args[]) {
        //MessageSerializer.debug();

        SignMessage assinatura = new SignMessage();
        Message broadcastMessage = new Message();
        broadcastMessage.setPeerName(args[1]);
        broadcastMessage.setPeerPort(Integer.parseInt(args[2]));
        broadcastMessage.setPublicKey(assinatura.getPublicKey());

        MulticastPeer multicastPeer = new MulticastPeer(args[0], broadcastMessage);

    }
}
