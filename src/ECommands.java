package projet;

import java.io.IOException;

public enum ECommands {
	PUT(1, "put <path>", (pi, cmd) -> {
		return pi.sendFile(cmd[1]);
	}),
	GET(1, "get <path>", (pi, cmd) -> {
		return pi.recieveFile(cmd[1]);
	}),
	LS(0, "ls", (pi, cmd) -> {
		System.out.println(pi.listFiles());
		return true;
	}),
	CD(1, "cd <path>", (pi, cmd) -> {
		return pi.changeDir(cmd[1]);
	}),
	PASSIVE(1, "passive <on/off>", (pi, cmd) -> {
		return pi.setMode(cmd[1].equals("on"));
	});
	
	private final int paramsNbr;
	private final String format;
	private final Command func;
	
	private ECommands(int paramsNbr, String format, Command func) {
		this.paramsNbr = paramsNbr;
		this.format = format;
		this.func = func;
	}
	
	public void startFunction(ClientPI pi, String[] cmd) throws IOException {
		if(cmd.length != paramsNbr + 1) {
			System.err.println("Commande incorrecte : " + format);
		} else
			if(!func.startFunction(pi, cmd))
				System.err.println("Erreur : " + pi.getCurrentMsg());
			else
				System.out.println("Opération terminée");
	}
}
