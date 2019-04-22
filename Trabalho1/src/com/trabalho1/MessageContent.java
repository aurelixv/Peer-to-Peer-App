package com.trabalho1;

import java.io.Serializable;
import java.security.PublicKey;
import java.time.LocalTime;

public class MessageContent implements Serializable {
    private String peerName;
    private int peerPort;
    private PublicKey publicKey;
    private String command;
    private String message;
    private LocalTime time;

    public MessageContent() {
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
    String getCommand() {
        return this.command;
    }
    LocalTime getTime() {
        return this.time;
    }
    String getMessage() {
        return this.message;
    }
}
