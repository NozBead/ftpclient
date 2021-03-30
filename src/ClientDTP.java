package projet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public abstract class ClientDTP implements Runnable{
	protected Socket connection;
	
	protected OutputStream out;
	protected InputStream in;
	
	protected File f;
	
	protected DTPType type;
	
	ClientDTP(OutputStream out, DTPType type){
		this.out = out;
		this.type = type;
	}
	
	ClientDTP(String path, DTPType type){
		this.type = type;
		String filename = (path.contains("/")) ? path.substring(path.lastIndexOf('/')) : path;
		f = new File(getCwdPath(), filename);
	}
	
	private String getCwdPath() {
		return System.getProperty("user.dir");
	}
	
	private void transfer() throws IOException {
		byte[] buffer = new byte[256];
		int size = in.read(buffer, 0, 256);
		
		while(size != -1) {
			out.write(buffer, 0, size);
			size = in.read(buffer, 0, 256);
		}
		
		in.close();
		out.close();
	}
	
	public abstract void connect() throws IOException;

	private boolean createFile(File f) {
		if(f.exists())
			f.delete();
		
		try {
			return f.createNewFile();
		} catch (IOException e) {
			return false;
		}
	}
	
	public void run() {
		try {
			connect();
			
			switch (type) {
				case FILE_RECIEVER:
					createFile(f);
					out = new FileOutputStream(f);
					in = connection.getInputStream();
				break;

				case FILE_SENDER:
					out = connection.getOutputStream();
					in = new FileInputStream(f);
				break;
				
				case TXT_RECIEVER:
					in = connection.getInputStream();
				break;
			}
			
			transfer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
