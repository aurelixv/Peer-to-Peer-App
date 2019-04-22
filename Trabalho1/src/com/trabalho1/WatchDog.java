package com.trabalho1;

import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.security.PublicKey;
import java.time.Clock;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import static com.trabalho1.MessageSerializer.decode;

public class WatchDog extends Thread{

    private Clock clock;
    private MulticastSocket s;
    private boolean kill;
    private PublicKey masterPublicKey;

    WatchDog(MulticastSocket s, PublicKey masterPublicKey) {
        this.s = s;
        this.kill = false;
        this.clock = Clock.systemDefaultZone();
        this.masterPublicKey = masterPublicKey;
    }

    public void run() {

        try {
            System.out.println("[ WatchDog ] WatchDog iniciado com sucesso.");

            LocalTime oldTime = LocalTime.now(clock);

            while(!kill) {
                byte[] buffer = new byte[1000];
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                s.receive(messageIn);
                Message message = (Message)decode(messageIn.getData());
                MessageContent messageContent = message.getMessageContent();

                if(messageContent.getPeerName().equals(PeerInfo.master)) {
                    if(MessageSignature.verify(Arrays.toString(MessageSerializer.encode(messageContent)),
                            message.getSignedMessageContent(), masterPublicKey)) {
                        System.out.println("[ WatchDog ] MESTRE VIVO");
                        oldTime = LocalTime.now(clock);
                    } else {
                        System.out.println("[ WatchDog ] MESTRE IMPOSTOR");
                    }
                }
                if(LocalTime.now(clock).minus(PeerInfo.deltaT1, ChronoUnit.SECONDS).isAfter(oldTime)) {
                    System.out.println("[ WatchDog ] MESTRE MORREU");
                    kill = true;
                }

            }
        } catch (Exception e){
            System.out.println("Erro na thread watchdog " + e);
            e.printStackTrace();
        }

    }

}
