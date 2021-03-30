package projet;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.util.Random;

public class ClientDTPActv extends ClientDTP{
	ServerSocket socket;
	
	ClientDTPActv(String filename,DTPType type) {
		super(filename,type);
	}
	
	ClientDTPActv(OutputStream out, DTPType type){
		super(out,type);
	}
	
	public int findPort() {
		int portbefore = 0;
		int port = 0;
		boolean portKo = true;

		while(portKo) {
			try {
				portbefore = port;
				port = getRandomPort();
				socket = new ServerSocket(port);
			}
			catch(IOException e) {
				portKo = false;
			}
		}
		
		return portbefore;
	}
	
	private int getRandomPort() {
		Random rng = new Random();
		
		return rng.nextInt(65535 - 1024) + 1024;
	}
	
	public void connect() throws IOException {
		connection = socket.accept();
	}
}
