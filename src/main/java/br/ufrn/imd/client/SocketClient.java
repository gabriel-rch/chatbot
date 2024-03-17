package br.ufrn.imd.client;

import br.ufrn.imd.interfaces.Client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class SocketClient implements Client {
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public boolean connect() {
        try {
            socket = new Socket("localhost", 1338);

            // Para enviar mensagens para o servidor
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();

            //Para ler mensagens enviadas pelo servidor e digitadas pelo usuário
            input = new ObjectInputStream(socket.getInputStream());

            if (output != null) {
                System.out.println("Conexão estabelecida com o servidor.");
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public void startConversation() {
        try {
            String message;
            do {
                // Recebe mensagem do servidor
                message = (String) input.readObject();

                if (message != null) {
                    System.out.println("Server: " + message);
                }

                // Escreve mensagem para o servidor
                System.out.print("Client >> ");
                message = scanner.nextLine();
                output.writeObject(message);
                output.flush();

            } while (!message.equalsIgnoreCase("fim"));

            // Encerramento da conexão após o loop
            input.close();
            output.close();
            socket.close();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
