package com.trabalho1;

import java.io.Serializable;
import java.security.PublicKey;

public class Message implements Serializable {
    private String peerName;
    private int peerPort;
    private PublicKey publicKey;

    public void setPeerName(String name) {
        this.peerName = name;
    }
    public void setPeerPort(int port) {
        this.peerPort = port;
    }
    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public String getPeerName() {
        return this.peerName;
    }
    public int getPeerPort() {
        return this.peerPort;
    }
    public PublicKey getPublicKey() {
        return this.publicKey;
    }
}
