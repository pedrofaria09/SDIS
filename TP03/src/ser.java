import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ser {
	
	public static void main(String[] args) throws IOException {
		
		ServerSocket serverSocket = new ServerSocket(6789);
		Socket receivedSocket = null;
		
		while(true){
			receivedSocket = serverSocket.accept();
			
			serProtocol proto = new serProtocol(receivedSocket);
			
			proto.execute();
		}
	}
}