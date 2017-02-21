import java.io.*;
import java.net.*;
import java.util.*;

public class ser {
	static ArrayList<user> users = new ArrayList<>();
	
	public static String addNewUser(String[] partsMSG){
		String plate = partsMSG[1];
		String name = partsMSG[2];
		for(user us : users){
			if(us.getUserPlate().equals(plate))
				return new String("-1: Plate exists");
		}
		users.add(new user(name,plate));
		Integer number = users.size();
		return new String(number.toString());
	}
	
	public static String getUser(String[] partsMSG){
		String plate = partsMSG[1];

		for(user us : users){
			if(us.getUserPlate().equals(plate))
				return new String(" " + us.getUserPlate() + " " + us.getUserName());
		}
		return new String("NOT FOUND");
	}
	
	public static void main(String args[]) throws Exception {
		DatagramSocket serverSocket = new DatagramSocket(4445);
		
		while (true) {
			byte[] receiveData = new byte[1024];
			byte[] sendData = new byte[1024];

			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);

			//Parse received data
			String sentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
			System.out.println("RECEIVED:" + sentence);
			
			String[] partsMSG = sentence.split(",");
			String type = partsMSG[0];
			
			switch (type) {
			case "REGISTER":
				sentence = addNewUser(partsMSG);
				break;
			case "LOOKUP":
				sentence = getUser(partsMSG);
				break;
			default:
				break;
			}
			
			//Send data
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			sendData = sentence.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);
		}

	}
}
