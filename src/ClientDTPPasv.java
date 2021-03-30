package projet;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ClientDTPPasv extends ClientDTP{
	private String host;
	private int port;
	
	ClientDTPPasv(String filename,DTPType type) {
		super(filename,type);
	}
	
	ClientDTPPasv(OutputStream out, DTPType type){
		super(out,type);
	}
	
	public void setupHostAndPort(String serverIndications) {
		String indications = serverIndications.substring(serverIndications.indexOf('(') + 1, serverIndications.indexOf(')'));
		String[] elts = indications.split(",");
		
		host = elts[0] + "." + elts[1] + "." + elts[2] + "." + elts[3];
		port = Integer.parseInt(elts[4]) * 256 +  Integer.parseInt(elts[5]);
	}

	public void connect() throws IOException{
		connection = new Socket(host,port);
	}
}
