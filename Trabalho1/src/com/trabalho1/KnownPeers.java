package com.trabalho1;

import java.security.PublicKey;
import java.util.Hashtable;
import java.util.Map;

class KnownPeers {

    private Map<String, MessageContent> peers;

    KnownPeers() {
        peers = new Hashtable<>();
    }

    void verifyPeer(MessageContent messageContent) {
        if (!peers.containsKey(messageContent.getPeerName())) {
            peers.put(messageContent.getPeerName(), messageContent);
            if(!messageContent.getPeerName().equals(PeerInfo.master))
                System.out.println("[ KnownPeers ] " + messageContent.getPeerName() + " na porta " +
                        messageContent.getPeerPort() + " adicionado com sucesso.");
        }
    }

    boolean isMasterSet() {
        return peers.containsKey(PeerInfo.master);
    }

    PublicKey getPublicKeyFromPeer(String peerName) {
        if(peers.containsKey(peerName)) {
            return peers.get(peerName).getPublicKey();
        }
        return null;
    }

    int getPortFromPeer(String peerName) {
        if(peers.containsKey(peerName)) {
            return peers.get(peerName).getPeerPort();
        }
        return 0;
    }

    int countPeers() {
        if(isMasterSet()) {
            return peers.size() - 1;
        }
        return peers.size();
    }

    void removePeer(int port) {
        Map<String, MessageContent> aux = new Hashtable<>(peers);
        for (String i: aux.keySet()) {
            if(peers.get(i).getPeerPort() == port) {
                peers.remove(i);
            }
        }
    }

    void startElection() {
        String masterName = "Peer0";

        for (String i: peers.keySet()) {
            if(!i.equals("Mestre")) {
                if (Integer.parseInt(i.substring(i.length() - 1)) >
                        Integer.parseInt(masterName.substring(masterName.length() - 1))) {
                    masterName = i;
                }
            }
        }

        if(Integer.parseInt(PeerInfo.name.substring(PeerInfo.name.length() - 1)) >
                Integer.parseInt(masterName.substring(masterName.length() - 1))) {
            PeerInfo.isMaster = true;
        }
    }

}
