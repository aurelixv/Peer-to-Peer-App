package com.trabalho1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class UnicastConnection extends Thread {

    private Socket client;

    public UnicastConnection(Socket client) {
        this.client = client;
    }

    public void run() {

        PrintWriter out;
        BufferedReader in;
        String input;

        try {
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            while((input = in.readLine()) != null) {
                System.out.println(input);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
