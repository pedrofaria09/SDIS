package client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import server.ServerInterFace;

public class ClientDriver {

	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		String chatServerURL = "rmi://localhost/RMIChatServer";
		ServerInterFace chatServer = (ServerInterFace) Naming.lookup(chatServerURL);
		Scanner scanner = new Scanner(System.in);
		String name;
		
		System.out.print("Please enter a name >");
		name = scanner.nextLine();
		
		new Thread(new Client(name, chatServer)).start();
	}
}