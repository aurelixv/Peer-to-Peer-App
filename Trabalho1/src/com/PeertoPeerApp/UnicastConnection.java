package com.PeertoPeerApp;

import java.io.*;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
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
                MessageContent messageContent = message.getMessageContent();
                messageContent.setCommand("tempo");
                messageContent.setTime();
                message.setSignedMessage(keyPair.sign(Arrays.toString(MessageSerializer.encode(messageContent))));
                byte [] encodedMessage = MessageSerializer.encode(message);
                // Envia para o escravo a solicitacao de tempo
                out.writeInt(encodedMessage.length);
                out.write(encodedMessage);
                LocalTime sentTime = LocalTime.now();

                // Espera a resposta do escravo
                String response = in.readLine();
                long rtt = Duration.between(sentTime, LocalTime.now()).getNano();
                System.out.println("[ UnicastConnection ] Resposta do escravo: " + response + " RTT: "  + rtt);

                LocalTime estimated = LocalTime.parse(response).plusNanos(rtt/2);
                System.out.println("[ UnicastConnection ] Estimado: " + estimated);

                sleep(PeerInfo.deltaT2 * 1000);

                messageQueue.add(estimated);

                //System.out.println("Enviado " + response);

                synchronized (clockSyncAlgorithm) {
                    clockSyncAlgorithm.wait();
                }

                long meanTime = clockSyncAlgorithm.getMeanTime();
                long adjustment = meanTime - estimated.getNano();
                System.out.println("[ UnicastConnection ] Ajuste a ser feito: " + adjustment);
                messageContent.setCommand("ajuste ");
                messageContent.setTime();
                messageContent.setMessage(Long.toString(adjustment));
                message.setSignedMessage(keyPair.sign(Arrays.toString(MessageSerializer.encode(messageContent))));
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
