package projet;

import java.io.IOException;

public interface Command {
	public boolean startFunction(ClientPI pi, String[] cmd) throws IOException;
}
