import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class cli {
	private static final Pattern PLATE_PATTERN = Pattern.compile("(([0-9]{2}|[A-Z]{2}|[a-z]{2})-([0-9]{2}|[A-Z]{2}|[a-z]{2})-([0-9]{2}|[A-Z]{2}|[a-z]{2}))");

	public static void showMenu() {
		System.out.println("============= MENU =============");
		System.out.println(" 1 - Register a new User");
		System.out.println(" 2 - Search for user");
		System.out.println(" 3 - Exit");
		System.out.print("Please choose an option (1 to 3) >");
	}

	public static void main(String args[]) throws Exception {

		// Declaration of socket
		DatagramSocket clientSocket = new DatagramSocket();

		InetAddress IPAddress = InetAddress.getByName("localhost");

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

				// Send the packet
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 4445);
				clientSocket.send(sendPacket);

				// Receive the packet back
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(receivePacket);

				// Write the packet received
				String modifiedSentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
				System.out.println("\n\nFROM SERVER:" + modifiedSentence + "\n\nPress enter to go back");
				System.in.read();
				System.out.println("\n\n\n\n\n");
			}
		} while (optMenu != 3);

		clientSocket.close();
	}

}
