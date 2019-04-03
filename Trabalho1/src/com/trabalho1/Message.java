package com.trabalho1;

import java.io.Serializable;

public class Message implements Serializable {
    private String peerName;
    private int peerPort;

    public void setPeerName(String name) {
        this.peerName = name;
    }
    public void setPeerPort(int port) {
        this.peerPort = port;
    }

    public String getPeerName() {
        return peerName;
    }
    public int getPeerPort() {
        return peerPort;
    }
}
