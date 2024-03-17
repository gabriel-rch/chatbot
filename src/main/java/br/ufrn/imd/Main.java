package br.ufrn.imd;

import br.ufrn.imd.client.RMIClient;
import br.ufrn.imd.client.SocketClient;
import br.ufrn.imd.interfaces.Client;

import java.util.Scanner;

public class Main {
    static boolean validate(String connectionType) {
        var valid = connectionType.equalsIgnoreCase("socket") || connectionType.equalsIgnoreCase("rmi");
        if (!valid) {
            System.out.println("Please choose either Socket or RMI.\n>> ");
        }
        return valid;
    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        var connectionType = "";

        System.out.print("Do you want to use Socket or RMI?\n>> ");

        do {
            connectionType = scanner.nextLine();
        } while (!validate(connectionType));

        Client client = switch (connectionType.toLowerCase()) {
            case "socket" ->  new SocketClient();
            case "rmi" -> new RMIClient();

            // Should be unreachable, but defaults to socket
            default -> new SocketClient();
        };

        boolean connected = client.connect();
        if (connected) {
            client.startConversation();
        } else {
            System.out.println("Could not connect. Did you start the server?");
        }
    }
}
