package com.trabalho1;

import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.concurrent.BlockingQueue;

import static com.trabalho1.MessageSerializer.*;

public class MulticastListener extends Thread {

    private MulticastSocket s;
    private BlockingQueue<Message> listenerQueue;
    private boolean kill;

    public MulticastListener(MulticastSocket s, BlockingQueue<Message> listenerQueue) {
        this.s = s;
        this.listenerQueue = listenerQueue;
        this.kill = false;
    }

    public void run() {
        try {
            System.out.println("Thread Listener iniciada com sucesso.");

            while(kill == false) {
                byte[] buffer = new byte[1000];
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                s.receive(messageIn);
                Message message = decode(messageIn.getData());

                // Se nao for ele mesmo
                if(message.getPeerPort() != PeerInfo.port) {
                    listenerQueue.put(message);
                }

                System.out.println(message.getPeerName() + " na porta " + message.getPeerPort());
                System.out.println("Assinatura valida: " + MessageSignature.verify(message.getMessage(),
                        message.getSignedMessage(),
                        message.getPublicKey()));
            }
        } catch (Exception e){
            System.out.println("Erro na thread listener " + e);
            e.printStackTrace();
        }
    }

    public void killThread(boolean kill) {
        this.kill = kill;
    }

}
