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

    public PublicKey getPublicKeyFromPeer(String peerName) {
        if(peers.containsKey(peerName)) {
            return peers.get(peerName).getPublicKey();
        }
        return null;
    }

    public Integer getPortFromPeer(String peerName) {
        if(peers.containsKey(peerName)) {
            return peers.get(peerName).getPeerPort();
        }
        return 0;
    }

}