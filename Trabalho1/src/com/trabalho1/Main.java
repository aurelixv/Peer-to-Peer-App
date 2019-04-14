package com.trabalho1;

public class Main {
    public static void main(String[] args) {
        PeerInfo.name = args[0];
        PeerInfo.port = Integer.parseInt(args[1]);

        KeyPair keyPair = new KeyPair();

        Message broadcastMessage = new Message();
        broadcastMessage.setPublicKey(keyPair.getPublicKey());
        broadcastMessage.setSignedMessage(keyPair.sign(broadcastMessage));

        MulticastHandler multicastHandler = new MulticastHandler(PeerInfo.ip, broadcastMessage);

        KnownPeers peers = multicastHandler.getListener().getPeers();

        System.out.println("\n******Esperando peers se conectarem******\n");
        synchronized (multicastHandler.getListener()) {
            try {
                multicastHandler.getListener().wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("\n\n\n******Peers conhecidos: " + peers.countPeers() + "******\n\n\n");

        if(!peers.isMasterSet()) {
            System.out.println("\nVirando mestre...\n");
            Message masterMessage = new Message();
            masterMessage.setPeerName(PeerInfo.master);
            masterMessage.setPublicKey(keyPair.getPublicKey());
            masterMessage.setSignedMessage(keyPair.sign(masterMessage));
            multicastHandler.createMaster(masterMessage);
        } else {
            System.out.println("\nMestre na porta: " + peers.getPortFromPeer(PeerInfo.master) + "\n");
        }

        // Se for o mestre, vira um servidor
        if(peers.isMaster()) {
            System.out.println("\nIniciando servidor mestre...\n");
            UnicastServer server = new UnicastServer();
            server.start();
            try {
                server.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Caso contrario, vira um cliente
        else {
            System.out.println("\nIniciando conexao com o mestre...\n");
            UnicastClient client = new UnicastClient(peers.getPortFromPeer(PeerInfo.master));
            client.start();
            try {
                client.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        multicastHandler.endConnection();
    }
}
