package com.trabalho1;

public class Main {
    public static void main(String[] args) {
        //MessageSerializer.debug();

        KeyPair keyPair = new KeyPair();
        Message broadcastMessage = new Message();
        broadcastMessage.setPeerName(args[1]);
        broadcastMessage.setPeerPort(Integer.parseInt(args[2]));
        broadcastMessage.setPublicKey(keyPair.getPublicKey());

        //System.out.println("Mensagem a ser assinada: " + broadcastMessage.getMessage());

        broadcastMessage.setSignedMessage(keyPair.sign(broadcastMessage));

        //System.out.println("Mensagem assinada: " + broadcastMessage.getSignedMessage().toString());

        /*
        System.out.println("Verificando assinatura: " +
        MessageSignature.verify(broadcastMessage.getMessage(),
                broadcastMessage.getSignedMessage(),
                broadcastMessage.getPublicKey())
        );
        */

        //System.exit(1);

        MulticastPeer multicastPeer = new MulticastPeer(args[0], broadcastMessage);

    }
}
