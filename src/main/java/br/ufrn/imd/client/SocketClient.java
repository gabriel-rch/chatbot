package br.ufrn.imd.client;

import java.io.*;
import java.net.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SocketClient {
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Scanner scanner = new Scanner(System.in);
    private String message;

    public SocketClient(String serverAddress, int serverPort) {
        try {
            this.socket = new Socket(serverAddress, serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int readPort() {
        int port = 0;
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Digite a porta que deseja se conectar: ");
            port = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.err.println("Porta inválida. Por favor, insira um número válido.");
        }

        return port;
    }

    public void connect() {
        try {
            // Para enviar mensagens para o servidor
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();

            //Para ler mensagens enviadas pelo servidor e digitadas pelo usuário
            input = new ObjectInputStream(socket.getInputStream());

            if (output != null) {
                System.out.println("Conexão estabelecida com o servidor.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startConversation() {
        try {
            do {
                //recebe mensagem do servidor
                message = (String) input.readObject();

                if (message != null) {
                    System.out.println("Server: " + message);
                }

                //escreve mensagem para o servidor
                System.out.print("Client >> ");
                message = scanner.nextLine();
                output.writeObject(message);
                output.flush();

            } while (!message.equalsIgnoreCase("fim"));
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        final String ADDRESS = "localhost";
        final int PORT = readPort();

        SocketClient client = new SocketClient(ADDRESS, PORT);

        client.connect();
        client.startConversation();

        // Encerramento da conexão após o loop
        client.output.close();
        client.input.close();
        client.socket.close();
    }
}
