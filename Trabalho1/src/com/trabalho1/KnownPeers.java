package com.trabalho1;

import java.security.PublicKey;
import java.util.Hashtable;
import java.util.Map;

public class KnownPeers {

    private Map<String, Message> peers;

    public KnownPeers() {
        peers = new Hashtable();
    }

    public void verifyPeer(Message message) {
        if (!peers.containsKey(message.getPeerName())) {
            peers.put(message.getPeerName(), message);
            System.out.println(message.getPeerName() + " adicionado com sucesso.");
        }
        else {
            System.out.println(message.getPeerName() + " ja esta na lista de peers conhecidos.");
        }
    }

    public boolean isMasterSet() {
        if(peers.containsKey("Mestre")) {
            return true;
        }
        return false;
    }

    public PublicKey getPublicKeyFromPeer(String peerName) {
        if(peers.containsKey(peerName)) {
            return peers.get(peerName).getPublicKey();
        }
        return null;
    }

    public int getPortFromPeer(String peerName) {
        if(peers.containsKey(peerName)) {
            return peers.get(peerName).getPeerPort();
        }
        return 0;
    }

    public int countPeers() {
        int count = 0;

        for (String i: peers.keySet()) {
            if(i != "Mestre") {
                count += 1;
            }
        }

        return count;
    }

}
