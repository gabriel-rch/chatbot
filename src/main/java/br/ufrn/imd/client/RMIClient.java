package br.ufrn.imd.client;

import br.ufrn.imd.interfaces.Chat;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

public class RMIClient {
    public void startConversation(Chat stub) {
        Scanner scanner = new Scanner(System.in);
        String question, answer = null;
        do {
            if (answer != null) {
                System.out.println("Server: " + answer);
            }

            System.out.print("Client >> ");
            question = scanner.nextLine();

            try {
                answer = stub.ask(question);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }

        } while (!question.equalsIgnoreCase("fim"));
    }
    
    public static void main(String[] args) {
        try {
            var registry = LocateRegistry.getRegistry(null, 1337);
            var chat = (Chat) registry.lookup("Chat");

            var client = new RMIClient();
            client.startConversation(chat);

        } catch (NotBoundException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
