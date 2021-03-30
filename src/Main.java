package projet;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {
	private Scanner sc;
	private ClientPI protocol;
	private CLIntepreter intrep;
	
	public Main() {
		sc = new Scanner(System.in);
		intrep = new CLIntepreter(sc);
	}
	
	public static void main(String[] args) {
		if(args.length != 2) {
			System.out.println("Commande incorrecte : ftp <hôte> <port>");
			return;
		}	
		
		Main main = new Main();
		
		if(!main.connectProtocol(args))
			return;
		
		try {
			if(!main.setupServer()) {
				return;
			}
			main.loopCLI();
		} catch (IOException e) {
			System.err.println("Erreur réseaux : " + e.getMessage());
			return;
		}
	}
	
	public void loopCLI() throws IOException{
		String[] cmd = intrep.getCommand();
			
			while(!cmd[0].toLowerCase().equals("exit")) {
				
				try {
					ECommands.valueOf(cmd[0].toUpperCase()).startFunction(protocol, cmd);
				}
				catch(IllegalArgumentException e) {
					System.err.println("Commande inconnue");
				}
				
				cmd = intrep.getCommand();
			}
	}
	
	public boolean setupServer() throws IOException {
		String user, pass;
		
		System.out.print("Nom d'utilisateur : ");
		user = sc.nextLine();
		System.out.print("Mot de passe : ");
		pass = sc.nextLine();
		
		if(!protocol.authentificate(user, pass)) {
			System.err.println("Identifiants incorrecte");
			return false;
		}
		else if(!protocol.setupTransferParams()) {
			System.err.println("Impossible de paramétrer le serveur pour les transfers");
			return false;
		}
		
		return true;
	}
	
	public boolean connectProtocol(String[] args) {
		try {
			protocol = new ClientPI(args[0], Integer.parseInt(args[1]));
			return true;
		} catch (NumberFormatException e) {
			System.err.println("Erreur de Port : " + e.getMessage());
			return false;
		} catch (UnknownHostException e) {
			System.err.println("Erreur format de l'hôte : " + e.getMessage());
			return false;
		} catch (IOException e) {
			System.err.println("Impossible de se connecter au serveur : " + e.getMessage());
			return false;
		}
	}
}
