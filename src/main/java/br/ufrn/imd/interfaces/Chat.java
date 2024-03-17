package br.ufrn.imd.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Chat extends Remote {
    String ask(String question) throws RemoteException;
}
