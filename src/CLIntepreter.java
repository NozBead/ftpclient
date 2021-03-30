package projet;

import java.util.Scanner;

public class CLIntepreter {
	private Scanner in;
	
	public CLIntepreter(Scanner in) {
		this.in = in;
	}

	public CLIntepreter() {
		this(new Scanner(System.in));
	}
	
	public String[] getCommand() {
		System.out.print("ftp> ");
		return in.nextLine().split(" +");
	}
}
