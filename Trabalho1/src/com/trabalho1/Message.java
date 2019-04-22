package com.trabalho1;

import java.io.Serializable;

public class Message implements Serializable {

    private MessageContent messageContent;
    private byte[] signedMessageContent;

    Message() {
        messageContent = new MessageContent();
    }

    MessageContent getMessageContent() {
        return this.messageContent;
    }

    byte[] getSignedMessageContent() {
        return this.signedMessageContent;
    }

    void setSignedMessage(byte[] signedMessageContent) {
        this.signedMessageContent = signedMessageContent;
    }

}
