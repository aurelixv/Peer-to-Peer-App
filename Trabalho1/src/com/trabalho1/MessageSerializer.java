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
            System.out.println("[ MessageSerializer ] Erro no encode da mensagem " + e);
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
            System.out.println("[ MessageSerializer ] Erro no decode da mensagem " + e);
        }

        return decoded;
    }

}
