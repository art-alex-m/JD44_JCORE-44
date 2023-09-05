package ru.netology;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        int port = 8085;

        while (true) {
            try (ServerSocket serverSocket = new ServerSocket(port);
                 Socket clientSocket = serverSocket.accept();
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ) {
                System.out.println("New client is connected. Listening...");

                out.println("Hello! Write your name, please");

                String name = reader.readLine();

                out.println(String.format(
                        "%s, your are connected from %s:%s",
                        name,
                        clientSocket.getInetAddress().getHostAddress(),
                        clientSocket.getPort()
                ));

                System.out.println("Connection close");
            }
        }
    }
}