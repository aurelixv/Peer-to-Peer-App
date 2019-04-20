package com.trabalho1;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class UnicastConnection extends Thread {

    private Socket client;
    private KeyPair keyPair;
    private BlockingQueue<Integer> messageQueue;
    private ClockSyncAlgorithm clockSyncAlgorithm;

    public UnicastConnection(Socket client, KeyPair keyPair,
                             BlockingQueue<Integer> messageQueue, ClockSyncAlgorithm clockSyncAlgorithm) {
        this.keyPair = keyPair;
        this.client = client;
        this.messageQueue = messageQueue;
        this.clockSyncAlgorithm = clockSyncAlgorithm;
    }

    public void run() {

        DataOutputStream out;
        BufferedReader in;
        String input;

        try {
            out = new DataOutputStream(client.getOutputStream());
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            while(true) {
                Message message = new Message();
                message.setCommand("tempo");
                message.setTime();
                message.setSignedMessage(keyPair.sign(message.getUnicastMessage()));
                byte [] encodedMessage = MessageSerializer.encode(message);
                // Envia para o escravo a solicitacao de tempo
                out.writeInt(encodedMessage.length);
                out.write(encodedMessage);
                sleep(PeerInfo.deltaT2 * 1000);

                // Espera a resposta do escravo
                String response = in.readLine();
                System.out.println("Resposta do escravo: " + response);
                messageQueue.add(Integer.parseInt(response));

                synchronized (clockSyncAlgorithm) {
                    clockSyncAlgorithm.wait();
                }

                int adjustment = clockSyncAlgorithm.getAdjustment();
                System.out.println("Ajuste a ser feito: " + adjustment);
                message.setCommand("ajuste " + adjustment);
                message.setSignedMessage(keyPair.sign(message.getUnicastMessage()));
                encodedMessage = MessageSerializer.encode(message);
                out.writeInt(encodedMessage.length);
                out.write(encodedMessage);
            }

        } catch (IOException | InterruptedException e) {
            //e.printStackTrace();
            System.out.println("Erro no socket.");
            clockSyncAlgorithm.decrementConnectedPeers();
        }
    }

}
