package br.ufrn.imd.client;

import br.ufrn.imd.interfaces.Chat;
import br.ufrn.imd.interfaces.Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

public class RMIClient implements Client {
    private Chat chat;

    @Override
    public boolean connect() {
        try {
            var registry = LocateRegistry.getRegistry("localhost", 1337);
            chat = (Chat) registry.lookup("Chat");
        } catch (RemoteException | NotBoundException e) {
            return false;
        }
        return true;
    }

    @Override
    public void startConversation() {
        Scanner scanner = new Scanner(System.in);
        String question, answer = null;
        do {
            if (answer != null) {
                System.out.println("Chatbot: " + answer);
            }

            System.out.print("You >> ");
            question = scanner.nextLine();

            try {
                answer = chat.ask(question);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }

        } while (!question.equalsIgnoreCase("bye"));
    }
}
