import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.TimerTask;

public class Advertise extends TimerTask {

	private int MultiCastPort, ServicePort;
	private String MultiCastAddress, ServiceAddress;

	public Advertise(String MultiCastAddress, int MultiCastPort, String ServiceAddress, int ServicePort) {
		this.MultiCastAddress = MultiCastAddress;
		this.MultiCastPort = MultiCastPort;
		this.ServiceAddress = ServiceAddress;
		this.ServicePort = ServicePort;
	}

	public void run() {

		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(MultiCastAddress);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try (DatagramSocket serverSocket = new DatagramSocket()) {

			String msg = ServiceAddress + " " + ServicePort;

			DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, addr, MultiCastPort);
			serverSocket.send(msgPacket);

			System.out.println(
					"multicast: " + MultiCastAddress + " " + MultiCastPort + ": " + ServiceAddress + " " + ServicePort);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}