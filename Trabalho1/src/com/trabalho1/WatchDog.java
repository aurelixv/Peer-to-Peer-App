package com.trabalho1;

import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.time.Clock;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static com.trabalho1.MessageSerializer.decode;

public class WatchDog extends Thread{

    private Clock clock;
    private MulticastSocket s;
    private boolean kill;

    public WatchDog(MulticastSocket s) {
        this.s = s;
        this.kill = false;
        this.clock = Clock.systemDefaultZone();
    }

    public void run() {

        try {
            System.out.println("WatchDog iniciado com sucesso.");

            LocalTime oldTime = LocalTime.now(clock);

            while(!kill) {
                byte[] buffer = new byte[1000];
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                s.receive(messageIn);
                Message message = decode(messageIn.getData());

                if(message.getPeerName().equals(PeerInfo.master)) {
                    System.out.println("MESTRE VIVO");
                    oldTime = LocalTime.now(clock);
                }
                if(LocalTime.now(clock).minus(PeerInfo.deltaT1, ChronoUnit.SECONDS).isAfter(oldTime)) {
                    System.out.println("MESTRE MORREU");
                    synchronized (this) {
                        this.notify();
                    }
                    kill = true;
                }

            }
        } catch (Exception e){
            System.out.println("Erro na thread watchdog " + e);
            e.printStackTrace();
        }

    }

}
