package com.trabalho1;

import java.io.*;
import java.net.Socket;
import java.time.LocalTime;
import java.util.concurrent.BlockingQueue;

public class UnicastConnection extends Thread {

    private Socket client;
    private KeyPair keyPair;
    private final BlockingQueue<LocalTime> messageQueue;
    private final ClockSyncAlgorithm clockSyncAlgorithm;

    UnicastConnection(Socket client, KeyPair keyPair,
                      BlockingQueue<LocalTime> messageQueue, ClockSyncAlgorithm clockSyncAlgorithm) {
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
                System.out.println("[ UnicastConnection ] Resposta do escravo: " + response);

                messageQueue.add(LocalTime.parse(response));

                //System.out.println("Enviado " + response);

                synchronized (clockSyncAlgorithm) {
                    clockSyncAlgorithm.wait();
                }

                double adjustment = clockSyncAlgorithm.getAdjustment();
                System.out.println("[ UnicastConnection ] Ajuste a ser feito: " + adjustment);
                message.setCommand("ajuste ");
                message.setMessage(Double.toString(adjustment));
                message.setSignedMessage(keyPair.sign(message.getUnicastMessage()));
                encodedMessage = MessageSerializer.encode(message);
                out.writeInt(encodedMessage.length);
                out.write(encodedMessage);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("[ UnicastConnection ] Erro no socket. " + e);
            clockSyncAlgorithm.decrementConnectedPeers();
        }
    }

}
