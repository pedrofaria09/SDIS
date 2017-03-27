package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.ClientInterface;

public interface ServerInterFace extends Remote {
	void registerClient(ClientInterface chatClient) throws RemoteException;
	void broadcastMessage(String message) throws RemoteException;
}
