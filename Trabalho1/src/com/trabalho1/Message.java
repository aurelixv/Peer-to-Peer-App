package com.trabalho1;

import java.io.Serializable;
import java.security.PublicKey;

public class Message implements Serializable {
    private String peerName;
    private int peerPort;
    private PublicKey publicKey;
    private byte[] signedMessage;

    public void setPeerName(String name) {
        this.peerName = name;
    }
    public void setPeerPort(int port) {
        this.peerPort = port;
    }
    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
    public void setSignedMessage(byte[] signedMessage) {
        this.signedMessage = signedMessage;
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
    public byte[] getSignedMessage() {
        return signedMessage;
    }
    public String getMessage() {
        return this.getPeerName() + this.getPeerPort();
    }
}
