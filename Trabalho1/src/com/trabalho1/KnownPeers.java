package com.trabalho1;

import java.security.PublicKey;
import java.util.Hashtable;
import java.util.Map;

public class KnownPeers {

    private Map<String, Message> peers;

    public KnownPeers() {
        peers = new Hashtable<>();
    }

    public void verifyPeer(Message message) {
        if (!peers.containsKey(message.getPeerName())) {
            peers.put(message.getPeerName(), message);
            if(!message.getPeerName().equals(PeerInfo.master))
                System.out.println(message.getPeerName() + " na porta " + message.getPeerPort() + " adicionado com sucesso.");
        }
    }

    public boolean isMasterSet() {
        if(peers.containsKey(PeerInfo.master)) {
            return true;
        }
        return false;
    }

    public boolean isMaster() {
        if(getPortFromPeer(PeerInfo.name) == getPortFromPeer(PeerInfo.master)) {
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
        if(isMasterSet()) {
            return peers.size() - 1;
        }
        return peers.size();
    }

    public void removePeer(int port) {
        Map<String, Message> aux = new Hashtable<>(peers);
        for (String i: aux.keySet()) {
            if(peers.get(i).getPeerPort() == port) {
                peers.remove(i);
            }
        }
    }

}
