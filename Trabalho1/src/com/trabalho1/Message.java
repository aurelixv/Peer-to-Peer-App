package com.trabalho1;

import java.io.Serializable;
import java.security.PublicKey;
import java.time.LocalTime;

public class Message implements Serializable {
    private String peerName;
    private int peerPort;
    private PublicKey publicKey;
    private byte[] signedMessage;
    private String command;
    private String message;
    private LocalTime time;

    public Message() {
        if(PeerInfo.isMaster) {
            this.peerName = PeerInfo.master;
        } else {
            this.peerName = PeerInfo.name;
        }
        this.peerPort = PeerInfo.port;
        this.time = null;
    }

    void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
    void setSignedMessage(byte[] signedMessage) {
        this.signedMessage = signedMessage;
    }
    void setCommand(String command) {
        this.command = command;
    }
    void setMessage(String message) {
        this.message = message;
    }
    void setTime() {
        this.time = LocalTime.now();
    }

    String getPeerName() {
        return this.peerName;
    }
    int getPeerPort() {
        return this.peerPort;
    }
    PublicKey getPublicKey() {
        return this.publicKey;
    }
    byte[] getSignedMessage() {
        return signedMessage;
    }
    String getCommand() {
        return this.command;
    }
    LocalTime getTime() {
        return this.time;
    }
    String getBroadcastMessage() {
        if(this.time == null) {
            setTime();
        }
        return this.peerName + this.peerPort + this.time.toString();
    }
    String getUnicastMessage() {
        if(this.time == null) {
            setTime();
        }
        return this.peerName + this.peerPort + this.command + this.message + this.time.toString();
    }
    String getMessage() {
        return this.message;
    }
}
