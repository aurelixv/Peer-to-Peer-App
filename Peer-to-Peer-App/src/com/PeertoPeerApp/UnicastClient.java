package com.PeertoPeerApp;

import java.io.*;
import java.net.Socket;
import java.security.PublicKey;
import java.time.LocalTime;
import java.util.Arrays;

public class UnicastClient extends Thread {

    private Socket client;
    private String hostName;
    private int port;
    private boolean kill;
    private PublicKey masterPublicKey;
    private long adjustment;

    UnicastClient(int port, PublicKey masterPublicKey) {
        this.hostName = "127.0.0.1";
        this.port = port;
        this.kill = false;
        this.masterPublicKey = masterPublicKey;
        this.adjustment = 0;
    }

    public void run() {
        try {
            System.out.println("\n[ UnicastClient ] Escravo " + PeerInfo.name +
                    " se conectando com o mestre na porta " + port + "...\n");
            client = new Socket(hostName, port);

            // Para enviar as mensagens para o mestre
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            // Para receber as mensagens do mestre
            DataInputStream in = new DataInputStream(client.getInputStream());

            int encodedMessageLength = 0;

            while(!kill) {
                // Comunica com o mestre
                try {
                    byte[] encodedMessage = new byte[in.readInt()];
                    in.readFully(encodedMessage, 0, encodedMessage.length);
                    Message message = (Message)MessageSerializer.decode(encodedMessage);
                    MessageContent messageContent = message.getMessageContent();
                    if (MessageSignature.verify(Arrays.toString(MessageSerializer.encode(messageContent)),
                            message.getSignedMessageContent(), masterPublicKey)) {
                        System.out.println("[ UnicastClient ] Mestre enviou o comando: " + messageContent.getCommand());
                        if(messageContent.getCommand().equals("tempo")) {
                            LocalTime clock = getTime();
                            System.out.println("[ UnicastClient ] Enviando tempo local para o mestre: " + clock);
                            out.println(clock);
                        } else {
                            System.out.println("[ UnicastClient ] Ajuste requisitado pelo mestre: " +
                                    messageContent.getMessage());
                            this.adjustment += Long.parseLong(messageContent.getMessage());
                        }
                    } else {
                        System.out.println("[ UnicastClient ] Erro ao validar assinatura digital do mestre.");
                    }
                } catch (Exception e) {
                    System.out.println("[ UnicastClient ] Erro na comunicacao com o mestre " + e);
                    kill = true;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void killThread(boolean kill) {
        this.kill = kill;
    }

    private LocalTime getTime() {
        return LocalTime.now().plusNanos(this.adjustment);
    }

}
