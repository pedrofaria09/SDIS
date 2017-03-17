import java.io.*;
import java.net.*;
import java.util.*;

public class serProtocol {
	static ArrayList<user> users = new ArrayList<>();
	private Socket socketReceived;

	public serProtocol(Socket socket) {
		socketReceived = socket;
	}

	public static String addNewUser(String[] partsMSG) {
		String plate = partsMSG[1];
		String name = partsMSG[2];
		for (user us : users) {
			if (us.getUserPlate().equals(plate))
				return new String("-1: Plate exists");
		}
		users.add(new user(name, plate));
		Integer number = users.size();
		return new String(number.toString());
	}

	public static String getUser(String[] partsMSG) {
		String plate = partsMSG[1];

		for (user us : users) {
			if (us.getUserPlate().equals(plate))
				return new String(" " + us.getUserPlate() + " " + us.getUserName());
		}
		return new String("NOT FOUND");
	}

	public void execute() throws IOException {
		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socketReceived.getInputStream()));
		String sentence = inFromClient.readLine();

		System.out.println("RECEIVED:" + sentence);
		
		//Parse
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

		// Send data
		PrintWriter outToClient = new PrintWriter(socketReceived.getOutputStream(), true);
		outToClient.println(sentence);
		outToClient.close();
		socketReceived.close();
	}
}