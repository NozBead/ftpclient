package projet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;

public class ClientPI {
	private StringBuilder builder;
	
	private ControlResponse responseControl;
	
	private ServerState serverState;
	private ClientState clientState;
	
	private NetClientPI connection;
	
	public ClientPI(String host, int port) throws UnknownHostException, IOException {
		builder = new StringBuilder();
		responseControl = new ControlResponse();
		
		connection = new NetClientPI(host, port);
		
		clientState = ClientState.PASSIVE;
		serverState = ServerState.WAITING_CMD;
		
		System.out.println(connection.getResponse());
	}
	
	private void wait_server() throws IOException {
		String rawResponse = connection.getResponse();
		responseControl.treatRawResponse(rawResponse);
		
		serverState = ServerState.getStateFromCode(responseControl.getCode());
		
		while(serverState.equals(ServerState.OCCUPIED)) {
			rawResponse = connection.getResponse();
			responseControl.treatRawResponse(rawResponse);
			
			serverState = ServerState.getStateFromCode(responseControl.getCode());
		}
	}
	
	public String getCurrentMsg() {
		return responseControl.getMsg();
	}
	
	public boolean authentificate(String user, String password) throws IOException {
		boolean success = sendCommandAndWait("USER", user);
		
		if(success && serverState.equals(ServerState.WAITING_INFOS) ) {
			return sendCommandAndWait("PASS", password); 
		}
		else {
			return false;
		}
	}
	
	public boolean setupTransferParams() throws IOException {
		return sendCommandAndWait("TYPE","I") && sendCommandAndWait("MODE", "S");
	}
	
	public boolean sendFile(String path) throws IOException {
		return transferFile(path,DTPType.FILE_SENDER) && sendCommandAndWait("STOR", path.substring(path.lastIndexOf('/')));
	}
	
	public boolean recieveFile(String path) throws IOException {
		return transferFile(path,DTPType.FILE_RECIEVER) && sendCommandAndWait("RETR", path);
	}

	private boolean transferFile(String path, DTPType type) throws IOException {
		boolean setupSuccess;
		
		if(clientState.equals(ClientState.ACTIVE)) {
			setupSuccess = setupActive(new ClientDTPActv(path,type));
		}
		else {
			setupSuccess = setupPassive(new ClientDTPPasv(path,type));
		}
		
		return setupSuccess;
	}
	
	public boolean setMode(boolean passive) {
		if(passive)
			clientState = ClientState.PASSIVE;
		else
			clientState = ClientState.ACTIVE;
		
		return true;
	}

	public boolean changeDir(String path) throws IOException {
		return sendCommandAndWait("CWD", path);
	}
	
	public String listFiles() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		if(clientState.equals(ClientState.ACTIVE)) {
			setupActive(new ClientDTPActv(out,DTPType.TXT_RECIEVER));
		}
		else {
			setupPassive(new ClientDTPPasv(out,DTPType.TXT_RECIEVER));
		}
		
		sendCommandAndWait("LIST");
			
		return new String(out.toByteArray());
	}
	
	private boolean setupActive(ClientDTPActv dtp) throws IOException {
		int port = dtp.findPort();
		
		new Thread(dtp).start();
		
		return sendCommandAndWait("PORT", getPortFormat(port));
	}
	
	private boolean setupPassive(ClientDTPPasv dtp) throws IOException {
		if(!sendCommandAndWait("PASV"))
			return false;
		
		dtp.setupHostAndPort(responseControl.getMsg());
		
		new Thread(dtp).start();
		
		return true;
	}
	
	private String getPortFormat(int port) {
		builder.setLength(0);
		
		builder.append(connection.getRemoteAddr().replace('.', ','));
		builder.append(",");
		builder.append(port >> 8).append(",").append(port & 0xFF);
		
		return builder.toString();
	}
	
	private String createCommand(String command, String... params) {
		builder.setLength(0);
		
		builder.append(command);
		
		for(String s : params) {
			builder.append(" ").append(s);
		}
		
		return builder.toString();
	}
	
	private boolean sendCommandAndWait(String command, String... params) throws IOException {
		command = createCommand(command,params);
		connection.sendMessage(command);
		wait_server();
		return !responseControl.isNegative();
	}
}
