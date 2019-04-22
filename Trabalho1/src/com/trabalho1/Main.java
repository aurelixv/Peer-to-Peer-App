package com.trabalho1;

public class Main {
    public static void main(String[] args) {
        // Nome e porta do peer, passados como argumentos
        PeerInfo.name = args[0];
        PeerInfo.port = Integer.parseInt(args[1]);
        // Flag do while principal
        boolean kill = false;

        // Gera um par de chaves (publico/privado) para o peer
        KeyPair keyPair = new KeyPair();

        // Cria a mensagem de descoberta multicasting do peer
        Message broadcastMessage = new Message();
        broadcastMessage.getMessageContent().setPublicKey(keyPair.getPublicKey());
        //broadcastMessage.setSignedMessage(keyPair.sign(broadcastMessage.getMessageContent().toString()));

        // Cria o responsavel por gerenciar as comunicacoes multicasting do peer
        MulticastHandler multicastHandler = new MulticastHandler(PeerInfo.ip, broadcastMessage, keyPair);

        // Pega a referencia do objeto KnownPeers, presente na Thread recem criada MulticastListener
        KnownPeers peers = multicastHandler.getListener().getPeers();

        // Aguarda a Thread MulticastListener avisar que existem ao menos 3 peers conectados
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

            // Inicia a eleicao, onde o peer de maior numero ganha
            System.out.println("[ Main ] Iniciando eleicao...");
            peers.startElection();

            // Se for o mestre, vira um servidor
            if (PeerInfo.isMaster && !peers.isMasterSet()) {
                System.out.println("\n[ Main ] Virando mestre...\n");
                Message masterMessage = new Message();
                MessageContent masterMessageContent = masterMessage.getMessageContent();
                masterMessageContent.setPublicKey(keyPair.getPublicKey());
                multicastHandler.createMaster(masterMessage);
                System.out.println("\n[ Main ] Iniciando servidor mestre...\n");
                UnicastServer server = new UnicastServer(keyPair);
                server.start();
                // Depois de inicializado, a main espera a Thread servidor encerrar
                try {
                    server.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // Caso contrario, vira um cliente
            else {
                // Espera o mestre comunicar a sua disponibilidade
                while(!peers.isMasterSet()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // Inicia o cliente Unicast com o mestre
                System.out.println("\n[ Main ] Mestre na porta: " + peers.getPortFromPeer(PeerInfo.master) + "\n");
                System.out.println("\n[ Main ] Iniciando conexao com o mestre...\n");
                UnicastClient client = new UnicastClient(peers.getPortFromPeer(PeerInfo.master),
                        peers.getPublicKeyFromPeer(PeerInfo.master));
                // Inicializa o WatchDog, responsavel por verificar a autenticidade e disponibilidade do servidor mestre
                WatchDog watchDog = multicastHandler.createWatchDog(peers.getPublicKeyFromPeer(PeerInfo.master));
                client.start();
                // Main espera o WatchDog encerrar, indicando erro na comunicacao com o mestre
                try {
                    watchDog.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Caso erro no mestre, mata a Thread cliente unicast e remove da lista de conhecidos o mestre
                client.killThread(true);
                peers.removePeer(peers.getPortFromPeer(PeerInfo.master));
            }
            // Caso seja o unico peer conectado, encerra
            if(peers.countPeers() == 1) {
                kill = true;
            }
        }
        // Encerra as conexoes ao sair
        multicastHandler.endConnection();
    }
}
