package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import server.ServerInterFace;

public class Client extends UnicastRemoteObject implements ClientInterface, Runnable {
	private static final long serial = 1L;
	private ServerInterFace chatServer;
	private String name = null;

	protected Client(String name, ServerInterFace chatServer) throws RemoteException {
		this.name = name;
		this.chatServer = chatServer;
		chatServer.registerClient(this);
	}

	@Override
	public void retreiveMessage(String message) throws RemoteException {
		System.out.println(message);
	}

	public void run() {
		Scanner scanner = new Scanner(System.in);
		String message;
		while (true) {
			message = scanner.nextLine();
			try {
				chatServer.broadcastMessage(name + " : " + message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}