import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class cli {
	public static final Pattern PLATE_PATTERN = Pattern.compile("(([0-9]{2}|[A-Z]{2}|[a-z]{2})-([0-9]{2}|[A-Z]{2}|[a-z]{2})-([0-9]{2}|[A-Z]{2}|[a-z]{2}))");
	public static final int port = 5000;
	public static final String group = "225.4.5.6";
	public static final int ttl = 1;
	
	public static void showMenu() {
		System.out.println("============= MENU =============");
		System.out.println(" 1 - Register a new User");
		System.out.println(" 2 - Search for user");
		System.out.println(" 3 - Exit");
		System.out.print("Please choose an option (1 to 3) >");
	}

	public static void main(String args[]) throws Exception {

		// Declaration of socket
		MulticastSocket clientSocket = new MulticastSocket(port);
		InetAddress inatAdd = InetAddress.getByName(group);
		clientSocket.joinGroup(inatAdd);
		
		// More Declarations
		Scanner in = new Scanner(System.in);
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];

		String sentence = null, name = null, plate = null;
		Integer optMenu = null;

		// Ask for data
		do {
			boolean goodPlate = false;
			boolean flag = false;
			showMenu();
			optMenu = in.nextInt();
			
			switch (optMenu) {
			case 1:
				System.out.print("Please enter a name for the user >");
				name = inFromUser.readLine();
				while (!goodPlate) {
					System.out.print("Please enter a plate XX-XX-XX >");
					plate = inFromUser.readLine();
					if (PLATE_PATTERN.matcher(plate).matches())
						goodPlate = true;
				}
				sentence = new String("REGISTER," + plate + "," + name);
				flag = true;
				break;
			case 2:
				while (!goodPlate) {
					System.out.print("Please enter a plate XX-XX-XX to search for user>");
					plate = inFromUser.readLine();
					if (PLATE_PATTERN.matcher(plate).matches())
						goodPlate = true;
				}
				sentence = new String("LOOKUP," + plate);
				flag = true;
				break;
			case 3:
				System.out.println("Leaving!!!");
				break;
			default:
				System.out.println("Wrong option choosed!");
				break;
			}

			if (flag) {
				sendData = sentence.getBytes();
				
				byte[] adbuf = new byte[256];
				DatagramPacket adPacket = new DatagramPacket(adbuf, adbuf.length);
				clientSocket.receive(adPacket);
				
				String adMSG = new String(adbuf, 0, adbuf.length);
				String[] addivided = adMSG.split(" ");
				String ServiceAddress = addivided[0];
				int ServicePort = Integer.parseInt(addivided[1].trim());

				// Send the packet
				DatagramSocket socket = new DatagramSocket();
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ServiceAddress), ServicePort);
				socket.send(sendPacket);

				// Receive the response from server
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				socket.receive(receivePacket);
				//clientSocket.leaveGroup(inatAdd);
				
				// Write the packet received
				String receivedBack = new String(receivePacket.getData(), 0, receivePacket.getLength());
				System.out.println("\n\nFROM SERVER:" + receivedBack + "\n\nPress enter to go back");
				System.in.read();
				System.out.println("\n\n\n\n\n");
			}
		} while (optMenu != 3);
		clientSocket.leaveGroup(inatAdd);
		clientSocket.close();
	}

}
