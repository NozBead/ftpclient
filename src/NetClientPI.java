package projet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetClientPI {
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	
	public NetClientPI(String host, int port) throws UnknownHostException, IOException {
		connect(host,port);
	}
	
	public void connect(String host, int port) throws UnknownHostException, IOException {
		socket = new Socket(host,port);
		out = new PrintWriter(socket.getOutputStream());
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	public void disconnect() throws IOException {
		socket.close();
		out = null;
		in = null;
	}
	
	public void sendMessage(String s) {
		out.print(s + "\r\n");
		out.flush();
	}
	
	public String getResponse() throws IOException {
		String s = in.readLine();
		return s;
	}
	
	public int getLocalPort() {
		return socket.getLocalPort();
	}
	
	public String getRemoteAddr() {
		return ((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress().getHostAddress();
	}
}
