package com.trabalho1;

import java.io.Serializable;
import java.security.PublicKey;

public class Message implements Serializable {
    private String peerName;
    private int peerPort;
    private PublicKey publicKey;
    private byte[] signedMessage;
    private String command;
    private String message;

    public Message() {
        this.peerName = PeerInfo.name;
        this.peerPort = PeerInfo.port;
    }

    public void setPeerName(String name) {
        this.peerName = name;
    }
    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
    public void setSignedMessage(byte[] signedMessage) {
        this.signedMessage = signedMessage;
    }
    public void setCommand(String command) {
        this.command = command;
    }
    public void setMessage(String message) {
        this.message = message;
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
    //public String getBroadcastMessage() {
    //    return this.getPeerName() + this.getPeerPort();
    //}
    public String getCommand() {
        return this.command;
    }
}
