package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import client.ClientInterface;

public class Server extends UnicastRemoteObject implements ServerInterFace {

	private static final long serial = 1L;
	private ArrayList<ClientInterface> chatClients;

	protected Server() throws RemoteException {
		chatClients = new ArrayList<ClientInterface>();
	}

	@Override
	public synchronized void registerClient(ClientInterface chatClient) throws RemoteException {
		this.chatClients.add(chatClient);
	}

	@Override
	public synchronized void broadcastMessage(String message) throws RemoteException {
		int i = 0;
		while (i < chatClients.size()) {
			chatClients.get(i++).retreiveMessage(message);
		}
	}

}