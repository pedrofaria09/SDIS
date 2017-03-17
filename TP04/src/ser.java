import java.io.*;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ser implements IServer {
	static ArrayList<user> users = new ArrayList<>();

	public ser() {
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

	public static void main(String args[]) throws Exception {
		ser server = new ser();
		Registry reg = LocateRegistry.getRegistry();
		IServer iserver = (IServer) UnicastRemoteObject.exportObject(server, 0);
		
		reg.bind("teste", iserver);
	}

	@Override
	public String request(String request) {
		String[] partsMSG = request.split(",");
		String type = partsMSG[0];
		String ret = null;
		
		switch (type) {
		case "REGISTER":
			ret = addNewUser(partsMSG);
			break;
		case "LOOKUP":
			ret = getUser(partsMSG);
			break;
		default:
			break;
		}
		
		return ret;
	}

}
