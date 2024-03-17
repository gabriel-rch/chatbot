package br.ufrn.imd.server;

import br.ufrn.imd.interfaces.Chat;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer implements Chat {
    private final Chatbot bot;

    public RMIServer() {
        bot = new Chatbot();
    }

    @Override
    public String ask(String question) throws RemoteException {
        System.out.println("Client: " + question);
        var answer = bot.answer(question);
        System.out.println("Server: " + answer);

        return answer;
    }

    public static void main(String[] args) {
        try {
            var server = new RMIServer();
            var stub = (Chat) UnicastRemoteObject.exportObject(server, 0);

            var registry = LocateRegistry.createRegistry(1337);
            registry.bind("Chat", stub);

            System.out.println("Server ready!");
        } catch (AlreadyBoundException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
