package com.trabalho1;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

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

    public static Message decode(String message) {
        
        return new Message();
    }
}

/*
ByteArrayInputStream bis = new ByteArrayInputStream(yourBytes);
ObjectInput in = null;
try {
  in = new ObjectInputStream(bis);
  Object o = in.readObject();
  ...
} finally {
  try {
    if (in != null) {
      in.close();
    }
  } catch (IOException ex) {
    // ignore close exception
  }
}
 */