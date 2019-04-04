package com.trabalho1;

import java.io.*;

public class MessageSerializer {
    public static byte[] encode(Message message) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] encoded = null;

        try {
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(message);
            encoded = bos.toByteArray();
            bos.close();
        }
        catch (Exception e) {
            System.out.println("Erro no encode da mensagem " + e);
        }

        return encoded;
    }

    public static Message decode(byte[] message) {
        ByteArrayInputStream bis = new ByteArrayInputStream(message);
        Message decoded = null;

        try {
            ObjectInputStream in = new ObjectInputStream(bis);
            decoded = (Message)in.readObject();
            in.close();
        }
        catch (Exception e) {
            System.out.println("Erro no decode da mensagem " + e);
        }

        return decoded;
    }

    public static void debug() {
        Message msg = new Message();
        msg.setPeerName("aurelio");
        msg.setPeerPort(112233);

        byte[] encoded = MessageSerializer.encode(msg);
        Message decoded = MessageSerializer.decode(encoded);

        System.out.println("Encoded: " + encoded);
        System.out.println("Decoded: " + decoded);
        System.out.println(decoded.getPeerName() + " " + decoded.getPeerPort());

        System.exit(1);
    }
}
