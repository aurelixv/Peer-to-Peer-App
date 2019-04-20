package com.trabalho1;

public class Main {
    public static void main(String[] args) {
        PeerInfo.name = args[0];
        PeerInfo.port = Integer.parseInt(args[1]);
        boolean kill = false;

        KeyPair keyPair = new KeyPair();

        Message broadcastMessage = new Message();
        broadcastMessage.setPublicKey(keyPair.getPublicKey());
        broadcastMessage.setSignedMessage(keyPair.sign(broadcastMessage.getBroadcastMessage()));

        MulticastHandler multicastHandler = new MulticastHandler(PeerInfo.ip, broadcastMessage, keyPair);

        KnownPeers peers = multicastHandler.getListener().getPeers();

        System.out.println("\n[ Main ] ******Esperando peers se conectarem******\n");
        synchronized (multicastHandler.getListener()) {
            try {
                multicastHandler.getListener().wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        while(!kill) {

            System.out.println("\n\n\n[ Main ] ******Peers conhecidos: " + peers.countPeers() + "******\n\n\n");

            System.out.println("[ Main ] Iniciando eleicao...");
            peers.startElection();

            // Se for o mestre, vira um servidor
            if (PeerInfo.isMaster && !peers.isMasterSet()) {
                System.out.println("\n[ Main ] Virando mestre...\n");
                Message masterMessage = new Message();
                masterMessage.setPublicKey(keyPair.getPublicKey());
                multicastHandler.createMaster(masterMessage);
                System.out.println("\n[ Main ] Iniciando servidor mestre...\n");
                UnicastServer server = new UnicastServer(keyPair);
                server.start();
                try {
                    server.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // Caso contrario, vira um cliente
            else {

                while(!peers.isMasterSet()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("\n[ Main ] Mestre na porta: " + peers.getPortFromPeer(PeerInfo.master) + "\n");
                System.out.println("\n[ Main ] Iniciando conexao com o mestre...\n");
                UnicastClient client = new UnicastClient(peers.getPortFromPeer(PeerInfo.master),
                        peers.getPublicKeyFromPeer(PeerInfo.master));
                WatchDog watchDog = multicastHandler.createWatchDog(peers.getPublicKeyFromPeer(PeerInfo.master));
                client.start();
                synchronized (watchDog) {
                    try {
                        watchDog.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                client.killThread(true);
                peers.removePeer(peers.getPortFromPeer(PeerInfo.master));
            }
        }

        multicastHandler.endConnection();
    }
}
