package br.ufrn.imd.server;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class SocketServer {
    final int PORT = 12345;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Scanner scanner = new Scanner(System.in);
    private String message;

    public SocketServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciado. Aguardando conexões...");
        } catch (IOException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return false;
    }

    public boolean startConversation() {
        try {
            while (true) {
                //recebe message do cliente
                message = (String) input.readObject();

                if (message.equalsIgnoreCase("fim")) {
                    return false;
                }

                if (message != null) {
                    System.out.println("Client: " + message);
                }

                //escreve message para o servidor
                System.out.print("Server >> ");
                message = scanner.nextLine();
                output.writeObject(message);
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
            e.printStackTrace();
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
