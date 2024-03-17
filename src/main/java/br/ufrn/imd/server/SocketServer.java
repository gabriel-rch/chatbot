package br.ufrn.imd.server;

import java.io.*;
import java.net.*;

public class SocketServer {
    private final Chatbot bot;
    private final ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    final int PORT = 1338;

    public SocketServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            bot = new Chatbot();
            System.out.println("Servidor iniciado. Aguardando conexões...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean connect() {
        try {
            clientSocket = serverSocket.accept();
            System.out.println("Novo cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

            // Para enviar mensagens para o servidor
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            output.flush();

            //Para ler mensagens enviadas pelo servidor e digitadas pelo usuário
            input = new ObjectInputStream(clientSocket.getInputStream());

            if (output != null) {
                output.writeObject("Conexão estabelecida com sucesso...\n");
                return true;
            }
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean startConversation() {
        try {
            while (true) {
                //recebe message do cliente
                String message = (String) input.readObject();

                if (message.equalsIgnoreCase("fim")) {
                    return false;
                }

                System.out.println("Client: " + message);
                var answer = bot.answer(message);
                System.out.println("Server: " + answer);

                output.writeObject(answer);
                output.flush();
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection() {
        try {
            if (output != null) {
                output.close();
            }
            if (input != null) {
                input.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        boolean running;
        SocketServer server = new SocketServer();

        running = server.connect();

        if (running) {
            do {
                running = server.startConversation();
            } while (running);
        }

        server.closeConnection();
        System.out.println("Conexão encerrada pelo cliente");
    }
}
